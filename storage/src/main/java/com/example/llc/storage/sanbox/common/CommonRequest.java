package com.example.llc.storage.sanbox.common;

import android.os.Environment;
import android.text.TextUtils;
import com.example.llc.storage.sanbox.BaseRequest;
import java.io.File;

public class CommonRequest extends BaseRequest {

    private String displayName;

    private String path;

    private String title;

    public CommonRequest(File file) {
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
        if (!TextUtils.isEmpty(path)) {
            return Environment.getExternalStorageDirectory() + "/" + path + "/";
        }
        return Environment.getExternalStorageDirectory() + "/";
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
