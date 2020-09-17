package com.example.llc.kotlin.core;

private val name = "Kobe"
private val age = 42
private val sex = 'M'

fun main() {

    name.apply()
    age.apply()
    sex.apply()

    val length = name.apply {
        it + "B"
    }.apply {
        it + "C"
    }.apply {
        it + "D"
    }.length

    println("length: $length")

    age.apply {
        it
    }

    sex.apply{
        it
    }

    name.let2 {

    }
}

fun <T> T.apply() {

}

//
fun <T> T.apply(mm: (T) -> T): T {
    // T == this
    return this
}

//
fun <T> T.also(mm: (T) -> Unit): T {
    // T == this
    mm(this)
    return this
}

fun <T, R> T.let(mm: (T) -> R): R {
    return mm(this)
}

// 不需要it 给 T 增加匿名扩展函数，即可将 it 去掉改成 this
fun <T, R> T.let2(mm: T.(T) -> R): R = mm(this)










