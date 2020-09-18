package com.example.llc.kotlin.generic.obj2;

// 这个类只能修改 不能获取
// 声明的时候加入泛型边界，类中所有的方法 都可以省略 一劳永逸
class Student<in T> {

    // 这样写 编译不过 只能修改 不能获取
    // fun a(list: MutableList<in T>){}

    fun setData(t: T) {

    }

    // 编译不过
//    fun getData(): T{
//
//    }


}
