package com.example.llc.kotlin;

import android.text.TextUtils

fun main() {

    /**
     *  只能声明，不能调用，调用报错，因为没有实现函数体
     * */
    var method1: () -> Unit

    // 声明类型的高阶函数
    val method6: (number1: Int, number2: Int) -> Int = { number1, number2 ->  number1 + number2 }
    println("method6: ${method6(100, 200)}")

    // 自动推导出类型
    val method7 = { number1: Int, number2: Int -> "result: ${number1 - number2}" }
    println("method7: ${method7(100, 200)}")

    //
    val m08 = {
        println("this is m08")
    }
    m08()

    //
    val m09 : (num1: Int, num2: Int) -> Int = { num1, num2 ->
        println("num1: $num1, num2: $num2")
        num1 + num2
    }
    println("m09: ${m09(100, 200)}")


    val m10 = {sex: Char -> if (sex == 'M') "男" else "女"}
    println(m10('M'))


}

typealias RequestLogin = (userName: String, userPwd: String) -> Unit

private fun loginService(userName: String, userPwd: String, requestLogin: (userName: String, userPwd: String) -> Unit) {
    if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(userPwd)) {
        requestLogin(userName, userPwd)
    }
}

// 使用别名形式
private fun loginService2(userName: String, userPwd: String, requestLogin: RequestLogin) {
    if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(userPwd)) {
        requestLogin(userName, userPwd)
    }
}

// 对外暴露
fun loginEngine(userName: String, userPwd: String) {
    loginService(userName, userPwd) { name, pwd ->
        if (name == "Mars" && pwd == "123456") {
            println("$name login success")
        } else {
            println("login fail")
        }
    }
}
