package com.example.llc.storage.sanbox.image;

import android.os.Build;
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (!Environment.isExternalStorageLegacy()) {
                this.path = Environment.DIRECTORY_PICTURES + "/" + file.getName();
            }

        }
        setParentPath(Environment.getExternalStorageDirectory() + "/" + displayName);
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (!Environment.isExternalStorageLegacy()) {
                // 不是兼容模式
                return Environment.DIRECTORY_PICTURES + "/" + path;
            }
        }
        return Environment.getExternalStorageDirectory() + "/" + path;
    }

    public void setPath(String path) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (!Environment.isExternalStorageLegacy()) {
                this.path = path;
            }
        }
        setParentPath(Environment.getExternalStorageDirectory() + "/" + displayName);
    }
}
