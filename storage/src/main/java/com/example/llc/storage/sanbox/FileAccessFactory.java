package com.example.llc.storage.sanbox;

import android.os.Build;
import android.os.Environment;

import com.example.llc.storage.sanbox.impl.FileStoreImpl;
import com.example.llc.storage.sanbox.impl.MediaStoreAccessImpl;

public class FileAccessFactory {

    public static IFile getIFile(BaseRequest baseRequest) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (!Environment.isExternalStorageLegacy()) {
                // android 11
                setFileType(baseRequest);
                return MediaStoreAccessImpl.getInstance();
            }
        }

        return FileStoreImpl.getInstance();
    }

    public static void setFileType (BaseRequest request) {

        String absolutePath = request.getFile().getAbsolutePath();

        if (absolutePath.endsWith(".mp3")
                || absolutePath.endsWith(".wav")) {
            request.setType(MediaStoreAccessImpl.AUDIO);
        } else if (absolutePath.startsWith(MediaStoreAccessImpl.VIDEO) || absolutePath.endsWith(".mp4")
                || absolutePath.endsWith(".rmvb") || absolutePath.endsWith(".avi")) {
            request.setType(MediaStoreAccessImpl.VIDEO);
        } else if(absolutePath.startsWith(MediaStoreAccessImpl.IMAGE) || absolutePath.endsWith(".jpg")
                || absolutePath.endsWith("jpeg") || absolutePath.endsWith(".png") || absolutePath.endsWith(".webp")) {
            request.setType(MediaStoreAccessImpl.IMAGE);
        } else {
            request.setType(MediaStoreAccessImpl.DOWNLOADS);
        }
    }
}
