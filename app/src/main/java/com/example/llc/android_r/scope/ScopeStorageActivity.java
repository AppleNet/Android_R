package com.example.llc.android_r.scope;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.llc.android_r.R;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * com.example.llc.android_r.ScopeStorageActivity
 *
 * @author liulongchao
 * @since 2022/4/15
 */
public class ScopeStorageActivity extends AppCompatActivity {
    /**
     *
         Environment.getDataDirectory() = /data
         Environment.getDownloadCacheDirectory() = /cache
         Environment.getExternalStorageDirectory() = /mnt/sdcard
         Environment.getExternalStoragePublicDirectory(“test”) = /mnt/sdcard/test
         Environment.getRootDirectory() = /system

         getPackageCodePath() = /data/app/com.my.app-1.apk
         getPackageResourcePath() = /data/app/com.my.app-1.apk
         getDatabasePath(“test”) = /data/data/packageName/databases/test
         getDir(“test”, Context.MODE_PRIVATE) = /data/data/packageName/app_test

         getExternalCacheDir() = /mnt/sdcard/Android/data/packageName/cache
         getExternalFilesDir(“test”) = /mnt/sdcard/Android/data/packageName/files/test
         getExternalFilesDir(null) = /mnt/sdcard/Android/data/packageName/files
         getFilesDir() = /data/data/packageName/files
         getCacheDir() = /data/data/packageName/cache
     *
     * */
    private final String fileName = "mars_new.jpg";
    private String mPackageName;
    private ImageView showImages;
    private Button createFileDir;
    private Button createCacheDir;
    private Button createExternalFilesDir;
    private Button createExternalCacheDir;
    private Button forceCreateExternalScopeStorageDir;
    private Button createMediaStorageDir;
    private Button createMediaStorageDownloadsDir;
    private Button queryOtherImages;
    private Button updateYouSelfImages;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scope_storage);
        showImages = findViewById(R.id.showImages);
        createFileDir = findViewById(R.id.crateSAF);
        createCacheDir = findViewById(R.id.createCacheDir);
        createExternalFilesDir = findViewById(R.id.createExternalFilesDir);
        createExternalCacheDir = findViewById(R.id.createExternalCacheDir);
        forceCreateExternalScopeStorageDir = findViewById(R.id.forceCreateExternalScopeStorageDir);
        createMediaStorageDir = findViewById(R.id.createMediaStorageDir);
        createMediaStorageDownloadsDir = findViewById(R.id.createMediaStorageDownloadsDir);
        queryOtherImages = findViewById(R.id.queryOtherImages);
        updateYouSelfImages = findViewById(R.id.updateYouSelfImages);
        mPackageName = getPackageName();
    }

    /**
     * 检测当前 activity 是否有相关权限
     * @param activity activity
     */
    private void checkPermission(Activity activity) {
        // 检测 sd 卡，有没有写或则读权限
        if (activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || activity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // 请求相关权限
            activity.requestPermissions(
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 0x16);
        } else {
            queryOtherImagesByPermission();
        }
    }

    /**
     * 权限请求结果回调
     * @param requestCode 请求权限码
     * @param permissions 请求的权限列表
     * @param grantResults 权限同意拒绝列表
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0x16) {
            if (grantResults.length > 0) {
                if (grantResults[0]  == PackageManager.PERMISSION_GRANTED) {
                    // 同意了相关权限
                    queryOtherImagesByPermission();
                } else {
                    // 拒绝了相关权限
                    Toast.makeText(this, "You denied STORAGE permission", Toast.LENGTH_SHORT).show();
                }
            } else {
                // 拒绝了相关权限
                Toast.makeText(this, "You denied STORAGE permission", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // ---------- 访问内部存储示例， 不需要向用户申请，让用户授予存储权限 ---------- //
    /**
     * 使用 Context.getFilesDir() 获取 /data/data/packageName/files 目录，并使用 File 的形式创建一个文件
     * @param view view
     */
    public void createFilesDir(View view) {
        try {
            File file = new File(getFilesDir(), "/internal_files_dir.txt");
            if (file.exists()) {
                file.delete();
            }
            boolean flag = file.createNewFile();
            if (flag) {
                createFileDir.setText(file.getAbsolutePath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用 Context.getCacheDir() 获取 /data/data/packageName/cache 目录，并使用 File 的形式创建一个文件
     * @param view view
     */
    public void createCacheDir(View view) {
        try {
            File file = new File(getCacheDir(), "/internal_cache_dir.txt");
            if (file.exists()) {
                file.delete();
            }
            boolean flag = file.createNewFile();
            if (flag) {
                createCacheDir.setText(file.getAbsolutePath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ---------- 访问sd卡上应用的内部存储示例， 不需要向用户申请让用户授予存储权限 ---------- //
    /**
     * 使用 Context.getExternalFilesDir 获取 /mnt/sdcard/Android/data/packageName/files 目录，使用 File 的形式创建一个文件
     * @param view view
     */
    public void createExternalFilesDir(View view) {
        try {
            File file = new File(getExternalFilesDir(""), "/external_files_dir.txt");
            if (file.exists()) {
                file.delete();
            }
            boolean flag = file.createNewFile();
            if (flag) {
                createExternalFilesDir.setText(file.getAbsolutePath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用 Context.getExternalCacheDir 获取 /mnt/sdcard/Android/data/packageName/cache 目录，使用 File 的形式创建一个文件
     * @param view view
     */
    public void createExternalCacheDir(View view) {
        try {
            File file = new File(getExternalCacheDir(), "/external_cache_dir.txt");
            if (file.exists()) {
                file.delete();
            }
            boolean flag = file.createNewFile();
            if (flag) {
                createExternalCacheDir.setText(file.getAbsolutePath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ----------  访问sd卡上共享的存储目录，在分区存储模型下 和 非分区存储模型下  ---------- //


    /**
     * 在分区存储模型下，外部存储设备的公共区域是不让访问的，如果强行访问，会在创建或读写文件的api上报错
     * 在分区存储模型下，强行访问，已经进入设置页面，开启了存储权限
     *
     * 在分区存储模型下
     * 使用 Environment.getExternalStorageDirectory() = /mnt/sdcard 获取sd卡上的目录，并使用 File 的形式创建文件，此时已经给了存储权限
     *
     * @param view view
     */
    public void forceCreateExternalScopeStorageDir(View view) {
        // 使用 File 的形式，给了存储权限
        try {
            File file = new File(Environment.getExternalStorageDirectory(), "/external_cache_dir.txt");
            if (file.exists()) {
                file.delete();
            }
            boolean flag = file.createNewFile();
            if (flag) {
                forceCreateExternalScopeStorageDir.setText(file.getAbsolutePath());
            }
        } catch (Exception e) {
            Log.d("SwanHostImpl", Log.getStackTraceString(e));
            forceCreateExternalScopeStorageDir.setText(Log.getStackTraceString(e));
        }
    }


    /**
     * 使用分区存储的应用对自己创建的文件始终拥有读/写权限，无论文件是否位于应用的私有目录内。因此，如果您的应用仅保存和访问自己创建的文件，则无需请求获得
     * READ_EXTERNAL_STORAGE 或 WRITE_EXTERNAL_STORAGE 权限
     *
     * 在分区存储模型下
     * 此时禁止存储权限，使用 MediaStorage 获取sd卡上的 Documents 目录，并创建txt文件，
     *
     * @param view view
     */
    public void createMediaStorageDir(View view) {
        Uri uri = MediaStore.Files.getContentUri("external");
        ContentResolver contentResolver = getContentResolver();
        ContentValues contentValues = new ContentValues();
        // 在 Downloads 目录下 创建 Mars 文件夹
        contentValues.put(MediaStore.Files.FileColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS + File.separator + mPackageName);
        contentValues.put(MediaStore.Files.FileColumns.DISPLAY_NAME, "media_storage_downloads_dir.txt");
        contentValues.put(MediaStore.Files.FileColumns.TITLE, "media_storage_downloads_dir");
        Uri insert = contentResolver.insert(uri, contentValues);
        if (insert != null) {
            try {
                // 向 txt 文件写入 hello world
                OutputStream outputStream = contentResolver.openOutputStream(insert);
                BufferedOutputStream bfs = new BufferedOutputStream(outputStream);
                bfs.write("hello world".getBytes());
                bfs.close();

                // 从 txt 文件读取 hello world
                InputStream inputStream = contentResolver.openInputStream(insert);
                int ch;
                StringBuilder sb = new StringBuilder();
                while ((ch = inputStream.read()) != -1) {
                    sb.append((char) ch);
                }
                createMediaStorageDir.setText(sb.toString());
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            createMediaStorageDir.setText("使用MediaStorage在Documents目录下创建txt文件失败");
        }
    }


    /**
     * 使用分区存储的应用对自己创建的文件始终拥有读/写权限，无论文件是否位于应用的私有目录内。因此，如果您的应用仅保存和访问自己创建的文件，则无需请求获得
     * READ_EXTERNAL_STORAGE 或 WRITE_EXTERNAL_STORAGE 权限
     *
     * 在分区存储模型下
     * 此时禁止存储权限，使用 MediaStorage 获取sd卡上的 Downloads 目录，并下载 apk 文件，
     * @param view view
     */
    public void createMediaStorageDownloadsDir(View view) {
        Toast.makeText(this, "开始下载", Toast.LENGTH_LONG).show();
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    URL url = new URL("https://down.qq.com/qqweb/QQlite/Android_apk/qqlite_4.0.1.1060_537064364.apk");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    InputStream inputStream = connection.getInputStream();
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                    Uri uri = MediaStore.Downloads.getContentUri("external");
                    ContentResolver contentResolver = getContentResolver();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + File.separator + mPackageName);
                    contentValues.put(MediaStore.Downloads.DISPLAY_NAME, "test_qq.apk");
                    final Uri insert = contentResolver.insert(uri, contentValues);
                    if (insert != null) {
                        OutputStream outputStream = contentResolver.openOutputStream(insert);
                        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
                        byte[] buffer = new byte[1024];
                        int bytes = bufferedInputStream.read(buffer);
                        while (bytes > 0) {
                            bufferedOutputStream.write(buffer, 0, bytes);
                            bufferedOutputStream.flush();
                            bytes = bufferedInputStream.read(buffer);
                        }
                        bufferedOutputStream.close();
                        bufferedInputStream.close();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                intent.setDataAndType(insert, "application/vnd.android.package-archive");
                                startActivity(intent);
                                Toast.makeText(ScopeStorageActivity.this, "FLAG_GRANT_READ_URI_PERMISSION", Toast.LENGTH_LONG).show();
                                createMediaStorageDownloadsDir.setText("下载成功");
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }

    /**
     * 使用分区存储的应用对自己创建的文件始终拥有读/写权限，无论文件是否位于应用的私有目录内。因此，如果您的应用仅保存和访问自己创建的文件，则无需请求获得
     * READ_EXTERNAL_STORAGE 或 WRITE_EXTERNAL_STORAGE 权限
     *
     * 在分区存储模型下
     * 此时禁止存储权限，使用 MediaStorage 获取sd卡上的 Images 目录，创建图片，
     * @param view view
     */
    public void createMediaStorageImagesDir(View view) {
        // MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Uri uri = MediaStore.Images.Media.getContentUri("external");
        ContentResolver contentResolver = getContentResolver();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.ImageColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + File.separator + mPackageName);
        contentValues.put(MediaStore.Images.ImageColumns.DISPLAY_NAME, fileName);
        contentValues.put(MediaStore.Images.ImageColumns.MIME_TYPE, "image/jpeg");
        Uri insert = contentResolver.insert(uri, contentValues);
        if (insert != null) {
            try {
                OutputStream outputStream = contentResolver.openOutputStream(insert);
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.comment_like_24_24);
                boolean compress = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                if (compress) {
                    Toast.makeText(ScopeStorageActivity.this, "jpg文件创建成功", Toast.LENGTH_LONG).show();
                }
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 使用分区存储的应用对自己创建的文件始终拥有读/写权限，无论文件是否位于应用的私有目录内。因此，如果您的应用仅保存和访问自己创建的文件，则无需请求获得
     * READ_EXTERNAL_STORAGE 或 WRITE_EXTERNAL_STORAGE 权限
     *
     * 在分区存储模型下
     * 此时禁止存储权限，使用 MediaStorage 获取sd卡上的 Pictures 目录，并读取自己应用创建的图片
     * @param view view
     */
    public void queryYouSelfImages(View view) {
        // MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Uri uri = MediaStore.Images.Media.getContentUri("external");
        ContentResolver contentResolver = getContentResolver();
        String selection = MediaStore.Images.ImageColumns.DISPLAY_NAME+ "=?";
        String [] arg = { fileName };
        String [] projection = { MediaStore.Images.ImageColumns._ID };
        Cursor cursor = contentResolver.query(uri, projection, selection, arg, "");
        if (cursor != null && cursor.moveToFirst()) {
            // Android R 提供的 API 将得到的 id 转换成对应的 uri
            Uri imageId = ContentUris.withAppendedId(uri, cursor.getLong(0));
            showImages.setImageURI(imageId);
            cursor.close();
        }
    }

    /**
     * 若要访问其他应用创建的文件，则需要 READ_EXTERNAL_STORAGE 权限。并且仍然只能使用 MediaStore 提供的 API 或是 SAF （存储访问框架）访问
     *
     * 在分区存储模型下
     * 此时禁止存储权限，使用 MediaStorage 获取sd卡上的 Pictures 目录，读取其他应用创建的图片
     * @param view view
     */
    public void queryOtherImages(View view) {
        // MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Uri uri = MediaStore.Images.Media.getContentUri("external");
        ContentResolver contentResolver = getContentResolver();
        Bundle bundle = new Bundle();
        bundle.putString(MediaStore.Images.ImageColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
        bundle.putString(MediaStore.Images.ImageColumns.DISPLAY_NAME, "Youku-1616944252788.png");
        String [] projection = { MediaStore.Images.ImageColumns._ID, MediaStore.Images.ImageColumns.DISPLAY_NAME };
        Cursor cursor = contentResolver.query(uri, projection, bundle, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME);
                String displayName = cursor.getString(columnIndex);
                if (displayName.equals("Youku-1616944252788.png")) {
                    // Android R 提供的 API 将得到的 id 转换成对应的 uri
                    Uri imageId = ContentUris.withAppendedId(uri, cursor.getLong(0));
                    showImages.setImageURI(imageId);
                }
                Log.d("SwanHostImpl", "index: " + columnIndex + ", 文件名字：" + displayName);
            }
            cursor.close();
        } else {
            queryOtherImages.setText("查询失败");
        }
    }

    /**
     * 若要访问其他应用创建的文件，则需要 READ_EXTERNAL_STORAGE 权限。并且仍然只能使用 MediaStore 提供的 API 或是 SAF （存储访问框架）访问
     *
     * 在分区存储模型下
     * 此时授予存储权限，使用 MediaStorage 获取sd卡上的 Pictures 目录，读取其他应用创建的图片
     * @param view view
     */
    public void queryOtherImagesByPermission(View view) {
        checkPermission(this);
    }

    /**
     * 若要访问其他应用创建的文件，则需要 READ_EXTERNAL_STORAGE 权限。并且仍然只能使用 MediaStore 提供的 API 或是 SAF （存储访问框架）访问
     *
     * 在分区存储模型下
     * 此时授予存储权限，使用 MediaStorage 获取sd卡上的 Pictures 目录，读取其他应用创建的图片
     */
    private void queryOtherImagesByPermission() {
        Uri uri = MediaStore.Images.Media.getContentUri("external");
        ContentResolver contentResolver = getContentResolver();
        Bundle bundle = new Bundle();
        bundle.putString(MediaStore.Images.ImageColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
        bundle.putString(MediaStore.Images.ImageColumns.DISPLAY_NAME, "helios-icon.JPG");
        String [] projection = { MediaStore.Images.ImageColumns._ID, MediaStore.Images.ImageColumns.DISPLAY_NAME };
        Cursor cursor = contentResolver.query(uri, projection, bundle, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME);
                String displayName = cursor.getString(columnIndex);
                if (displayName.equals("Screenshot_2022-03-23-11-11-53-92.jpg")) {
                    // Android R 提供的 API 将得到的 id 转换成对应的 uri
                    Uri imageId = ContentUris.withAppendedId(uri, cursor.getLong(0));
                    showImages.setImageURI(imageId);
                }
                Log.d("SwanHostImpl", "index: " + columnIndex + ", 文件名字：" + displayName);
            }
            cursor.close();
        } else {
            queryOtherImages.setText("查询失败");
        }
    }

    private static final int REQUEST_CODE_FOR_OPEN_FILE = 0X12;
    /**
     * 使用分区存储的应用对自己创建的文件始终拥有读/写权限，无论文件是否位于应用的私有目录内。因此，如果您的应用仅保存和访问自己创建的文件，则无需请求获得
     * READ_EXTERNAL_STORAGE 或 WRITE_EXTERNAL_STORAGE 权限
     *
     * 在分区存储模型下
     * 此时禁止存储权限，使用 MediaStorage 获取sd卡上的 Pictures 目录，并修改自己应用创建的图片
     * @param view view
     */
    public void updateYouSelfImagesByMS(View view) {
        try {
            // 以修改名字为例子
            Uri uri = MediaStore.Images.Media.getContentUri("external");
            ContentResolver contentResolver = getContentResolver();
            String selection = MediaStore.Images.ImageColumns.DISPLAY_NAME+ "=?";
            String [] arg = { fileName };
            String [] projection = { MediaStore.Images.ImageColumns._ID };
            Cursor cursor = contentResolver.query(uri, projection, selection, arg, "");
            if (cursor != null) {
                // Android R 提供的 API 将得到的 id 转换成对应的 uri
                cursor.moveToFirst();
                Uri imageId = ContentUris.withAppendedId(uri, cursor.getLong(0));
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.Images.ImageColumns.DISPLAY_NAME, "mars_cout.jpg");
                int update = contentResolver.update(imageId, contentValues, null, null);
                if (update > 0) {
                    selection = MediaStore.Images.ImageColumns.DISPLAY_NAME+ "=?";
                    arg = new String[] { "mars_cout.jpg" };
                    projection = new String[] { MediaStore.Images.ImageColumns._ID };
                    Cursor query = contentResolver.query(uri, projection, selection, arg, null);
                    while (query.moveToNext()) {
                        int columnIndex = query.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME);
                        String displayName = query.getString(columnIndex);
                        updateYouSelfImages.setText(displayName);
                        query.close();
                    }
                }
                cursor.close();
            }
        } catch (Exception e) {
            // 使用 SAF 进行 update 操作
            // 通过系统的文件浏览器选择一个文件
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            // 筛选，只显示可以“打开”的结果，如文件(而不是联系人或时区列表)
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_CODE_FOR_OPEN_FILE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_FOR_OPEN_FILE) {
            if (data != null) {
                Uri mMediaUri = data.getData();
                ContentResolver contentResolver = getContentResolver();
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.Images.ImageColumns.DISPLAY_NAME, "mars_cout.jpg");
                int update = contentResolver.update(mMediaUri, contentValues, null, null);
                if (update > 0) {
                    String selection = MediaStore.Images.ImageColumns.DISPLAY_NAME+ "=?";
                    String [] arg = { "mars_cout.jpg" };
                    String [] projection = { MediaStore.Images.ImageColumns._ID };
                    Cursor query = contentResolver.query(mMediaUri, projection, selection, arg, null);
                    while (query.moveToNext()) {
                        int columnIndex = query.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME);
                        String displayName = query.getString(columnIndex);
                        updateYouSelfImages.setText(displayName);
                    }
                    query.close();
                }
            }

        }
    }

    /**
     * 使用分区存储的应用对自己创建的文件始终拥有读/写权限，无论文件是否位于应用的私有目录内。因此，如果您的应用仅保存和访问自己创建的文件，则无需请求获得
     * READ_EXTERNAL_STORAGE 或 WRITE_EXTERNAL_STORAGE 权限
     *
     * 在分区存储模型下
     * 此时禁止存储权限，使用 SAF 获取sd卡上的 Pictures 目录，并修改自己应用创建的图片
     * @param view view
     */
    public void updateYouSelfImagesBySAF(View view) {
        // 使用 SAF 进行 update 操作
        // 通过系统的文件浏览器选择一个文件
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        // 筛选，只显示可以“打开”的结果，如文件(而不是联系人或时区列表)
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_FOR_OPEN_FILE);
    }

    /**
     * 使用分区存储的应用对自己创建的文件始终拥有读/写权限，无论文件是否位于应用的私有目录内。因此，如果您的应用仅保存和访问自己创建的文件，则无需请求获得
     * READ_EXTERNAL_STORAGE 或 WRITE_EXTERNAL_STORAGE 权限
     *
     * 在分区存储模型下
     * 此时禁止存储权限，使用 MediaStorage 获取sd卡上的 Pictures 目录，并删除自己应用创建的图片
     * 删除的时候 也是先执行查询操作，用查询返回的 uri 进行删除
     * @param view view
     */
    public void deleteYouSelfImages(View view) {
        Uri uri = MediaStore.Images.Media.getContentUri("external");
        ContentResolver contentResolver = getContentResolver();
        String selection = MediaStore.Images.ImageColumns.DISPLAY_NAME+ "=?";
        String [] arg = { "mars_cout.jpg" };
        String [] projection = { MediaStore.Images.ImageColumns._ID };
        Cursor cursor = contentResolver.query(uri, projection, selection, arg, "");
        if (cursor != null) {
            // Android R 提供的 API 将得到的 id 转换成对应的 uri
            cursor.moveToFirst();
            Uri imageId = ContentUris.withAppendedId(uri, cursor.getLong(0));
            int update = contentResolver.delete(imageId, null, null);
            if (update > 0) {
                Toast.makeText(ScopeStorageActivity.this, "jpg文件删除成功", Toast.LENGTH_LONG).show();
            }
            cursor.close();
        }
    }

}
