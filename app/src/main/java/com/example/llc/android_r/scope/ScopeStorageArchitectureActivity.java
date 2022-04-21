package com.example.llc.android_r.scope;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.llc.android_r.R;
import com.example.llc.storage.sanbox.FileAccessFactory;
import com.example.llc.storage.sanbox.request.coy.CopyRequest;
import com.example.llc.storage.sanbox.request.download.DownloadRequest;
import com.example.llc.storage.sanbox.request.file.FileRequest;
import com.example.llc.storage.sanbox.request.image.ImageRequest;
import com.example.llc.storage.sanbox.response.FileResponse;
import com.example.llc.storage.sanbox.storage.IFileListener;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * com.example.llc.android_r.ScopeStorageArchitectureActivity
 *
 * @author liulongchao
 * @since 2022/4/19
 */
public class ScopeStorageArchitectureActivity extends AppCompatActivity {

    private static final String DOWN_LOAD_URL = "https://down.qq.com/qqweb/QQlite/Android_apk/qqlite_4.0.1.1060_537064364.apk";
    private static final String DOWN_LOAD_FILE_NAME = "qq_test.apk";
    private static final String CREATE_FILE_NAME = "cba.txt";
    private static final String CREATE_MEDIA_FILE_NAME = "Kobe.jpg";
    private static final String UPDATE_MEDIA_FILE_NAME = "kobe.jpeg";
    private static final String CREATE_MEDIA_IMAGE_MIME_TYPE = "image/jpeg";

    private Button createFile;
    private Button queryFile;
    private Button updateFile;
    private Button updateImage;
    private ImageView testImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scope_storage_arc);

        createFile = findViewById(R.id.createFile);
        queryFile = findViewById(R.id.queryFile);
        updateFile = findViewById(R.id.updateFile);
        updateImage = findViewById(R.id.updateImage);
        testImage = findViewById(R.id.image);
    }

    /**
     * 使用分区存储框架，下载文件
     *
     * @param view view
     */
    public void downloadFile(View view) {
        try {
            DownloadRequest downloadRequest = new DownloadRequest.Builder()
                    .setDownLoadUrl(DOWN_LOAD_URL)
                    .setDisplayName(DOWN_LOAD_FILE_NAME)
                    .setPath(Environment.DIRECTORY_DOWNLOADS + File.separator + getPackageName())
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
                                Toast.makeText(ScopeStorageArchitectureActivity.this, "FLAG_GRANT_READ_URI_PERMISSION", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(ScopeStorageArchitectureActivity.this, "FLAG_GRANT_READ_URI_PERMISSION_FAIL", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用分区存储框架，创建文件
     *
     * @param view view
     */
    public void createFile(View view) {
        try {
            final FileRequest fileRequest = new FileRequest.Builder()
                    .setDisplayName(CREATE_FILE_NAME)
                    .setPath(Environment.DIRECTORY_DOCUMENTS + File.separator + getPackageName())
                    .setTitle(CREATE_FILE_NAME)
                    .build(new File(CREATE_FILE_NAME));
            FileAccessFactory.getIFile(fileRequest)
                    .createFile(this, fileRequest, new IFileListener() {
                        @Override
                        public void onFileResponse(FileResponse fileResponse) {
                            // TODO 测试代码，向创建的文件中 写入内容
                            if (fileResponse.isSuccess()) {
                                Uri responseUri = fileResponse.getUri();
                                if (responseUri != null) {
                                    try {
                                        // 向 txt 文件写入 hello world
                                        OutputStream outputStream = getContentResolver().openOutputStream(responseUri);
                                        BufferedOutputStream bfs = new BufferedOutputStream(outputStream);
                                        bfs.write("where amazing happens！".getBytes());
                                        bfs.close();

                                        // 从 txt 文件读取 hello world
                                        InputStream inputStream = getContentResolver().openInputStream(responseUri);
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
                .setPath(Environment.DIRECTORY_DOCUMENTS + File.separator + getPackageName())
                .setTitle(CREATE_FILE_NAME)
                .build(new File(CREATE_FILE_NAME));
        FileAccessFactory.getIFile(fileRequest)
                .queryFile(this, fileRequest, new IFileListener() {
                    @Override
                    public void onFileResponse(FileResponse fileResponse) {
                        if (fileResponse.isSuccess()) {
                            // TODO 测试代码 从查询到的 txt 文件读取 hello world
                            try {
                                InputStream inputStream = getContentResolver().openInputStream(fileResponse.getUri());
                                int ch;
                                StringBuilder sb = new StringBuilder();
                                while ((ch = inputStream.read()) != -1) {
                                    sb.append((char) ch);
                                }
                                queryFile.setText(sb.toString());
                                inputStream.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (fileResponse.getIntent() != null){
                            startActivityForResult(fileResponse.getIntent(), 0x12);
                        } else {
                            Toast.makeText(ScopeStorageArchitectureActivity.this, "文件查询失败", Toast.LENGTH_LONG).show();
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
                .setPath(Environment.DIRECTORY_DOCUMENTS + File.separator + getPackageName())
                .setTitle(CREATE_FILE_NAME)
                .build(new File(CREATE_FILE_NAME));
        FileAccessFactory.getIFile(fileRequest)
                .deleteFile(this, fileRequest, new IFileListener() {
                    @Override
                    public void onFileResponse(FileResponse fileResponse) {
                        if (fileResponse.isSuccess()) {
                            Toast.makeText(ScopeStorageArchitectureActivity.this, "文件删除成功", Toast.LENGTH_LONG).show();
                        } else if (fileResponse.getIntent() != null) {
                            startActivityForResult(fileResponse.getIntent(), 0x13);
                        } else {
                            Toast.makeText(ScopeStorageArchitectureActivity.this, "文件删除失败", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    /**
     * 使用分区存储框架，更新文件
     * @param view view
     */
    public void updateFile(View view) {
        final FileRequest fileRequest = new FileRequest.Builder()
                .setDisplayName(CREATE_FILE_NAME)
                .setPath(Environment.DIRECTORY_DOCUMENTS + File.separator + getPackageName())
                .setTitle(CREATE_FILE_NAME)
                .build(new File(CREATE_FILE_NAME));
        FileAccessFactory.getIFile(fileRequest)
                .updateFile(this, fileRequest, fileRequest, new IFileListener(){
                    @Override
                    public void onFileResponse(FileResponse fileResponse) {
                        if (fileResponse.isSuccess()) {

                        } else if (fileResponse.getIntent() != null) {
                            startActivityForResult(fileResponse.getIntent(), 0x14);
                        } else {
                            Toast.makeText(ScopeStorageArchitectureActivity.this, "文件更新失败", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    /**
     * 使用分区存储框架，创建 媒体 文件，以图片为例子
     * @param view view
     */
    public void createImage(View view) {
        try {
            ImageRequest imageRequest = new ImageRequest.Builder()
                    .setDisplayName(CREATE_MEDIA_FILE_NAME)
                    .setPath(Environment.DIRECTORY_PICTURES + File.separator + getPackageName())
                    .setMimeType(CREATE_MEDIA_IMAGE_MIME_TYPE)
                    .build(new File(CREATE_MEDIA_FILE_NAME));
            FileAccessFactory.getIFile(imageRequest)
                    .createFile(this, imageRequest, new IFileListener() {
                        @Override
                        public void onFileResponse(FileResponse fileResponse) {
                            if (fileResponse.isSuccess()) {
                                Uri uri = fileResponse.getUri();
                                try {
                                    OutputStream outputStream = getContentResolver().openOutputStream(uri);
                                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.kobe);
                                    boolean compress = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                                    if (compress) {
                                        Toast.makeText(ScopeStorageArchitectureActivity.this, fileResponse.getFile().getName() + "文件创建成功", Toast.LENGTH_LONG).show();
                                        InputStream inputStream = getContentResolver().openInputStream(uri);
                                        bitmap = BitmapFactory.decodeStream(inputStream);
                                        if (bitmap != null) {
                                            testImage.setImageBitmap(bitmap);
                                        }
                                        inputStream.close();
                                    }
                                    outputStream.close();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用分区存储框架，查询 媒体 文件，以图片为例子
     * @param view view
     */
    public void queryImage(View view) {
        ImageRequest imageRequest = new ImageRequest.Builder()
                .setDisplayName(CREATE_MEDIA_FILE_NAME)
                .setPath(Environment.DIRECTORY_PICTURES + File.separator + getPackageName())
                .setMimeType(CREATE_MEDIA_IMAGE_MIME_TYPE)
                .build(new File(CREATE_MEDIA_FILE_NAME));
        FileAccessFactory.getIFile(imageRequest)
                .queryFile(this, imageRequest, new IFileListener() {
                    @Override
                    public void onFileResponse(FileResponse fileResponse) {
                        if (fileResponse.isSuccess()) {
                            Uri uri = fileResponse.getUri();
                            testImage.setImageURI(uri);
                        } else {
                            Toast.makeText(ScopeStorageArchitectureActivity.this, fileResponse.getFile().getName() + "文件查询失败", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    /**
     * 使用分区存储框架，更新 媒体 文件，以图片为例子
     * @param view view
     */
    public void updateImage(View view) {
        ImageRequest srcRequest = new ImageRequest.Builder()
                .setDisplayName(CREATE_MEDIA_FILE_NAME)
                .setPath(Environment.DIRECTORY_PICTURES + File.separator + getPackageName())
                .setMimeType(CREATE_MEDIA_IMAGE_MIME_TYPE)
                .build(new File(CREATE_MEDIA_FILE_NAME));

        ImageRequest destRequest = new ImageRequest.Builder()
                .setDisplayName(UPDATE_MEDIA_FILE_NAME)
                .setPath(Environment.DIRECTORY_PICTURES + File.separator + getPackageName())
                .setMimeType(CREATE_MEDIA_IMAGE_MIME_TYPE)
                .build(new File(UPDATE_MEDIA_FILE_NAME));

        FileAccessFactory.getIFile(destRequest)
                .updateFile(this, destRequest, srcRequest, new IFileListener(){
                    @Override
                    public void onFileResponse(FileResponse fileResponse) {
                        if (fileResponse.isSuccess()) {
                            String selection = MediaStore.Images.ImageColumns.DISPLAY_NAME+ "=?";
                            String[] arg = new String[] { CREATE_MEDIA_FILE_NAME };
                            String[] projection = new String[] { MediaStore.Images.ImageColumns._ID };
                            Cursor query = getContentResolver().query(fileResponse.getUri(), projection, selection, arg, "");
                            if (query != null && query.moveToFirst()) {
                                int columnIndex = query.getColumnIndex(MediaStore.Images.Media._ID);
                                if (columnIndex >= 0) {
                                    String displayName = query.getString(columnIndex);
                                    updateImage.setText(displayName);
                                }
                                query.close();
                            }
                        }
                    }
                });
    }

    /**
     * 使用分区存储框架，删除 媒体 文件，以图片为例子
     * @param view view
     */
    public void deleteImage(View view) {
        ImageRequest srcRequest = new ImageRequest.Builder()
                .setDisplayName(CREATE_MEDIA_FILE_NAME)
                .setPath(Environment.DIRECTORY_PICTURES + File.separator + getPackageName())
                .setMimeType(CREATE_MEDIA_IMAGE_MIME_TYPE)
                .build(new File(CREATE_MEDIA_FILE_NAME));
        FileAccessFactory.getIFile(srcRequest)
                .deleteFile(this, srcRequest, new IFileListener() {
                    @Override
                    public void onFileResponse(FileResponse fileResponse) {
                        if (fileResponse.isSuccess()) {
                            Toast.makeText(ScopeStorageArchitectureActivity.this, fileResponse.getFile().getName() + "删除成功", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    /**
     * 使用分区存储框架，复制 媒体 文件，以图片为例子
     * @param view view
     */
    public void copyImage(View view) {
        try {
            CopyRequest copyRequest = new CopyRequest.Builder()
                    .setDisplayName(CREATE_MEDIA_FILE_NAME)
                    .setPath(Environment.DIRECTORY_PICTURES + File.separator + getPackageName())
                    .setTitle(CREATE_MEDIA_FILE_NAME)
                    .setDistFile(new File(Environment.DIRECTORY_PICTURES + File.separator + getPackageName(), UPDATE_MEDIA_FILE_NAME))
                    .build(new File(CREATE_MEDIA_FILE_NAME));
            FileAccessFactory.getIFile(copyRequest)
                    .copyFile(this, copyRequest, new IFileListener() {
                        @Override
                        public void onFileResponse(FileResponse fileResponse) {
                            if (fileResponse.isSuccess()) {
                                Uri responseUri = fileResponse.getUri();
                                testImage.setImageURI(responseUri);
                                Toast.makeText(ScopeStorageArchitectureActivity.this, "文件复制成功", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(ScopeStorageArchitectureActivity.this, "文件复制失败", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 0x12:
                    // 查询
                    if (data != null) {
                        Uri uri = data.getData();
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(uri);
                            int ch;
                            StringBuilder sb = new StringBuilder();
                            while ((ch = inputStream.read()) != -1) {
                                sb.append((char) ch);
                            }
                            queryFile.setText(sb.toString());
                            inputStream.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 0x13:
                    // 删除
                    if (data != null) {
                        Uri uri = data.getData();
                        try {
                            boolean delete = DocumentsContract.deleteDocument(getContentResolver(), uri);
                            if (delete) {
                                Toast.makeText(ScopeStorageArchitectureActivity.this, "文件删除成功", Toast.LENGTH_LONG).show();
                            }
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 0x14:
                    if (data != null) {
                        Uri uri = data.getData();
                        try {
                            // 获取 OutputStream
                            OutputStream outputStream = getContentResolver().openOutputStream(uri);
                            outputStream.write("Storage Access Framework Example".getBytes(StandardCharsets.UTF_8));

                            InputStream inputStream = getContentResolver().openInputStream(uri);
                            int ch;
                            StringBuilder sb = new StringBuilder();
                            while ((ch = inputStream.read()) != -1) {
                                sb.append((char) ch);
                            }
                            updateFile.setText(sb.toString());
                            inputStream.close();
                            outputStream.close();
                        } catch (Exception e) {
                            Toast.makeText(this, "修改文件失败！", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
            }
        }
    }
}
