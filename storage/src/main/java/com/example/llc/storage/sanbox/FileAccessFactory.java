package com.example.llc.storage.sanbox;

import android.os.Environment;
import com.example.llc.storage.sanbox.storage.impl.file.FileStoreImpl;
import com.example.llc.storage.sanbox.storage.impl.ms.MediaStoreAccessImpl;
import com.example.llc.storage.sanbox.request.base.BaseRequest;
import com.example.llc.storage.sanbox.storage.IFile;

public class FileAccessFactory {

    /**
     * 获取文件操作实例
     *
     * @param baseRequest baseRequest
     * @return 文件操作实例
     */
    public static IFile getIFile(BaseRequest baseRequest) {
        // android 11
        if (!Environment.isExternalStorageLegacy()) {
            setFileType(baseRequest);
            return MediaStoreAccessImpl.getInstance();
        }
        return FileStoreImpl.getInstance();
    }

    /**
     * 根据请求类型，创建不同文件目录类型
     * @param request request
     */
    public static void setFileType (BaseRequest request) {
        String absolutePath = request.getFile().getAbsolutePath();
        if (absolutePath.endsWith(".mp3")
                || absolutePath.endsWith(".wav")) {
            request.setType(MediaStoreAccessImpl.AUDIO);
        } else if (absolutePath.startsWith(MediaStoreAccessImpl.VIDEO) || absolutePath.endsWith(".mp4")
                || absolutePath.endsWith(".rmvb") || absolutePath.endsWith(".avi")) {
            request.setType(MediaStoreAccessImpl.VIDEO);
        } else if(absolutePath.startsWith(MediaStoreAccessImpl.IMAGE) || absolutePath.endsWith(".jpg")
                || absolutePath.endsWith(".jpeg") || absolutePath.endsWith(".png") || absolutePath.endsWith(".webp")) {
            request.setType(MediaStoreAccessImpl.IMAGE);
        } else if (absolutePath.endsWith(".txt") || absolutePath.endsWith(".pdf") || absolutePath.endsWith(".doc")) {
            request.setType(MediaStoreAccessImpl.DOCUMENTS);
        } else {
            request.setType(MediaStoreAccessImpl.DOWNLOADS);
        }
    }
}
