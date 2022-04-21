package com.example.llc.android_r.coroutine

import android.app.ProgressDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.llc.android_r.R
import kotlinx.coroutines.*

/**
 * com.example.llc.android_r.coroutine.KotlinActivity
 * @author liulongchao
 * @since 2021/3/4
 */
class KotlinActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin)

    }

    suspend fun go() {

        // 通过 GlobalScope「非阻塞式」 声明一个协程「默认开始在 UI 线程」
        val job = GlobalScope.launch(Dispatchers.Main) {
            // 这样声明默认也是在 UI 线程

            val pro = ProgressDialog(this@KotlinActivity)
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