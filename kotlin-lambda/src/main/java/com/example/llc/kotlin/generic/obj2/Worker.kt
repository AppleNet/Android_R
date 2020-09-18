package com.example.llc.kotlin.generic.obj2;

class Worker<out T> {


    fun getData(): T? = null

    // 编译不过 只能读取 不能修改
    // fun setData(t: T){}
}
