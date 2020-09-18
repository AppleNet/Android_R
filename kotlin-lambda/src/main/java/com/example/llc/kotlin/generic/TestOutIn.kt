package com.example.llc.kotlin.generic;

import com.example.llc.kotlin.generic.obj.FuClass
import com.example.llc.kotlin.generic.obj.ZiClass

class TestOutIn {

    val fuClass = FuClass()
    val ziClass = ZiClass()

    private fun test01(){
        // out == extends  只能获取(out) 不能修改
        val list : MutableList<out FuClass> = ArrayList<ZiClass>()
        val clazz = list.get(0)

        // int == super 只能修改(int) 不能获取
        val list1 : MutableList<in ZiClass> = ArrayList<FuClass>()
        list1.add(ziClass)


    }
}
