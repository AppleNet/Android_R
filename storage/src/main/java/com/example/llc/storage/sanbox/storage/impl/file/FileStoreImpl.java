package com.example.llc.storage.sanbox.storage.impl.file;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.llc.storage.sanbox.request.base.BaseRequest;
import com.example.llc.storage.sanbox.request.download.DownloadRequest;
import com.example.llc.storage.sanbox.request.image.ImageRequest;
import com.example.llc.storage.sanbox.response.FileResponse;
import com.example.llc.storage.sanbox.request.coy.CopyRequest;
import com.example.llc.storage.sanbox.request.file.FileRequest;
import com.example.llc.storage.sanbox.storage.IFile;
import com.example.llc.storage.sanbox.storage.IFileListener;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class FileStoreImpl implements IFile {

    /**
     * 私有构造，单例对外提供
     */
    private FileStoreImpl() {}

    /**
     * holder 类型单例
     */
    private static final class Holder {
        private static final FileStoreImpl instance = new FileStoreImpl();
    }

    /**
     * 对外暴漏 FileStoreImpl 获取接口
     * @return FileStoreImpl
     */
    public static FileStoreImpl getInstance() {
        return Holder.instance;
    }

    /**
     *  创建文件或者文件夹，displayName 不存在，则创建文件夹，存在 则创建文件
     *  @param context context
     * @param baseRequest baseRequest
     * @param iFileListener iFileListener
     * */
    @Override
    public <T extends BaseRequest> void createFile(Context context, final T baseRequest, final IFileListener iFileListener) throws IllegalAccessException {
        if (!(baseRequest instanceof FileRequest)) {
            throw new IllegalAccessException("Operate file please use FileRequest");
        }
        RxPermissions rxPermissions = new RxPermissions((Activity) context);
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) {
                        if (aBoolean) {
                            // 权限授予
                            Observable.just(baseRequest)
                                    .map(new Function<T, FileRequest>() {
                                        @Override
                                        public FileRequest apply(T baseRequest) {
                                            return (FileRequest) baseRequest;
                                        }
                                    })
                                    .map(new Function<FileRequest, FileResponse>() {
                                        @Override
                                        public FileResponse apply(FileRequest fileRequest) {
                                            FileResponse fileResponse;
                                            if (TextUtils.isEmpty(fileRequest.getDisplayName())) {
                                                // 文件名字为空，则创建文件夹
                                                File file = new File(fileRequest.getPath());
                                                if (file.exists()) {
                                                    file.delete();
                                                }
                                                boolean mkdirs = file.mkdirs();
                                                if (mkdirs) {
                                                    fileResponse = new FileResponse(true, Uri.parse(file.getAbsolutePath()), file, true);
                                                    return fileResponse;
                                                }
                                            } else {
                                                try {
                                                    // 文件名字不空，则创建文件
                                                    File file = new File(fileRequest.getPath(), fileRequest.getDisplayName());
                                                    if (file.exists()) {
                                                        file.delete();
                                                    }
                                                    if (!file.exists() && !file.isDirectory()) {
                                                        file = new File(fileRequest.getPath());
                                                        if (!file.exists()) {
                                                            file.mkdirs();
                                                        }
                                                        file = new File(fileRequest.getPath(), fileRequest.getDisplayName());
                                                        boolean newFile = file.createNewFile();
                                                        if (newFile) {
                                                            fileResponse = new FileResponse(true, Uri.parse(file.getAbsolutePath()), file, false);
                                                            return fileResponse;
                                                        }
                                                    }
                                                } catch (Exception e) {
                                                    Log.d("SwanHostImpl", "create file error: " + Log.getStackTraceString(e));
                                                    fileResponse = new FileResponse(false, null, null, false);
                                                    return fileResponse;
                                                }
                                            }
                                            fileResponse = new FileResponse(false, null, null, false);
                                            return fileResponse;
                                        }
                                    })
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Consumer<FileResponse>() {
                                        @Override
                                        public void accept(FileResponse fileResponse) {
                                            if (iFileListener != null) {
                                                iFileListener.onFileResponse(fileResponse);
                                            }
                                        }
                                    });
                        } else {
                            if (iFileListener != null) {
                                FileResponse fileResponse = new FileResponse(false, null, null, false);
                                iFileListener.onFileResponse(fileResponse);
                            }
                        }
                    }
                });
    }

    /**
     * 文件下载操作
     * @param <T> T
     * @param context     context
     * @param baseRequest baseRequest
     * @param iFileListener iFileListener
     * @throws Exception Exception
     */
    @Override
    public <T extends BaseRequest> void downLoadFile(final Context context, T baseRequest, final IFileListener iFileListener) throws Exception {
        if (!(baseRequest instanceof DownloadRequest)) {
            throw new IllegalAccessException("Download File user DownloadRequest!");
        }
        Toast.makeText(context, "开始下载...", Toast.LENGTH_LONG).show();
        Observable.just(baseRequest)
                .subscribeOn(Schedulers.io())
                .flatMap(new Function<BaseRequest, ObservableSource<DownloadRequest>>() {
                    @Override
                    public ObservableSource<DownloadRequest> apply(BaseRequest baseRequest) {
                        return Observable.just((DownloadRequest) baseRequest);
                    }
                })
                .map(new Function<DownloadRequest, FileResponse>() {
                    @Override
                    public FileResponse apply(DownloadRequest downloadRequest) {
                        try {
                            // 文件名字不空，则创建文件
                            File file = new File(downloadRequest.getPath(), downloadRequest.getDisplayName());
                            if (file.exists()) {
                                file.delete();
                            }
                            if (!file.exists() && !file.isDirectory()) {
                                file = new File(downloadRequest.getPath());
                                if (!file.exists()) {
                                    file.mkdirs();
                                }
                                file = new File(downloadRequest.getPath(), downloadRequest.getDisplayName());
                                boolean newFile = file.createNewFile();
                                if (newFile) {
                                    URL url = new URL(downloadRequest.getDownLoadUrl());
                                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                                    connection.setRequestMethod("GET");
                                    InputStream inputStream = connection.getInputStream();
                                    BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

                                    OutputStream outputStream = new FileOutputStream(file);
                                    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
                                    byte[] buffer = new byte[1024];
                                    int bytes = bufferedInputStream.read(buffer);
                                    while (bytes > 0) {
                                        bufferedOutputStream.write(buffer, 0, bytes);
                                        bufferedOutputStream.flush();
                                        bytes = bufferedInputStream.read(buffer);
                                        Log.d("SwanHostImpl", "currentSize: " + bytes);
                                    }
                                    bufferedOutputStream.close();
                                    bufferedInputStream.close();
                                    return new FileResponse(true, Uri.parse(file.getAbsolutePath()), file, false);
                                }
                            }
                        } catch (Exception e) {
                            Log.d("SwanHostImpl", "create file error: " + Log.getStackTraceString(e));
                            return new FileResponse(false, null, null, false);
                        }
                        return new FileResponse(false, null, null, false);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<FileResponse>() {
                    @Override
                    public void accept(FileResponse fileResponse) {
                        Toast.makeText(context, "下载完成...", Toast.LENGTH_LONG).show();
                        if (iFileListener != null) {
                            iFileListener.onFileResponse(fileResponse);
                        }
                    }
                });
    }

    /**
     * 删除文件
     * @param context context
     * @param baseRequest baseRequest
     * @param iFileListener iFileListener
     * */
    @Override
    public <T extends BaseRequest> void deleteFile(Context context, final T baseRequest, final IFileListener iFileListener) {
        Observable.zip(Observable.just(baseRequest), Observable.just(baseRequest), new BiFunction<T, T, FileResponse>() {
            @Override
            public FileResponse apply(T fileRequest, T imageRequest) {
                if (fileRequest instanceof FileRequest) {
                    FileRequest fr = (FileRequest) fileRequest;
                    String displayName = fr.getDisplayName();
                    if (!TextUtils.isEmpty(displayName)) {
                        File file = new File(fr.getParentPath(), displayName);
                        if (file.exists()) {
                            if (file.delete()) {
                                return new FileResponse(true, Uri.parse(file.getAbsolutePath()), file, false);
                            }
                        }
                    }
                    return new FileResponse(false, null, null, false);
                }
                if (imageRequest instanceof ImageRequest) {
                    ImageRequest ir = (ImageRequest) imageRequest;
                    String displayName = ir.getDisplayName();
                    if (!TextUtils.isEmpty(displayName)) {
                        File file = new File(ir.getParentPath(), displayName);
                        if (file.exists()) {
                            if (file.delete()) {
                                return new FileResponse(true, Uri.parse(file.getAbsolutePath()), file, false);
                            }
                        }
                    }
                    return new FileResponse(false, null, null, false);
                }
                return new FileResponse(false, null, null, false);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<FileResponse>() {
                    @Override
                    public void accept(FileResponse fileResponse) {
                        if (iFileListener != null) {
                            iFileListener.onFileResponse(fileResponse);
                        }
                    }
                });
    }

    /**
     * 更新文件
     * @param context        context
     * @param srcRequest    srcRequest
     * @param destRequest destRequest
     * @param iFileListener  iFileListener
     * @param <T> T
     */
    @Override
    public <T extends BaseRequest> void updateFile(Context context, T srcRequest, T destRequest, final IFileListener iFileListener) {
        Observable.zip(Observable.just(srcRequest), Observable.just(destRequest), new BiFunction<T, T, FileResponse>() {
            @Override
            public FileResponse apply(T srcRequest, T destRequest) {
                File srcFile = new File(srcRequest.getParentPath(), srcRequest.getFile().getName());
                File destFile = new File(destRequest.getParentPath(), destRequest.getFile().getName());
                boolean renameTo = srcFile.renameTo(destFile);
                if (renameTo) {
                    return new FileResponse(true, Uri.parse(destFile.getAbsolutePath()), destFile, false);
                }
                return new FileResponse(false, null, null, false);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<FileResponse>() {
                    @Override
                    public void accept(FileResponse fileResponse) {
                        if (iFileListener != null) {
                            iFileListener.onFileResponse(fileResponse);
                        }
                    }
                });
    }

    /**
     * 复制文件
     * @param context context
     * @param baseRequest request
     * @param iFileListener iFileListener
     * */
    @Override
    public <T extends BaseRequest> void copyFile(Context context, T baseRequest, final IFileListener iFileListener) throws IllegalAccessException {
        if (!(baseRequest instanceof CopyRequest)) {
            throw new IllegalAccessException("Operate copy file please use CopyRequest!");
        }
        final File[] srcFile = new File[1];
        Observable.just(baseRequest)
                .map(new Function<T, CopyRequest>() {
                    @Override
                    public CopyRequest apply(T baseRequest) {
                        return (CopyRequest) baseRequest;
                    }
                })
                .map(new Function<CopyRequest, CopyRequest>() {
                    @Override
                    public CopyRequest apply(CopyRequest copyRequest) {
                        File file = new File(copyRequest.getPath(), copyRequest.getDisplayName());
                        if (file.exists()) {
                            srcFile[0] = file;
                            return copyRequest;
                        }
                        return null;
                    }
                })
                .map(new Function<CopyRequest, FileRequest>() {
                    @Override
                    public FileRequest apply(CopyRequest copyRequest) {
                        if (copyRequest != null) {
                            FileRequest fileRequest = new FileRequest.Builder()
                                    .setDisplayName(copyRequest.getDistFile().getName())
                                    .setPath(copyRequest.getDistFile().getAbsolutePath())
                                    .build(copyRequest.getDistFile());
                            File file = new File(fileRequest.getParentPath(), fileRequest.getDisplayName());
                            if (file.exists()) {
                                boolean delete = file.delete();
                                if (delete) {
                                    return fileRequest;
                                }
                            }
                            return fileRequest;
                        }
                        return null;
                    }
                })
                .map(new Function<FileRequest, FileResponse>() {
                    @Override
                    public FileResponse apply(FileRequest fileRequest) {
                        if (fileRequest != null) {
                            try {
                                File file = new File(fileRequest.getParentPath(), fileRequest.getDisplayName());
                                boolean newFile = file.createNewFile();
                                if (newFile) {
                                    return new FileResponse(true, Uri.parse(file.getAbsolutePath()), file, false);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        return null;
                    }
                })
                .map(new Function<FileResponse, FileResponse>() {
                    @Override
                    public FileResponse apply(FileResponse fileResponse) {
                        try {
                            OutputStream outputStream = new FileOutputStream(fileResponse.getFile());
                            BufferedOutputStream fileOutputStream = new BufferedOutputStream(outputStream);
                            InputStream inputStream = new FileInputStream(srcFile[0]);
                            BufferedInputStream fileInputStream = new BufferedInputStream(inputStream);
                            byte[] buff = new byte[1024];
                            while (fileInputStream.read(buff) != -1) {
                                fileOutputStream.write(buff);
                            }
                            fileInputStream.close();
                            fileOutputStream.close();
                            return new FileResponse(true, Uri.parse(fileResponse.getFile().getAbsolutePath()), fileResponse.getFile(), false);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return new FileResponse(false, null, null, false);
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<FileResponse>() {
                    @Override
                    public void accept(FileResponse fileResponse) {
                        if (iFileListener != null) {
                            iFileListener.onFileResponse(fileResponse);
                        }
                    }
                });
    }

    /**
     * 查询文件
     * @param <T> 泛型
     * @param context     context
     * @param baseRequest baseRequest
     * @param iFileListener iFileListener
     */
    @Override
    public <T extends BaseRequest> void queryFile(Context context, final T baseRequest, final IFileListener iFileListener) {
        Observable.zip(Observable.just(baseRequest), Observable.just(baseRequest), new BiFunction<T, T, FileResponse>() {
            @Override
            public FileResponse apply(T imageRequest, T fileRequest) {
                String path = baseRequest.getParentPath();
                File file = new File(path);
                if (file.exists()) {
                    if (fileRequest instanceof FileRequest) {
                        if (file.isDirectory()) {
                            FileRequest fr = (FileRequest) fileRequest;
                            file = new File(path, fr.getDisplayName());
                            if (file.isFile() && file.exists()) {
                                return new FileResponse(true, Uri.parse(file.getAbsolutePath()), file, false);
                            }
                        } else {
                            return new FileResponse(true, Uri.parse(file.getAbsolutePath()), file, true);
                        }
                    } else if (imageRequest instanceof ImageRequest) {
                        if (file.isDirectory()) {
                            ImageRequest ir = (ImageRequest) imageRequest;
                            file = new File(path, ir.getDisplayName());
                            if (file.isFile() && file.exists()) {
                                return new FileResponse(true, Uri.parse(file.getAbsolutePath()), file, false);
                            }
                        } else {
                            return new FileResponse(true, Uri.parse(file.getAbsolutePath()), file, true);
                        }

                    }
                }
                return new FileResponse(false, null, null, false);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<FileResponse>() {
                    @Override
                    public void accept(FileResponse fileResponse) {
                        if (iFileListener != null) {
                            iFileListener.onFileResponse(fileResponse);
                        }
                    }
                });
    }

}
