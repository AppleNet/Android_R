package com.example.llc.storage.sanbox.impl;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import com.example.llc.storage.sanbox.BaseRequest;
import com.example.llc.storage.sanbox.FileAccessFactory;
import com.example.llc.storage.sanbox.FileResponse;
import com.example.llc.storage.sanbox.IFile;
import com.example.llc.storage.sanbox.annotation.DbFiled;
import com.example.llc.storage.sanbox.coy.CopyRequest;
import com.example.llc.storage.sanbox.file.FileRequest;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MediaStoreAccessImpl implements IFile {

    // uri
    public static final String AUDIO = "Audio";
    public static final String VIDEO = "Video";
    public static final String IMAGE = "Image";
    public static final String DOWNLOADS = "Downloads"; // 什么都能放

    // 外置卡的 uri 放到 hashMap 中
    private HashMap<String, Uri> uriMap = new HashMap<>();

    /**
     * 构造方法，将类型存入 uriMap 中
     */
    private MediaStoreAccessImpl() {
        uriMap.put(AUDIO, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        uriMap.put(VIDEO, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        uriMap.put(IMAGE, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        uriMap.put(DOWNLOADS, MediaStore.Downloads.EXTERNAL_CONTENT_URI);
    }

    private static final class Holder {
        private static final MediaStoreAccessImpl instance = new MediaStoreAccessImpl();
    }

    public static MediaStoreAccessImpl getInstance() {
        return Holder.instance;
    }

    /**
     * 创建文件，支持 图片，文本，音频，视频
     */
    @Override
    public <T extends BaseRequest> FileResponse newCreateFile(Context context, T baseRequest) {
        Uri uri = uriMap.get(baseRequest.getType());
        ContentValues contentValues = objectConvertValues(baseRequest);
        ContentResolver contentResolver = context.getContentResolver();
        if (uri != null) {
            Uri insertUri = contentResolver.insert(uri, contentValues);
            if (insertUri != null) {
                return new FileResponse(true, insertUri, baseRequest.getFile());
            }
        }
        return new FileResponse(false, null, null);
    }

    /**
     * 删除文件，支持 图片，文本，音频，视频
     */
    @Override
    public <T extends BaseRequest> FileResponse delete(Context context, T baseRequest) {
        Uri uri = query(context, baseRequest).getUri();
        if (uri != null) {
            int delete = context.getContentResolver().delete(uri, null, null);
            if (delete > 0) {
                return new FileResponse(true, uri, baseRequest.getFile());
            }
        }
        return new FileResponse(false, null, null);
    }

    /**
     * 重命名文件，支持 图片，文本，音频，视频
     */
    @Override
    public <T extends BaseRequest> FileResponse renameTo(Context context, T where, T request) {
        Uri uri = query(context, where).getUri();
        if (uri != null) {
            ContentValues contentValues = objectConvertValues(request);
            int update = context.getContentResolver().update(uri, contentValues, null, null);
            if (update > 0) {
                return new FileResponse(true, uri, request.getFile());
            }
        }
        return new FileResponse(false, null, null);
    }

    /**
     * 复制文件，支持 图片，文本，音频，视频
     */
    @Override
    public <T extends BaseRequest> FileResponse copyFile(Context context, T baseRequest) {
        // 目的文件可能存在 可能不存在
        CopyRequest copyRequest = (CopyRequest) baseRequest;
        // 源文件存不存在
        Uri srcUri = query(context, baseRequest).getUri();
        if (srcUri == null) {
            return new FileResponse(false, null, null);
        }
        FileRequest fileRequest = new FileRequest(copyRequest.getDistFile().getParentFile());
        fileRequest.setDisplayName(copyRequest.getDistFile().getName());
        FileAccessFactory.setFileType(fileRequest);
        if (query(context, fileRequest).isSuccess()) {
            // 存在 目的文件 删除
            delete(context, fileRequest);
            // 创建目的文件
            Uri destUri = newCreateFile(context, fileRequest).getUri();
            OutputStream outputStream = null;
            InputStream inputStream = null;
            try {
                outputStream = context.getContentResolver().openOutputStream(destUri);
                inputStream = context.getContentResolver().openInputStream(destUri);
                BufferedOutputStream fileOutputStream = new BufferedOutputStream(outputStream);
                BufferedInputStream fileInputStream = new BufferedInputStream(inputStream);
                byte[] buff = new byte[1024];
                while (fileInputStream.read(buff) != -1) {
                    fileOutputStream.write(buff);
                }
                fileInputStream.close();
                fileOutputStream.close();
            } catch (Exception e) {
                //
            }
        }
        return null;
    }

    /**
     * 查询文件，支持 图片，文本，音频，视频
     */
    @Override
    public <T extends BaseRequest> FileResponse query(Context context, T baseRequest) {
        Uri uri = uriMap.get(baseRequest.getType());
        ContentValues contentValues = objectConvertValues(baseRequest);
        Condition condition = new Condition(contentValues);
        String[] projection = new String[]{MediaStore.Images.Media._ID};
        if (uri != null) {
            Cursor cursor = context.getContentResolver().query(uri, projection, condition.whereCause, condition.whereArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                Uri queryUri = ContentUris.withAppendedId(uri, cursor.getLong(0));
                cursor.close();
                return new FileResponse(true, queryUri, baseRequest.getFile());
            }
        }
        return new FileResponse(false, null, null);
    }

    /**
     * 将 request 参数转换为 ContentValues
     */
    private <T extends BaseRequest> ContentValues objectConvertValues(T baseRequest) {
        ContentValues contentValues = new ContentValues();
        Field[] declaredFields = baseRequest.getClass().getDeclaredFields();
        for (Field declaredField : declaredFields) {
            try {
                DbFiled annotation = declaredField.getAnnotation(DbFiled.class);
                if (annotation == null) {
                    continue;
                }
                String key = annotation.value();
                String declaredFieldName = declaredField.getName();
                char firstLetter = Character.toUpperCase(declaredFieldName.charAt(0));
                String theRest = declaredFieldName.substring(1);
                String methodName = "get" + firstLetter + theRest;
                Method method = baseRequest.getClass().getMethod(methodName);
                String value = (String) method.invoke(baseRequest);
                if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
                    Log.d("VeloceHostImpl", "key: " + key + ", value: " + value);
                    contentValues.put(key, value);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return contentValues;
    }

    /**
     *  查询条件构造器
     * */
    private static class Condition {

        /**
         *  查询条件
         * */
        String whereCause;
        /**
         *  查询条件对应的值
         * */
        String[] whereArgs;

        /**
         *  contentValues 解析，参数构造
         * */
        Condition(ContentValues contentValues) {
            if (contentValues == null) {
                return;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("1 = 1"); // 防止前面拼错，where 也可以省略
            Iterator<Map.Entry<String, Object>> iterator = contentValues.valueSet().iterator();
            ArrayList<String> arrayList = new ArrayList<>();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> next = iterator.next();
                String key = next.getKey();
                String value = (String) next.getValue();
                if (value != null && key != null) {
                    stringBuilder.append(" and ").append(key).append(" = ?");
                    arrayList.add(value);
                }
            }
            whereCause = stringBuilder.toString();
            int size = arrayList.size();
            whereArgs = arrayList.toArray(new String[size]);
        }

    }

}
