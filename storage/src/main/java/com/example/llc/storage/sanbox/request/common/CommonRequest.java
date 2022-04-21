package com.example.llc.storage.sanbox.request.common;

import android.os.Environment;
import com.example.llc.storage.sanbox.request.base.BaseRequest;
import java.io.File;

/**
 * com.example.llc.storage.sanbox.request.common.CommonRequest
 *
 * @author liulongchao
 * @since 2022/04/18
 *
 * 文件请求通用类
 */
public class CommonRequest extends BaseRequest {

    /**
     * 文件名字
     */
    private String mDisplayName;
    /**
     * 文件相对路径
     */
    private String mPath;
    /**
     * 文件title，其实就是文件名字
     */
    private String mTitle;

    public CommonRequest(File file) {
        super(file);
    }

    public String getDisplayName() {
        return mDisplayName;
    }

    private void setDisplayName(String displayName) {
        this.mDisplayName = displayName;
    }

    public String getPath() {
        // 不是兼容模式
        if (!Environment.isExternalStorageLegacy()) {
            return mPath;
        }
        return getParentPath();
    }

    private void setPath(String path) {
        if (!Environment.isExternalStorageLegacy()) {
            // 不是兼容模式，强制使用分区存储
            this.mPath = path;
        } else {
            // 普通模式
            setParentPath(Environment.getExternalStorageDirectory() + mPackageName);
        }
    }

    public String getTitle() {
        return mTitle;
    }

    private void setTitle(String title) {
        this.mTitle = title;
    }

    public static class Builder {

        private String displayName;
        private String path;
        private String title;

        public Builder setDisplayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        public Builder setPath(String path) {
            this.path = path;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public CommonRequest build(File file) {
            CommonRequest commonRequest = new CommonRequest(file);
            commonRequest.setDisplayName(displayName);
            commonRequest.setPath(path);
            commonRequest.setTitle(title);
            return commonRequest;
        }
    }
}
