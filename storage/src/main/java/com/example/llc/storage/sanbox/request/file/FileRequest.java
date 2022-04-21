package com.example.llc.storage.sanbox.request.file;

import android.os.Environment;
import android.provider.MediaStore;

import com.example.llc.storage.sanbox.request.base.BaseRequest;
import com.example.llc.storage.sanbox.annotation.DbFiled;
import com.example.llc.storage.sanbox.request.image.ImageRequest;

import java.io.File;

/**
 * com.example.llc.storage.sanbox.request.file.FileRequest
 *
 * @author liulongchao
 * @since 2022/04/18
 * 文件操作请求
 */
public class FileRequest extends BaseRequest {

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

    /**
     * 查询条件key
     */
    private String whereCause;
    /**
     * 查询条件值
     */
    private String[] whereArgs;

    /**
     * 创建 File 操作请求
     * @param file file
     */
    private FileRequest(File file) {
        super(file);
    }

    public String getWhereCause() {
        return whereCause;
    }

    private void setWhereCause(String whereCause) {
        this.whereCause = whereCause;
    }

    public String[] getWhereArgs() {
        return whereArgs;
    }

    private void setWhereArgs(String[] whereArgs) {
        this.whereArgs = whereArgs;
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

    public static class Builder {

        private String displayName;
        private String title;
        private String path;
        private String whereCause;
        private String[] whereArgs;

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

        public Builder setWhereCause(String whereCause) {
            this.whereCause = whereCause;
            return this;
        }

        public Builder setWhereArgs(String[] whereArgs) {
            this.whereArgs = whereArgs;
            return this;
        }

        public FileRequest build(File file) {
            FileRequest fileRequest = new FileRequest(file);
            fileRequest.setDisplayName(displayName);
            fileRequest.setPath(path);
            fileRequest.setTitle(title);
            fileRequest.setWhereCause(whereCause);
            fileRequest.setWhereArgs(whereArgs);
            return fileRequest;
        }
    }
}
