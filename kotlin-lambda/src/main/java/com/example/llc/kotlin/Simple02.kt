package com.example.llc.kotlin;

fun main() {

    common().login {

    }

    onRun(true){
        println("this is onrun work")
    }

    onRun(true, {
        println("this is onrun2 work")
    })

    val runValue = Runnable {
        println("this is runnable work")
    }

    onRun(true, runValue::run)
}

val name: String = "Mars"

// m : T.() -> R
// T.() 给 T 定义一个匿名函数
fun <T, R> T.login(m : T.() -> R): R{
    return m()
}

private fun <T, R> T.myWith(input: T, mm: T.() -> R) : R{
    return input.mm()
}

fun common() {
    println("this is common function")
}


fun onRun(isRun: Boolean, mm: () -> Unit) {
    if (isRun) {
        mm()
    }
}
