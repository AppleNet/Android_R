package com.example.llc.storage.sanbox.impl;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import com.example.llc.storage.sanbox.BaseRequest;
import com.example.llc.storage.sanbox.FileResponse;
import com.example.llc.storage.sanbox.IFile;
import com.example.llc.storage.sanbox.annotation.DbFiled;
import com.example.llc.storage.sanbox.file.FileRequest;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

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
        if (baseRequest instanceof FileRequest) {
            FileRequest fileRequest = (FileRequest) baseRequest;
            boolean flag = false;
            File file;
            if (TextUtils.isEmpty(fileRequest.getDisplayName())) {
                file = new File(fileRequest.getPath());
                if (!file.exists() && file.isDirectory()) {
                    flag = file.mkdirs();
                }
            } else {
                file = new File(fileRequest.getPath() + "/" + fileRequest.getDisplayName());
                if (!file.exists() && !file.isDirectory()) {
                    try {
                        flag = file.createNewFile();
                    } catch (IOException e) {
                        //
                    }
                }
            }
            return new FileResponse(flag, Uri.parse(file.getAbsolutePath()), file);
        }
        return null;
    }

    @Override
    public <T extends BaseRequest> FileResponse delete(Context context, T baseRequest) {
        return null;
    }

    @Override
    public <T extends BaseRequest> FileResponse renameTo(Context context, T baseRequest, T subBaseRequest) {
        return null;
    }

    @Override
    public <T extends BaseRequest> FileResponse copyFile(Context context, T baseRequest) {
        return null;
    }

    @Override
    public <T extends BaseRequest> FileResponse query(Context context, T baseRequest) {
        return null;
    }

}
