package com.example.llc.kotlin.corountines;

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() {

    // 通过 runBlocking「阻塞式」 声明一个协程「默认开始在 UI 线程」
    val r = runBlocking {
        println("runBlocking threadName: ${Thread.currentThread().name}")
        // 通过 Dispatchers 将协程切换到 IO 线程
        launch(Dispatchers.IO) {
            println("launch threadName: ${Thread.currentThread().name}")
            repeat(10) {
                Thread.sleep(1000)
                println("计数中：$it")
            }

        }
    }


}
