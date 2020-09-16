package com.example.llc.storage.sanbox.impl;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import com.example.llc.storage.sanbox.BaseRequest;
import com.example.llc.storage.sanbox.FileResponse;
import com.example.llc.storage.sanbox.IFile;
import com.example.llc.storage.sanbox.common.CommonRequest;
import com.example.llc.storage.sanbox.coy.CopyRequest;
import com.example.llc.storage.sanbox.file.FileRequest;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileStoreImpl implements IFile {

    private FileStoreImpl() {}

    private static final class Holder {
        private static final FileStoreImpl instance = new FileStoreImpl();
    }

    public static FileStoreImpl getInstance() {
        return Holder.instance;
    }

    /**
     *  创建文件或者文件夹，displayName 不存在，则创建文件夹，存在 则创建文件
     *
     * @param context context
     * @param baseRequest baseRequest
     * */
    @Override
    public <T extends BaseRequest> FileResponse newCreateFile(Context context, T baseRequest) {
        //
        if (baseRequest instanceof CommonRequest) {
            CommonRequest fileRequest = (CommonRequest) baseRequest;
            boolean flag = false;
            File file;
            if (TextUtils.isEmpty(fileRequest.getDisplayName())) {
                // 创建文件夹
                file = new File(fileRequest.getPath());
                if (file.exists()) {
                    flag = file.delete();
                }
                if (!file.exists() && !file.isDirectory()) {
                    flag = file.mkdirs();
                }
            } else {
                // 创建文件
                file = new File(fileRequest.getPath() + fileRequest.getDisplayName());
                if (file.exists()) {
                    flag = file.delete();
                }
                if (!file.exists() && !file.isDirectory()) {
                    try {
                        file = new File(fileRequest.getPath());
                        flag = file.mkdirs();
                        if (flag) {
                            file = new File(fileRequest.getPath() + fileRequest.getDisplayName());
                            flag = file.createNewFile();
                        }
                    } catch (IOException e) {
                        //
                    }
                }
            }
            return new FileResponse(flag, Uri.parse(file.getAbsolutePath()), file);
        }
        return new FileResponse(false, null, null);
    }

    /**
     *  删除文件
     *
     * @param context context
     * @param baseRequest baseRequest
     * */
    @Override
    public <T extends BaseRequest> FileResponse delete(Context context, T baseRequest) {
        if (baseRequest instanceof CommonRequest) {
            CommonRequest fileRequest = (CommonRequest) baseRequest;
            boolean flag = false;
            File file;
            if (!TextUtils.isEmpty(fileRequest.getDisplayName())) {
                file = new File(fileRequest.getPath() + fileRequest.getDisplayName());
                if (file.exists() || file.isDirectory()) {
                    flag = file.delete();
                }
            } else {
                file = new File(fileRequest.getPath());
            }
            return new FileResponse(flag, Uri.parse(file.getAbsolutePath()), file);
        }
        return new FileResponse(false, null, null);
    }

    @Override
    public <T extends BaseRequest> FileResponse renameTo(Context context, T baseRequest, T subBaseRequest) {

        return null;
    }

    /**
     *  复制文件
     *
     * */
    @Override
    public <T extends BaseRequest> FileResponse copyFile(Context context, T baseRequest) {
        if (baseRequest instanceof CopyRequest) {
            CopyRequest copyRequest = (CopyRequest) baseRequest;
            FileResponse fileResponse = query(context, baseRequest);
            if (!fileResponse.isSuccess()) {
                // 源文件不存在
                 return new FileResponse(false, null, null);
            }
            //
            FileRequest fileRequest = new FileRequest(copyRequest.getDistFile().getParentFile());
            fileRequest.setDisplayName(copyRequest.getDistFile().getName());
            fileResponse = query(context, fileRequest);
            if (fileResponse.isSuccess()) {
                delete(context, fileRequest);
            } else {
                Uri destUri = newCreateFile(context, fileRequest).getUri();
                if (fileResponse.isSuccess()) {
                    OutputStream outputStream;
                    InputStream inputStream;
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
            }
        }
        return null;
    }

    @Override
    public <T extends BaseRequest> FileResponse query(Context context, T baseRequest) {
        String path = baseRequest.getParentPath();
        File file = new File(path);
        if (file.exists()) {
            return new FileResponse(true, Uri.parse(path), file);
        }
        return new FileResponse(false, null, null);
    }

}
