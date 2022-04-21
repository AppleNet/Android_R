package com.example.llc.storage.sanbox.storage;

import com.example.llc.storage.sanbox.response.FileResponse;

/**
 * com.example.llc.storage.sanbox.storage.IFileListener
 *
 * @author liulongchao
 * @since 2022/4/19
 */
public interface IFileListener {

    /**
     * @param fileResponse fileResponse
     */
    void onFileResponse(FileResponse fileResponse);
}
