package com.example.llc.android_r.mvi.intent

/**
 * com.example.llc.android_r.mvi.intent.LoginViewAction
 * @author liulongchao
 * @since 2024/5/30
 */
sealed class LoginViewAction {

    data class UpdateUserName(val userName: String): LoginViewAction()

    data class UpdatePassword(val password: String): LoginViewAction()

    object Login: LoginViewAction()
}