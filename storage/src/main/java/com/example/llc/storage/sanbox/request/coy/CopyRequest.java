package com.example.llc.storage.sanbox.request.coy;

import android.os.Environment;
import android.provider.MediaStore;

import com.example.llc.storage.sanbox.request.base.BaseRequest;
import com.example.llc.storage.sanbox.annotation.DbFiled;

import java.io.File;

/**
 * com.example.llc.storage.sanbox.request.coy.CopyRequest
 * @author liulongchao
 * @since 2022/04/18
 *
 * 文件操作 负责类
 */
public class CopyRequest extends BaseRequest {

    /**
     * 目标文件
     */
    private File mDistFile;
    /**
     * MS创建文件使用的文件名
     */
    @DbFiled(MediaStore.Downloads.DISPLAY_NAME)
    private String mDisplayName;
    /**
     * MS创建文件使用的相对路径
     */
    @DbFiled(MediaStore.Downloads.RELATIVE_PATH)
    private String mPath;
    /**
     * 文件title，其实就是文件名字
     */
    @DbFiled(MediaStore.Downloads.TITLE)
    private String mTitle;


    private CopyRequest(File file) {
        super(file);
    }

    public File getDistFile() {
        return mDistFile;
    }

    private void setDistFile(File distFile) {
        this.mDistFile = distFile;
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
        // 不是兼容模式
        if (!Environment.isExternalStorageLegacy()) {
            this.mPath = path;
        } else {
            setParentPath(Environment.getExternalStorageDirectory() + mPackageName);
        }
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public static class Builder {
        private File distFile;
        private String displayName;
        private String path;
        private String title;

        public Builder setDistFile(File file) {
            this.distFile = file;
            return this;
        }

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

        public CopyRequest build(File file) {
            CopyRequest copyRequest = new CopyRequest(file);
            copyRequest.setDisplayName(displayName);
            copyRequest.setDistFile(distFile);
            copyRequest.setPath(path);
            copyRequest.setTitle(title);
            return copyRequest;
        }
    }
}
