package com.example.llc.storage.sanbox;

import android.net.Uri;

import java.io.File;

public class FileResponse {

    private boolean isSuccess;
    private Uri uri;
    private File file;

    public FileResponse(boolean isSuccess, Uri uri, File file) {
        this.isSuccess = isSuccess;
        this.uri = uri;
        this.file = file;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
