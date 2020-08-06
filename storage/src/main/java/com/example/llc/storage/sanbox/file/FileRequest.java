package com.example.llc.storage.sanbox.file;

import android.os.Environment;
import android.provider.MediaStore;

import com.example.llc.storage.sanbox.BaseRequest;
import com.example.llc.storage.sanbox.annotation.DbFiled;

import java.io.File;

public class FileRequest extends BaseRequest {

    @DbFiled(MediaStore.Downloads.DISPLAY_NAME)
    private String displayName;

    @DbFiled(MediaStore.Downloads.RELATIVE_PATH)
    private String path;

    @DbFiled(MediaStore.Downloads.TITLE)
    private String title;

    public FileRequest(File file) {
        super(file);
        this.path = file.getName();
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPath() {
        return Environment.DIRECTORY_DOWNLOADS + "/" + path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
