package com.example.llc.storage.sanbox;

import java.io.File;

public class BaseRequest {

    // Android 11  中没有太大的作用  表示相对路径
    private File file;
    // type 文件类型
    private String type;

    public BaseRequest(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
