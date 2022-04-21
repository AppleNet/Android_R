package com.example.llc.storage.sanbox.response;

import android.content.Intent;
import android.net.Uri;

import java.io.File;

public class FileResponse {

    /**
     * 文件操作是否成功
     */
    private boolean isSuccess;
    /**
     * 是否文件夹
     */
    private boolean isDirectory;
    /**
     * 文件操作成功对应的文件 uri
     */
    private Uri uri;
    /**
     * 文件操作成功对应的文件
     */
    private File file;
    /**
     * SAF Intent
     */
    private Intent intent;

    public FileResponse(boolean isSuccess, Uri uri, File file) {
        this.isSuccess = isSuccess;
        this.uri = uri;
        this.file = file;
    }

    public FileResponse(boolean isSuccess, Uri uri, File file, boolean isDirectory) {
        this.isSuccess = isSuccess;
        this.uri = uri;
        this.file = file;
        this.isDirectory = isDirectory;
    }

    public FileResponse(Intent intent) {
        this.intent = intent;
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

    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public void setDirectory(boolean directory) {
        isDirectory = directory;
    }
}
