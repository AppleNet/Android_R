package com.example.llc.storage.sanbox.request.image;

import android.os.Environment;
import android.provider.MediaStore;

import com.example.llc.storage.sanbox.request.base.BaseRequest;
import com.example.llc.storage.sanbox.annotation.DbFiled;

import java.io.File;

/**
 * com.example.llc.storage.sanbox.request.image.ImageRequest
 * @author liulongchao
 * @since 2022/04/18
 *
 * 图片操作请求类
 */
public class ImageRequest extends BaseRequest {

    /**
     * MS创建文件使用的文件名
     */
    @DbFiled(MediaStore.Images.ImageColumns.DISPLAY_NAME)
    private String mDisplayName;
    /**
     * MS创建图片使用的类型
     */
    @DbFiled(MediaStore.Images.ImageColumns.MIME_TYPE)
    private String mMimeType = "image/jpeg";
    /**
     * MS创建文件使用的文件夹名
     */
    @DbFiled(MediaStore.Images.ImageColumns.RELATIVE_PATH)
    private String mPath;
    /**
     * 查询条件key
     */
    private String whereCause;
    /**
     * 查询条件值
     */
    private String[] whereArgs;

    public ImageRequest(File file) {
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

    public String getMimeType() {
        return mMimeType;
    }

    private void setMimeType(String mimeType) {
        this.mMimeType = mimeType;
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
            setParentPath(Environment.getExternalStorageDirectory() + mPackageName + path);
        }
    }

    public static class Builder {

        private String displayName;
        private String path;
        private String mimeType;
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

        public Builder setMimeType(String mimeType) {
            this.mimeType = mimeType;
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

        public ImageRequest build(File file) {
            ImageRequest imageRequest = new ImageRequest(file);
            imageRequest.setDisplayName(displayName);
            imageRequest.setPath(path);
            imageRequest.setMimeType(mimeType);
            imageRequest.setWhereCause(whereCause);
            imageRequest.setWhereArgs(whereArgs);
            return imageRequest;
        }
    }
}
