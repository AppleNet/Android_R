package com.example.llc.storage.sanbox.storage;


import android.content.Context;

import com.example.llc.storage.sanbox.response.FileResponse;
import com.example.llc.storage.sanbox.request.base.BaseRequest;

public interface IFile {

    /**
     * 文件创建操作
     * @param <T> FileResponse
     * @param context context
     * @param baseRequest baseRequest
     * @param iFileListener iFileListener
     */
    <T extends BaseRequest> void createFile(Context context, T baseRequest, IFileListener iFileListener) throws Exception;

    /**
     * 文件下载操作
     * @param <T> T
     * @param context context
     * @param baseRequest baseRequest
     * @param iFileListener iFileListener
     * @throws Exception Exception
     */
    <T extends BaseRequest> void downLoadFile(Context context, T baseRequest, IFileListener iFileListener) throws Exception;

    /**
     * 文件删除操作
     * @param context context
     * @param baseRequest baseRequest
     * @param iFileListener iFileListener
     */
    <T extends BaseRequest> void deleteFile(Context context, T baseRequest, IFileListener iFileListener);

    /**
     * 重命名操作
     * @param context context
     * @param baseRequest baseRequest
     * @param subBaseRequest subBaseRequest
     * @param iFileListener iFileListener
     */
    <T extends BaseRequest> void updateFile(Context context, T baseRequest, T subBaseRequest, IFileListener iFileListener);

    /**
     * 文件复制操作
     * @param context context
     * @param baseRequest baseRequest
     * @param iFileListener iFileListener
     */
    <T extends BaseRequest> void copyFile(Context context, T baseRequest, IFileListener iFileListener) throws Exception;

    /**
     * 文件查询操作
     * @param context context
     * @param baseRequest baseRequest
     * @param iFileListener iFileListener
     */
    <T extends BaseRequest> void queryFile(Context context, T baseRequest, IFileListener iFileListener);
}
