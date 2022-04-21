package com.example.llc.storage.sanbox.storage.impl.ms;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.llc.storage.sanbox.FileAccessFactory;
import com.example.llc.storage.sanbox.request.base.BaseRequest;
import com.example.llc.storage.sanbox.request.download.DownloadRequest;
import com.example.llc.storage.sanbox.request.image.ImageRequest;
import com.example.llc.storage.sanbox.response.FileResponse;
import com.example.llc.storage.sanbox.request.coy.CopyRequest;
import com.example.llc.storage.sanbox.request.file.FileRequest;
import com.example.llc.storage.sanbox.storage.IFile;
import com.example.llc.storage.sanbox.annotation.DbFiled;
import com.example.llc.storage.sanbox.storage.IFileListener;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * com.example.llc.storage.sanbox.impl.MediaStoreAccessImpl
 * @author liulongchao
 * @since 2021-03-04
 *
 * 可共享的MediaStore Api 来进行文件的存储访问
 *   在 Android 10（API 级别 29）或更高版本中，访问其他应用的文件需要 READ_EXTERNAL_STORAGE 或 WRITE_EXTERNAL_STORAGE 权限
 *   在 Android 9（API 级别 28）或更低版本中，访问所有文件均需要相关权限
 */
public class MediaStoreAccessImpl implements IFile {

    // uri
    public static final String AUDIO = "Audio"; // 音频
    public static final String VIDEO = "Video"; // 视频
    public static final String IMAGE = "Image"; // 图片
    public static final String DOCUMENTS = "Documents"; // txt，doc，pdf 非媒体文件，需要使用 SAF 框架来访问
    public static final String DOWNLOADS = "Downloads"; // 什么都能放

    // 外置卡的 uri 放到 hashMap 中
    private final HashMap<String, Uri> mUriMap = new HashMap<>();

    /**
     * 构造方法，将类型存入 uriMap 中
     */
    private MediaStoreAccessImpl() {
        mUriMap.put(AUDIO, MediaStore.Audio.Media.getContentUri("external"));
        mUriMap.put(VIDEO, MediaStore.Video.Media.getContentUri("external"));
        mUriMap.put(IMAGE, MediaStore.Images.Media.getContentUri("external"));
        mUriMap.put(DOCUMENTS, MediaStore.Files.getContentUri("external"));
        mUriMap.put(DOWNLOADS, MediaStore.Downloads.getContentUri("external"));
    }

    /**
     * Holder 单例类
     */
    private static final class Holder {
        private static final MediaStoreAccessImpl instance = new MediaStoreAccessImpl();
    }

    /**
     * 对外暴漏获取 MediaStoreAccessImpl 实例接口
     * @return MediaStoreAccessImpl
     */
    public static MediaStoreAccessImpl getInstance() {
        return Holder.instance;
    }

    /**
     * 创建文件，支持 图片，文本，音频，视频
     */
    @Override
    public <T extends BaseRequest> void createFile(final Context context, final T baseRequest, final IFileListener iFileListener) throws IllegalAccessException {
        if (!(baseRequest instanceof FileRequest) && !(baseRequest instanceof ImageRequest)) {
            throw new IllegalAccessException("Operate file please use FileRequest/ImageRequest");
        }
        Observable.just(baseRequest)
                .subscribeOn(Schedulers.io())
                .flatMap(new Function<BaseRequest, ObservableSource<BaseRequest>>() {
                    @Override
                    public ObservableSource<BaseRequest> apply(BaseRequest baseRequest) {
                        return Observable.just(baseRequest);
                    }
                })
                .map(new Function<BaseRequest, FileResponse>() {
                    @Override
                    public FileResponse apply(BaseRequest fileRequest) {
                        Uri requestUri = mUriMap.get(fileRequest.getType());
                        ContentValues contentValues = objectConvertValues(fileRequest);
                        ContentResolver contentResolver = context.getContentResolver();
                        if (requestUri != null) {
                            Uri responseUri = contentResolver.insert(requestUri, contentValues);
                            if (responseUri != null) {
                                return new FileResponse(true, responseUri, fileRequest.getFile());
                            }
                            return  new FileResponse(false, null, fileRequest.getFile());
                        }
                        return new FileResponse(false, null, fileRequest.getFile());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<FileResponse>() {
                    @Override
                    public void accept(FileResponse fileResponse) {
                        if (iFileListener != null) {
                            iFileListener.onFileResponse(fileResponse);
                        }
                    }
                });
    }

    /**
     * 文件下载操作
     * @param <T> T
     * @param context     context
     * @param baseRequest baseRequest
     * @param iFileListener iFileListener
     * @throws Exception Exception
     */
    @Override
    public <T extends BaseRequest> void downLoadFile(final Context context, final T baseRequest, final IFileListener iFileListener) throws Exception {
        if (!(baseRequest instanceof DownloadRequest)) {
            throw new IllegalAccessException("Download File user DownloadRequest!");
        }
        Toast.makeText(context, "开始下载...", Toast.LENGTH_LONG).show();
        Observable.just(baseRequest)
                .subscribeOn(Schedulers.io())
                .flatMap(new Function<BaseRequest, ObservableSource<DownloadRequest>>() {
                    @Override
                    public ObservableSource<DownloadRequest> apply(BaseRequest baseRequest) {
                        return Observable.just((DownloadRequest) baseRequest);
                    }
                })
                .map(new Function<DownloadRequest, FileResponse>() {
                    @Override
                    public FileResponse apply(DownloadRequest downloadRequest) {
                        try {
                            URL url = new URL(downloadRequest.getDownLoadUrl());
                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                            connection.setRequestMethod("GET");
                            InputStream inputStream = connection.getInputStream();
                            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                            Uri requestUri = mUriMap.get(baseRequest.getType());
                            ContentResolver contentResolver = context.getContentResolver();
                            ContentValues contentValues = new ContentValues();
                            contentValues.put(MediaStore.Downloads.RELATIVE_PATH, downloadRequest.getPath());
                            contentValues.put(MediaStore.Downloads.DISPLAY_NAME, downloadRequest.getDisplayName());
                            contentValues.put(MediaStore.Downloads.TITLE, downloadRequest.getTitle());
                            final Uri insertUri = contentResolver.insert(requestUri, contentValues);
                            if (insertUri != null) {
                                OutputStream outputStream = contentResolver.openOutputStream(insertUri);
                                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
                                byte[] buffer = new byte[1024];
                                int bytes = bufferedInputStream.read(buffer);
                                while (bytes > 0) {
                                    bufferedOutputStream.write(buffer, 0, bytes);
                                    bufferedOutputStream.flush();
                                    bytes = bufferedInputStream.read(buffer);
                                    Log.d("SwanHostImpl", "currentSize: " + bytes);
                                }
                                bufferedOutputStream.close();
                                bufferedInputStream.close();
                                return new FileResponse(true, insertUri, baseRequest.getFile());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return new FileResponse(false, null, null);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<FileResponse>() {
                    @Override
                    public void accept(FileResponse fileResponse) {
                        Toast.makeText(context, "下载完成...", Toast.LENGTH_LONG).show();
                        if (iFileListener != null) {
                            iFileListener.onFileResponse(fileResponse);
                        }
                    }
                });
    }

    /**
     * 删除文件，支持 图片，文本，音频，视频
     * @param context context
     * @param baseRequest request
     * @param iFileListener iFileListener
     */
    @Override
    public <T extends BaseRequest> void deleteFile(final Context context, final T baseRequest, final IFileListener iFileListener) {
        if (DOCUMENTS.equals(baseRequest.getType())) {
            // 使用 SAF 查询
            // 通过系统的文件浏览器选择一个文件
            openSAFArchitecture((FileRequest) baseRequest, iFileListener);
            return;
        }
        queryFile(context, baseRequest, new IFileListener() {
            @Override
            public void onFileResponse(FileResponse fileResponse) {
                if (fileResponse.isSuccess()) {
                    Observable<T> justBaseRequest = Observable.just(baseRequest);
                    Observable<FileResponse> justFileResponse = Observable.just(fileResponse);
                    Observable.zip(justBaseRequest, justFileResponse, new BiFunction<T, FileResponse, FileResponse>() {
                        @Override
                        public FileResponse apply(T baseRequest, FileResponse fileResponse) {
                            if (fileResponse.isSuccess()) {
                                int delete = context.getContentResolver().delete(fileResponse.getUri(), null, null);
                                if (delete > 0) {
                                    return new FileResponse(true, fileResponse.getUri(), baseRequest.getFile());
                                }
                            }
                            return new FileResponse(false, null, null);
                        }
                    })
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<FileResponse>() {
                                @Override
                                public void accept(FileResponse fileResponse) {
                                    if (iFileListener != null) {
                                        iFileListener.onFileResponse(fileResponse);
                                    }
                                }
                            });
                }
            }
        });

    }

    /**
     * 重命名文件，支持 图片，文本，音频，视频
     */
    @Override
    public <T extends BaseRequest> void updateFile(final Context context, T baseRequest, final T subBaseRequest, final IFileListener iFileListener) {
        if (DOCUMENTS.equals(baseRequest.getType())) {
            // 使用 SAF 查询
            // 通过系统的文件浏览器选择一个文件
            openSAFArchitecture((FileRequest) baseRequest, iFileListener);
            return;
        }

        queryFile(context, baseRequest, new IFileListener() {
            @Override
            public void onFileResponse(FileResponse fileResponse) {
                Observable<T> justSubBaseRequest = Observable.just(subBaseRequest);
                Observable<FileResponse> justFileResponse = Observable.just(fileResponse);
                Observable.zip(justFileResponse, justSubBaseRequest, new BiFunction<FileResponse, T, FileResponse>() {
                    @Override
                    public FileResponse apply(FileResponse fileResponse, T subBaseRequest) {
                        if (fileResponse.isSuccess()) {
                            ContentValues contentValues = objectConvertValues(subBaseRequest);
                            int update = context.getContentResolver().update(fileResponse.getUri(), contentValues, null, null);
                            if (update > 0) {
                                return new FileResponse(true, fileResponse.getUri(), subBaseRequest.getFile());
                            }
                        }
                        return new FileResponse(false, null, null);
                    }
                })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<FileResponse>() {
                            @Override
                            public void accept(FileResponse fileResponse) {
                                if (iFileListener != null) {
                                    iFileListener.onFileResponse(fileResponse);
                                }
                            }
                        });
            }
        });
    }

    /**
     * 复制文件，支持 图片，文本，音频，视频
     * @param context context
     * @param baseRequest baseRequest
     * @param iFileListener iFileListener
     */
    @Override
    public <T extends BaseRequest> void copyFile(final Context context, T baseRequest, final IFileListener iFileListener) throws IllegalAccessException {
        if (!(baseRequest instanceof CopyRequest)) {
            throw new IllegalAccessException("Operate copy file please use CopyRequest!");
        }
        final Uri[] srcUri = new Uri[1];
        Observable.just(baseRequest)
                .map(new Function<T, CopyRequest>() {
                    @Override
                    public CopyRequest apply(T baseRequest) {
                        return (CopyRequest) baseRequest;
                    }
                })
                .map(new Function<CopyRequest, CopyRequest>() {
                    @Override
                    public CopyRequest apply(CopyRequest copyRequest) {
                        // TODO 查询源文件 存在不存在
                        Uri requestUri = mUriMap.get(copyRequest.getType());
                        ContentValues contentValues = objectConvertValues(copyRequest);
                        Condition condition = new Condition(contentValues);
                        String[] projection = new String[]{ MediaStore.Images.Media._ID };
                        if (requestUri != null) {
                            Cursor cursor = context.getContentResolver()
                                    .query(requestUri, projection, condition.whereCause, condition.whereArgs, null);
                            if (cursor != null && cursor.moveToFirst()) {
                                // 源文件的 uri
                                srcUri[0] = ContentUris.withAppendedId(requestUri, cursor.getLong(0));
                                if (srcUri[0] != null) {
                                    // 源文件存在
                                    return copyRequest;
                                }
                                cursor.close();
                            }
                        }
                        return null;
                    }
                })
                .map(new Function<CopyRequest, FileRequest>() {
                    @Override
                    public FileRequest apply(CopyRequest copyRequest) {
                        if (copyRequest != null) {
                            // TODO 查询目标文件 存在不存在，如果存在，删除，不存在直接返回
                            FileRequest fileRequest = new FileRequest.Builder()
                                    .setDisplayName(copyRequest.getDistFile().getName())
                                    .setPath(copyRequest.getDistFile().getAbsolutePath().substring(1))
                                    .build(copyRequest.getDistFile());
                            FileAccessFactory.setFileType(fileRequest);
                            Uri requestUri = mUriMap.get(fileRequest.getType());
                            ContentValues contentValues = objectConvertValues(fileRequest);
                            Condition condition = new Condition(contentValues);
                            String[] projection = new String[]{ MediaStore.Images.Media._ID };
                            if (requestUri != null) {
                                Cursor cursor = context.getContentResolver()
                                        .query(requestUri, projection, condition.whereCause, condition.whereArgs, null);
                                if (cursor != null && cursor.moveToFirst()) {
                                    Uri responseUri = ContentUris.withAppendedId(requestUri, cursor.getLong(0));
                                    if (responseUri != null) {
                                        // 目标文件存在，删除
                                        int delete = context.getContentResolver().delete(responseUri, null, null);
                                        // 目标文件删除成功
                                        if (delete > 0) {
                                            return fileRequest;
                                        }
                                    }
                                    cursor.close();
                                }
                            }
                            return fileRequest;
                        }
                        return null;
                    }
                })
                .map(new Function<FileRequest, FileResponse>() {
                    @Override
                    public FileResponse apply(FileRequest fileRequest) {
                        // TODO 创建目标文件
                        if (fileRequest != null) {
                            Uri requestUri = mUriMap.get(fileRequest.getType());
                            ContentValues contentValues = objectConvertValues(fileRequest);
                            ContentResolver contentResolver = context.getContentResolver();
                            if (requestUri != null) {
                                Uri responseUri = contentResolver.insert(requestUri, contentValues);
                                if (responseUri != null) {
                                    return new FileResponse(true, responseUri, fileRequest.getFile());
                                }
                                return  new FileResponse(false, null, fileRequest.getFile());
                            }
                        }
                        return new FileResponse(false, null, null);
                    }
                })
                .map(new Function<FileResponse, FileResponse>() {
                    @Override
                    public FileResponse apply(FileResponse fileResponse) throws Exception {
                        // TODO 将源文件内容 复制 到目标文件内
                        Uri destUri = fileResponse.getUri();
                        OutputStream outputStream = context.getContentResolver().openOutputStream(destUri);
                        InputStream inputStream = context.getContentResolver().openInputStream(srcUri[0]);
                        BufferedOutputStream fileOutputStream = new BufferedOutputStream(outputStream);
                        BufferedInputStream fileInputStream = new BufferedInputStream(inputStream);
                        byte[] buff = new byte[1024];
                        while (fileInputStream.read(buff) != -1) {
                            fileOutputStream.write(buff);
                        }
                        fileInputStream.close();
                        fileOutputStream.close();
                        return new FileResponse(true, destUri, fileResponse.getFile());
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<FileResponse>() {
                    @Override
                    public void accept(FileResponse fileResponse) {
                        if (iFileListener != null) {
                            iFileListener.onFileResponse(fileResponse);
                        }
                    }
                });
    }

    /**
     * 查询文件，支持 图片，文本，音频，视频
     * @param context context
     * @param baseRequest baseRequest
     * @param iFileListener iFileListener
     */
    @Override
    public <T extends BaseRequest> void queryFile(final Context context, final T baseRequest, final IFileListener iFileListener) {
        if (DOCUMENTS.equals(baseRequest.getType())) {
            // 使用 SAF 查询
            // 通过系统的文件浏览器选择一个文件
            openSAFArchitecture((FileRequest) baseRequest, iFileListener);
            return;
        }
        Observable.just(baseRequest)
                .map(new Function<T, FileResponse>() {
                    @Override
                    public FileResponse apply(T baseRequest) {
                        String requestType = baseRequest.getType();
                        Uri requestUri = mUriMap.get(requestType);
                        ContentValues contentValues = objectConvertValues(baseRequest);
                        Condition condition = new Condition(contentValues);
                        String[] projection = new String[]{ MediaStore.Images.Media._ID };
                        if (requestUri != null) {

                            Cursor cursor = context.getContentResolver()
                                    .query(requestUri, projection, condition.whereCause, condition.whereArgs, "");
                            if (cursor != null && cursor.moveToFirst()) {
                                Uri responseUri = ContentUris.withAppendedId(requestUri, cursor.getLong(0));
                                if (responseUri != null) {
                                    return new FileResponse(true, responseUri, baseRequest.getFile());
                                }
                                cursor.close();
                            }
                        }
                        return new FileResponse(false, null, baseRequest.getFile());
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<FileResponse>() {
                    @Override
                    public void accept(FileResponse fileResponse) {
                        if (iFileListener != null) {
                            iFileListener.onFileResponse(fileResponse);
                        }
                    }
                });
    }

    /**
     * 使用 SAF 进行相关操作
     * @param baseRequest baseRequest
     * @param iFileListener iFileListener
     * @param <T> BaseRequest
     */
    private <T extends BaseRequest> void openSAFArchitecture(FileRequest baseRequest, IFileListener iFileListener) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        // 筛选，只显示可以“打开”的结果，如文件(而不是联系人或时区列表)
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        // 文件名称
        intent.putExtra(Intent.EXTRA_TITLE, baseRequest.getDisplayName());
        if (iFileListener != null) {
            iFileListener.onFileResponse(new FileResponse(intent));
        }
    }

    /**
     * 将 request 参数转换为 ContentValues
     * @param baseRequest baseRequest
     * @return ContentValues
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
                // 获取注解上的值，例如 MediaStore.Files.FileColumns.DISPLAY_NAME
                String key = annotation.value();
                // mDisplayName
                String declaredFieldName = declaredField.getName();
                char firstLetter = Character.toUpperCase(declaredFieldName.charAt(1));
                String theRest = declaredFieldName.substring(2);
                // 获取 getDisPlayName 方法名
                String methodName = "get" + firstLetter + theRest;
                Method method = baseRequest.getClass().getMethod(methodName);
                // 获取 getDisplayName 的返回值
                String value = (String) method.invoke(baseRequest);
                if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
                    Log.d("SwanHostImpl", "key: " + key + ", value: " + value);
                    contentValues.put(key, value);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return contentValues;
    }
}
