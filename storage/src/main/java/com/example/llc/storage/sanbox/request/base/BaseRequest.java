package com.example.llc.storage.sanbox.request.base;

import java.io.File;

/**
 * com.example.llc.storage.sanbox.request.base.BaseRequest
 *
 * @author liulongchao
 * @since 2022/04/18
 *
 * 文件操作请求基类
 */
public class BaseRequest {

    /**
     * 包名
     */
    public String mPackageName = "/com.example.llc.android_r/";
    /**
     * Android 11 中没有太大的作用  表示相对路径
     */
    private File mFile;
    /**
     * type 文件类型
     */
    private String mType;
    /**
     * 文件路径
     */
    private String mParentPath;

    public BaseRequest(File file) {
        this.mFile = file;
    }

    public File getFile() {
        return mFile;
    }

    public void setFile(File file) {
        this.mFile = file;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        this.mType = type;
    }

    public String getParentPath() {
        return mParentPath;
    }

    public void setParentPath(String parentPath) {
        this.mParentPath = parentPath;
    }
}
