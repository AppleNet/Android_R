package com.example.llc.kotlin;

fun main(){

    login("Mars", "123456") {
        if (it) {
            println("login success")
        } else {
            println("login fail")
        }
    }
}

private fun login(userName: String, userPwd: String, response: (Boolean)-> Unit) {
    loginEngine(userName, userPwd, response)
}

private fun loginEngine(userName: String, userPwd: String, response: (Boolean)-> Unit) {

    if (userName == "Mars" && userPwd == "123456") {
        // TODO
        response(true)
    } else {
        // TODO
        response(false)
    }
}
