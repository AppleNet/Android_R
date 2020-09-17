package com.example.llc.kotlin.core;

fun main() {

    runLog(9){
        println("执行了一次，下标是：$it")
    }
}

fun ktRun(start: Boolean = true, threadName: String = "MarsThread", runAction: () -> Unit): Thread {

    val thread = object : Thread(threadName){
        override fun run() {
            super.run()
            runAction()
        }
    }

    if (start) {
        thread.start()
    }
    return thread
}

fun runLog(counts: Int, mm:(Int)-> Unit) {
    for (index in 0 until counts) {
        mm(index)
    }
}