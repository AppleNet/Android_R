package com.example.llc.storage.sanbox.impl;

import android.content.Context;

import com.example.llc.storage.sanbox.BaseRequest;
import com.example.llc.storage.sanbox.FileResponse;
import com.example.llc.storage.sanbox.IFile;

public class FileStoreImpl implements IFile {

    private FileStoreImpl() {}

    private static final class Holder {
        private static final FileStoreImpl instance = new FileStoreImpl();
    }

    public static FileStoreImpl getInstance() {
        return Holder.instance;
    }

    @Override
    public <T extends BaseRequest> FileResponse newCreateFile(Context context, T baseRequest) {
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
