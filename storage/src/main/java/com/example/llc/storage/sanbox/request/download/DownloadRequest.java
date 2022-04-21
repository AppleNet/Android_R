package com.example.llc.storage.sanbox.request.download;

import android.os.Environment;
import android.provider.MediaStore;

import com.example.llc.storage.sanbox.annotation.DbFiled;
import com.example.llc.storage.sanbox.request.base.BaseRequest;
import com.example.llc.storage.sanbox.request.file.FileRequest;

import java.io.File;

/**
 * com.example.llc.storage.sanbox.request.download.DownloadRequest
 *
 * @author liulongchao
 * @since 2022/4/18
 */
public class DownloadRequest extends BaseRequest {

    /**
     * MS创建文件使用的文件名
     */
    @DbFiled(MediaStore.Files.FileColumns.DISPLAY_NAME)
    private String mDisplayName;
    /**
     * MS创建文件使用的相对路径
     */
    @DbFiled(MediaStore.Files.FileColumns.RELATIVE_PATH)
    private String mPath;
    /**
     * MS创建文件使用的文件名，可写可不写
     */
    @DbFiled(MediaStore.Files.FileColumns.TITLE)
    private String mTitle;

    private String downLoadUrl;

    /**
     * 创建 File 操作请求
     * @param file file
     */
    public DownloadRequest(File file) {
        super(file);
    }

    public String getDisplayName() {
        return mDisplayName;
    }

    private void setDisplayName(String displayName) {
        this.mDisplayName = displayName;
    }

    /**
     * 获取文件绝对路径
     *
     * @return 分区存储：sdcard/Downloads/${mPath}
     * 非分区存储：sdcard/${mPath}
     */
    public String getPath() {
        // 不是兼容模式
        if (!Environment.isExternalStorageLegacy()) {
            return mPath;
        }
        return getParentPath();
    }

    /**
     * 设置文件相对路径
     * @param path path
     */
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

    private void setTitle(String title) {
        this.mTitle = title;
    }

    public String getDownLoadUrl() {
        return downLoadUrl;
    }

    private void setDownLoadUrl(String downLoadUrl) {
        this.downLoadUrl = downLoadUrl;
    }

    /**
     * 建造者模式 构建请求实体类
     */
    public static class Builder {
        private String displayName;
        private String title;
        private String path;
        private String downLoadUrl;

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

        public Builder setDownLoadUrl(String downLoadUrl) {
            this.downLoadUrl = downLoadUrl;
            return this;
        }

        public DownloadRequest build(File file) {
            DownloadRequest downloadRequest = new DownloadRequest(file);
            downloadRequest.setDisplayName(displayName);
            downloadRequest.setPath(path);
            downloadRequest.setTitle(title);
            downloadRequest.setDownLoadUrl(downLoadUrl);
            return downloadRequest;
        }
    }
}
