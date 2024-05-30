package com.example.llc.android_r.mvi.intent

/**
 * com.example.llc.android_r.mvi.intent.LoginViewEvent
 * @author liulongchao
 * @since 2024/5/30
 */
sealed class LoginViewEvent {

    data class ShowToast(val message: String): LoginViewEvent()
    object ShowLoadingDialog: LoginViewEvent()
    object DismissLoadingDialog: LoginViewEvent()
}