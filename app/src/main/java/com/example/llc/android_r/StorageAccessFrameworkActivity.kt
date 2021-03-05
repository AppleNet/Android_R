package com.example.llc.android_r

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_storage_access_framework.*
import java.io.BufferedOutputStream
import java.io.FileDescriptor
import java.io.FileOutputStream


/**
 * com.example.llc.android_r.StorageAccessFrameworkActivity
 * @author liulongchao
 * @since 2021/3/4
 */
class StorageAccessFrameworkActivity: AppCompatActivity() {

    private val REQUEST_CODE_FOR_OPEN_FILE = 0x12
    private val REQUEST_CODE_CREATE_FILE = 0x13
    private val IMAGE_PROJECTION = arrayOf(
        MediaStore.Images.Media.DISPLAY_NAME,
        MediaStore.Images.Media.SIZE,
        MediaStore.Images.Media._ID)

    private var mCreateUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_storage_access_framework)

        // 打开文件选择器
        safStorageOpenFile.setOnClickListener {
            // 通过系统的文件浏览器选择一个文件
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            // 筛选，只显示可以“打开”的结果，如文件(而不是联系人或时区列表)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            // 过滤只显示图像类型文件
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_CODE_FOR_OPEN_FILE)
        }

        // 创建文件，使用 SAF 只能创建在 download 目录下
        safStorageCreateFile.setOnClickListener {
            // 只能创建在 sd 卡 download 目录下
            val intent = Intent(Intent.ACTION_CREATE_DOCUMENT)
            // 筛选，只显示可以“打开”的结果，如文件(而不是联系人或时区列表)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TITLE, "llc.txt")
            startActivityForResult(intent, REQUEST_CODE_CREATE_FILE)
        }

        // 修改文件
        safStorageAlertFile.setOnClickListener {
            mCreateUri?.let {
                val pfd = contentResolver.openFileDescriptor(it, "w")
                val fileOutputStream = FileOutputStream(pfd?.fileDescriptor)
                val content = "Welcome to CBA where amazing happens!!!!"
                val bos = BufferedOutputStream(fileOutputStream)
                bos.write(content.toByteArray())
                bos.close()
                Toast.makeText(this, "数据修改成功： $it", Toast.LENGTH_LONG).show()
            }
        }

        // 删除文件
        safStorageDeleteFile.setOnClickListener {
            mCreateUri?.let {
                val deleteDocument = DocumentsContract.deleteDocument(contentResolver, it)
                if (deleteDocument) {
                    Toast.makeText(this, "数据删除成功： $it", Toast.LENGTH_LONG).show()
                }
            }
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when(requestCode) {
                REQUEST_CODE_FOR_OPEN_FILE -> {
                    val uri = data?.data
                    uri?.let {
                        val cursor = contentResolver.query(it, IMAGE_PROJECTION, null, null, null, null)
                        cursor?.let {
                            if (cursor.moveToFirst()) {
                                val displayName = cursor.getString(cursor.getColumnIndexOrThrow(IMAGE_PROJECTION[0]))
                                Toast.makeText(this, displayName, Toast.LENGTH_LONG).show()
                            }
                            // 显示图片
                            val parcelFileDescriptor = contentResolver.openFileDescriptor(uri, "r")
                            parcelFileDescriptor?.let {
                                val fileDescriptor: FileDescriptor = parcelFileDescriptor.fileDescriptor
                                val image = BitmapFactory.decodeFileDescriptor(fileDescriptor)
                                parcelFileDescriptor.close()
                                imageView.visibility = View.VISIBLE
                                imageView.setImageBitmap(image)
                            }

                            cursor.close()
                        }

                    }
                }
                //
                REQUEST_CODE_CREATE_FILE -> {
                    mCreateUri = data?.data
                    mCreateUri?.let {
                        val content = "Welcome to NBA where amazing happens"
                        val outputStream = contentResolver.openOutputStream(it)
                        val bos = BufferedOutputStream(outputStream)
                        bos.write(content.toByteArray())
                        bos.close()
                        Toast.makeText(this, "数据写入成功： ${it}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}