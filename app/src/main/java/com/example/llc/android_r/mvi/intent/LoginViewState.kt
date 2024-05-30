package com.example.llc.android_r.mvi.intent

data class LoginViewState(val userName: String= "", val password: String = "") {
    val isLoginEnable: Boolean
        get() = userName.isNotEmpty() && password.length >= 6

    val passwordTipVisible: Boolean
        get() = password.length in 1..5
}
