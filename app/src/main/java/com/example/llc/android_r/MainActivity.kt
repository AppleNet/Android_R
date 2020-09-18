package com.example.llc.android_r

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentUris
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.llc.database.db.StudentDb
import com.example.llc.screen.NotchManager
import com.example.llc.screen.OnNotchCallBack
import com.example.llc.storage.sanbox.FileAccessFactory
import com.example.llc.storage.sanbox.coy.CopyRequest
import com.example.llc.storage.sanbox.file.FileRequest
import com.example.llc.storage.sanbox.image.ImageRequest

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity(), OnNotchCallBack {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        checkPermission(this)
        NotchManager.getInstance().setOnNotchListener(window, this)
        StudentDb.getDataBase(this)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNotchPortraitCallback(margin: Int) {
        // TODO 设置控件的 margin -> topMargin  = margin

    }

    override fun onNotchLandscapeCallback(margin: Int) {
        // TODO 判断水平方向是 270 度 还是 90
        // TODO leftMargin or rightMargin
    }


    private fun checkPermission(activity: Activity): Boolean {
        if (activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
            activity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            activity.requestPermissions(
                Array(2) { Manifest.permission.WRITE_EXTERNAL_STORAGE; Manifest.permission.READ_EXTERNAL_STORAGE },
                1
            )
        } else {
            createFile()
        }
        return false
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            1 ->{
                if(grantResults.isNotEmpty() && grantResults[0]  == PackageManager.PERMISSION_GRANTED) {
                    createFile()
                } else {
                    Toast.makeText(this, "You denied STORAGE permission", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun createFile() {
        val file = File(Environment.getExternalStorageDirectory(), "mars.txt")
        file.createNewFile()
    }

    /**
     *  在 Android R 中创建普通文件夹
     *
     * */
    fun createSAF(view: View) {

        val uri = MediaStore.Files.getContentUri("external")
        val contentResolver = contentResolver
        val contentValues = ContentValues()
        val path = Environment.DIRECTORY_DOWNLOADS + "/Mars"
        contentValues.put(MediaStore.Downloads.RELATIVE_PATH, path)
        contentValues.put(MediaStore.Downloads.DISPLAY_NAME, path)
        contentValues.put(MediaStore.Downloads.TITLE, path)

        val result: Uri? = contentResolver.insert(uri, contentValues)

        if (result == null) {
            // 失败
            Toast.makeText(view.context, "文件夹创建成功", Toast.LENGTH_LONG).show()
        } else {
            // 成功
            Toast.makeText(view.context, "文件夹创建失败", Toast.LENGTH_LONG).show()
        }
    }

    private var imageUri: Uri? = null

    /**
     *  在 Android R 中创建图片(文件)
     *
     *   并且可以在系统中找到
     * */
    fun insertImage(view: View) {

        val displayName = "mars.jpg"
        val values = ContentValues()
        values.put(MediaStore.Images.ImageColumns.DISPLAY_NAME, displayName)
        values.put(MediaStore.Images.ImageColumns.MIME_TYPE, "image/jpeg")
        values.put(MediaStore.Images.ImageColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/nba")
        imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        val outputStream = imageUri?.let {
            contentResolver.openOutputStream(it)
        }
        val bitmap = BitmapFactory.decodeResource(resources, R.mipmap.comment_like_24_24)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream?.close()
        //
        Toast.makeText(view.context, "insert success!", Toast.LENGTH_LONG).show()
    }

    /**
     *  在 Android R 中查询图片(文件)
     *
     * */
    fun query(view: View) {

        val external = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val selection = MediaStore.Images.Media.DISPLAY_NAME+ "=?"
        val args = Array(1){"mars.jpg"}
        val projection = Array(1){MediaStore.Images.Media._ID}
        // 数据库查询
        val cursor = contentResolver.query(external, projection, selection, args, null)
        if(cursor != null && cursor.moveToFirst()) {
            // Android R 提供的 API 将得到的 id 转换成对应的 uri
            val queryUri = ContentUris.withAppendedId(external, cursor.getLong(0))
            Toast.makeText(view.context, "query success! $queryUri", Toast.LENGTH_LONG).show()
            cursor.close()
        }
    }

    /**
     *  在 Android R 中修改图片(文件)
     *
     * */
    fun update(view: View) {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.DISPLAY_NAME, "mars_cout.jpg")
        imageUri?.let {
            // Android R中的所有的文件操作 都需要通过 uri 来操作
            // 查询到具体的 uri 再进行删除，修改
            val update = contentResolver.update(it, values, null, null)
            if (update > 0) {
                Toast.makeText(view.context, "update success", Toast.LENGTH_LONG).show()
            }
        }
    }

    /**
     *  在 Android R 中删除图片(文件)
     *
     * */
    fun delete(view: View) {
        val external = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val selection = MediaStore.Images.Media.DISPLAY_NAME+ "=?"
        val args = Array(1){"mars_cout.jpg"}
        val delete = contentResolver.delete(external, selection, args)
        if (delete > 0) {
            Toast.makeText(view.context, "delete success", Toast.LENGTH_LONG).show()
        }
    }

    /**
     *  在 Android R 中下载图片(文件)
     *
     * */
    @SuppressLint("NewApi")
    fun download(view: View) {

        Toast.makeText(this, "开始下载", Toast.LENGTH_LONG).show()
        thread {
            val url = URL("https://down.qq.com/qqweb/QQlite/Android_apk/qqlite_4.0.1.1060_537064364.apk")
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            val inputStream = connection.inputStream
            val bis = BufferedInputStream(inputStream)

            val values = ContentValues()
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, "test_qq.apk")
            // 必须在 download 目录
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + "/ExternalScopeTestApp/")
            val insertUri = contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)

            // uri ===> outputStream
            val outputStream = insertUri?.let {
                contentResolver.openOutputStream(it)
            }
            val bos = outputStream?.let {
                BufferedOutputStream(it)
            }
            val buffer = ByteArray(1024)
            var bytes = bis.read(buffer)
            while (bytes >= 0) {
                bos?.write(buffer, 0, bytes)
                bos?.flush()
                bytes = bis.read(buffer)
            }

            bos?.close()
            bis.close()

            runOnUiThread {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                intent.setDataAndType(insertUri, "application/vnd.android.package-archive")
                startActivity(intent)
                Toast.makeText(view.context, "FLAG_GRANT_READ_URI_PERMISSION", Toast.LENGTH_LONG).show()
            }
        }

    }

    /**
     *  使用架构创建文件夹
     *
     * */
    fun create(view: View) {
        // 创建一个文件夹
        val baseRequest = FileRequest(File("Mars"))
        baseRequest.path = "Mars"
        val response = FileAccessFactory.getIFile(baseRequest).newCreateFile(this, baseRequest)
        if (response.isSuccess) {
            Toast.makeText(view.context, "使用架构创建文件夹成功", Toast.LENGTH_LONG).show()
        }
    }

    /**
     *  使用架构创建文件
     *
     * */
    fun createFile(view: View) {
        val baseRequest = FileRequest(File("ExternalScopeTestApp/"))
        baseRequest.displayName = "test.txt"
        val fileResponse = FileAccessFactory.getIFile(baseRequest).newCreateFile(this, baseRequest)
        if (fileResponse.isSuccess) {
            val data = "Welcome to NBA where amazing happens"
            val outputStream = contentResolver.openOutputStream(fileResponse.uri)
            val bos = BufferedOutputStream(outputStream)
            bos.write(data.toByteArray())
            bos.close()
            Toast.makeText(view.context, "数据写入成功： ${fileResponse.uri}", Toast.LENGTH_LONG).show()
        }
    }

    /**
     *  使用架构创建图片
     *
     * */
    fun createImage(view: View) {
        val imageRequest = ImageRequest(File("mars.jpg"))
        imageRequest.path = "Mars"
        imageRequest.displayName = "mars.jpg"
        imageRequest.mimeType = "image/jpeg"
        val response = FileAccessFactory.getIFile(imageRequest).newCreateFile(this, imageRequest)
        if (response.isSuccess) {
            val outputStream = response.uri?.let {
                contentResolver.openOutputStream(it)
            }
            val bitmap = BitmapFactory.decodeResource(resources, R.mipmap.comment_like_24_24)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream?.close()
            Toast.makeText(view.context, "insert success!", Toast.LENGTH_LONG).show()
        }
    }

    /**
     *  使用架构查询图片
     *
     * */
    fun queryImage(view: View) {
        val baseRequest = ImageRequest(File("mars.jpg"))
        baseRequest.displayName = "mars.jpg"
        val response = FileAccessFactory.getIFile(baseRequest).query(this, baseRequest)
        if(response.isSuccess) {
            Toast.makeText(view.context, "查询成功：" + response.uri, Toast.LENGTH_LONG).show()
        }
    }

    /**
     *  使用架构删除图片
     *
     * */
    fun deleteImage(view: View) {
        // 先查询出来，再执行删除操作
        val baseRequest = ImageRequest(File("mars.jpg"))
        baseRequest.displayName = "mars.jpg"
        val response = FileAccessFactory.getIFile(baseRequest).delete(this, baseRequest)
        if(response.isSuccess) {
            Toast.makeText(view.context, "删除成功", Toast.LENGTH_LONG).show()
        }
    }

    /**
     *  使用架构更新图片
     *
     * */
    fun updateImage(view: View) {
        val where = ImageRequest(File("mars.jpg"))
        where.displayName = "mars.jpg"

        val item = ImageRequest(File("mars_cout.jpg"))
        item.displayName = "mars_cout.jpg"

        val fileResponse = FileAccessFactory.getIFile(where).renameTo(this, where, item)
        if (fileResponse.isSuccess) {
            Toast.makeText(view.context, "重命名成功", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(view.context, "重命名失败", Toast.LENGTH_LONG).show()
        }
    }

    /**
     *  使用架构复制文件
     * */
    fun copyImage(view: View) {
        val copyRequest = CopyRequest(File("test.txt"))
        copyRequest.displayName = "test.txt"
        copyRequest.distFile = File("Mars/test.txt")
        val fileResponse = FileAccessFactory.getIFile(copyRequest).copyFile(this, copyRequest)
        if (fileResponse.isSuccess) {
            Toast.makeText(view.context, "复制成功", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(view.context, "复制失败", Toast.LENGTH_LONG).show()
        }
    }

    suspend fun go() {

        // 通过 GlobalScope「非阻塞式」 声明一个协程「默认开始在 UI 线程」
        val job = GlobalScope.launch(Dispatchers.Main) {
            // 这样声明默认也是在 UI 线程

            val pro = ProgressDialog(this@MainActivity)
            pro.setMessage("正在注册中....")
            pro.show()

            withContext(Dispatchers.IO) {
                println("1. 注册耗时操作：${Thread.currentThread().name}")
                Thread.sleep(2000)
            }

            println("2.注册耗时操作完成，更新注册 UI：${Thread.currentThread().name}")
            pro.setMessage("注册成功，正在去登录...")

            withContext(Dispatchers.IO) {
                println("3. 登录耗时操作：${Thread.currentThread().name}")
                Thread.sleep(2000)
            }

            println("4.注册耗时操作完成，更新注册 UI：${Thread.currentThread().name}")
            pro.setMessage("登录成功")

            Thread.sleep(1000)
            pro.dismiss()
        }

        // 一点点的时间差，不是非常
        job.cancel()

        // 一点点的时间差都不允许
        job.cancelAndJoin()

    }

}
