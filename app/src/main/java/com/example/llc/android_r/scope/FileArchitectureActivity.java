package com.example.llc.android_r.scope;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.llc.android_r.R;
import com.example.llc.storage.sanbox.FileAccessFactory;
import com.example.llc.storage.sanbox.request.coy.CopyRequest;
import com.example.llc.storage.sanbox.request.download.DownloadRequest;
import com.example.llc.storage.sanbox.request.file.FileRequest;
import com.example.llc.storage.sanbox.response.FileResponse;
import com.example.llc.storage.sanbox.storage.IFileListener;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * com.example.llc.android_r.FileArchitectureActivity
 *
 * @author liulongchao
 * @since 2022/4/21
 */
public class FileArchitectureActivity extends AppCompatActivity {

    private static final String DOWN_LOAD_URL = "https://down.qq.com/qqweb/QQlite/Android_apk/qqlite_4.0.1.1060_537064364.apk";
    private static final String DOWN_LOAD_FILE_NAME = "qq_test.apk";
    private static final String CREATE_FILE_NAME = "cba.txt";
    private static final String UPDATE_FILE_NAME = "nba.txt";
    private static final String CREATE_UPDATE_FILE_NAME = "cba_nba.txt";

    private Button createFile;
    private Button updateFile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_arc);
        createFile = findViewById(R.id.createFile);
        updateFile = findViewById(R.id.updateFile);
    }

    /**
     * 使用分区存储框架，下载文件
     * @param view view
     */
    public void downloadFile(View view) {

        try {
            DownloadRequest downloadRequest = new DownloadRequest.Builder()
                    .setDownLoadUrl(DOWN_LOAD_URL)
                    .setDisplayName(DOWN_LOAD_FILE_NAME)
                    .setPath(Environment.getExternalStorageDirectory() + File.separator + getPackageName())
                    .setTitle(DOWN_LOAD_FILE_NAME)
                    .build(new File(DOWN_LOAD_FILE_NAME));
            FileAccessFactory.getIFile(downloadRequest)
                    .downLoadFile(this, downloadRequest, new IFileListener() {
                        @Override
                        public void onFileResponse(FileResponse fileResponse) {
                            if (fileResponse.isSuccess()) {
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                intent.setDataAndType(fileResponse.getUri(), "application/vnd.android.package-archive");
                                startActivity(intent);
                                Toast.makeText(FileArchitectureActivity.this, "FLAG_GRANT_READ_URI_PERMISSION", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(FileArchitectureActivity.this, "FLAG_GRANT_READ_URI_PERMISSION_FAIL", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用架构创建文件
     * @param view view
     */
    public void createFile(View view) {
        try {
            final FileRequest fileRequest = new FileRequest.Builder()
                    .setDisplayName(CREATE_FILE_NAME)
                    .setPath(Environment.getExternalStorageDirectory() + File.separator + getPackageName())
                    .setTitle(CREATE_FILE_NAME)
                    .build(new File(CREATE_FILE_NAME));
            FileAccessFactory.getIFile(fileRequest)
                    .createFile(this, fileRequest, new IFileListener() {
                        @Override
                        public void onFileResponse(FileResponse fileResponse) {
                            if (fileResponse.isSuccess()) {
                                File responseFile = fileResponse.getFile();
                                if (responseFile != null) {
                                    try {
                                        // 向 txt 文件写入 hello world
                                        OutputStream outputStream = new FileOutputStream(responseFile);
                                        BufferedOutputStream bfs = new BufferedOutputStream(outputStream);
                                        bfs.write("where amazing happens！".getBytes());
                                        bfs.close();

                                        // 从 txt 文件读取 hello world
                                        InputStream inputStream = new FileInputStream(responseFile);
                                        int ch;
                                        StringBuilder sb = new StringBuilder();
                                        while ((ch = inputStream.read()) != -1) {
                                            sb.append((char) ch);
                                        }
                                        createFile.setText(sb.toString());
                                        inputStream.close();
                                        outputStream.close();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用分区存储框架，文件夹
     * @param view view
     */
    public void queryFile(View view) {
        final FileRequest fileRequest = new FileRequest.Builder()
                .setDisplayName(CREATE_FILE_NAME)
                .setPath(Environment.getExternalStorageDirectory() + File.separator + getPackageName())
                .setTitle(CREATE_FILE_NAME)
                .build(new File(CREATE_FILE_NAME));
        FileAccessFactory.getIFile(fileRequest)
                .queryFile(this, fileRequest, new IFileListener() {
                    @Override
                    public void onFileResponse(FileResponse fileResponse) {
                        if (fileResponse.isDirectory()) {
                            Toast.makeText(FileArchitectureActivity.this, fileResponse.getFile().getName() + "查询的文件夹存在", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(FileArchitectureActivity.this, fileResponse.getFile().getName() + "查询的文件存在", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    /**
     * 使用分区存储框架，删除文件
     * @param view view
     */
    public void deleteFile(View view) {
        final FileRequest fileRequest = new FileRequest.Builder()
                .setDisplayName(CREATE_FILE_NAME)
                .setPath(Environment.getExternalStorageDirectory() + File.separator + getPackageName())
                .setTitle(CREATE_FILE_NAME)
                .build(new File(CREATE_FILE_NAME));
        FileAccessFactory.getIFile(fileRequest)
                .deleteFile(this, fileRequest, new IFileListener() {
                    @Override
                    public void onFileResponse(FileResponse fileResponse) {
                        if (fileResponse.isSuccess()) {
                            Toast.makeText(FileArchitectureActivity.this, fileResponse.getFile().getName() + "文件删除成功", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    /**
     * 使用分区存储框架，更新文件
     * @param view view
     */
    public void updateFile(View view) {
        final FileRequest srcRequest = new FileRequest.Builder()
                .setDisplayName(CREATE_FILE_NAME)
                .setPath(Environment.getExternalStorageDirectory() + File.separator + getPackageName())
                .setTitle(CREATE_FILE_NAME)
                .build(new File(CREATE_FILE_NAME));

        final FileRequest destRequest = new FileRequest.Builder()
                .setDisplayName(UPDATE_FILE_NAME)
                .setPath(Environment.getExternalStorageDirectory() + File.separator + getPackageName())
                .setTitle(UPDATE_FILE_NAME)
                .build(new File(UPDATE_FILE_NAME));

        FileAccessFactory.getIFile(srcRequest)
                .updateFile(this, srcRequest, destRequest, new IFileListener(){
                    @Override
                    public void onFileResponse(FileResponse fileResponse) {
                        if (fileResponse.isSuccess()) {
                            updateFile.setText(fileResponse.getFile().getName());
                        }
                    }
                });
    }

    /**
     * 使用分区存储框架，复制文件
     * @param view view
     */
    public void copyFile(View view) {
        try {
            CopyRequest copyRequest = new CopyRequest.Builder()
                    .setDisplayName(UPDATE_FILE_NAME)
                    .setPath(Environment.getExternalStorageDirectory() + File.separator + getPackageName())
                    .setTitle(UPDATE_FILE_NAME)
                    .setDistFile(new File(Environment.getExternalStorageDirectory() + File.separator + getPackageName() + File.separator, CREATE_UPDATE_FILE_NAME))
                    .build(new File(UPDATE_FILE_NAME));
            FileAccessFactory.getIFile(copyRequest)
                    .copyFile(this, copyRequest, new IFileListener() {
                        @Override
                        public void onFileResponse(FileResponse fileResponse) {
                            if (fileResponse.isSuccess()) {
                                Toast.makeText(FileArchitectureActivity.this, "文件复制成功", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(FileArchitectureActivity.this, "文件复制失败", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
