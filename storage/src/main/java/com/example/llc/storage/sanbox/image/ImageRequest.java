package com.example.llc.storage.sanbox.image;

import android.os.Environment;
import android.provider.MediaStore;

import com.example.llc.storage.sanbox.BaseRequest;
import com.example.llc.storage.sanbox.annotation.DbFiled;

import java.io.File;

public class ImageRequest extends BaseRequest {

    @DbFiled(MediaStore.Images.ImageColumns.DISPLAY_NAME)
    private String displayName;
    @DbFiled(MediaStore.Images.ImageColumns.MIME_TYPE)
    private String mimeType;
    @DbFiled(MediaStore.Images.ImageColumns.RELATIVE_PATH)
    private String path;

    public ImageRequest(File file) {
        super(file);
        this.path = Environment.DIRECTORY_PICTURES + "/" + file.getName();
    }


    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getPath() {
        return Environment.DIRECTORY_PICTURES + "/" + path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
