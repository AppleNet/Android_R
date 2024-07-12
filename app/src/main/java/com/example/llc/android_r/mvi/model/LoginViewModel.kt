package com.example.llc.android_r.mvi.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.llc.android_r.mvi.intent.LoginViewAction
import com.example.llc.android_r.mvi.intent.LoginViewEvent
import com.example.llc.android_r.mvi.intent.LoginViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.Exception

/**
 * com.example.llc.android_r.mvi.model.LoginViewModel
 * @author liulongchao
 * @since 2024/5/30
 */
class LoginViewModel: ViewModel() {

    private val _viewState =  MutableStateFlow(LoginViewState())
    val viewStates = _viewState.asStateFlow()

    private val _viewEvent = MutableSharedFlow<LoginViewEvent>()
    val viewEvents = _viewEvent.asSharedFlow()


    fun dispatch(loginViewAction: LoginViewAction) {
        when(loginViewAction) {
            is LoginViewAction.UpdateUserName -> {
                updateUserName(loginViewAction.userName)
            }

            is LoginViewAction.UpdatePassword -> {
                updatePassword(loginViewAction.password)
            }

            is LoginViewAction.Login -> {
                login()
            }
        }
    }

    private fun updateUserName(userName: String) {
//        _viewState.setState {
//            copy(userName = userName)
//        }
        _viewState.value = _viewState.value.copy(userName = userName)
    }

    private fun updatePassword(passWord: String) {
//        _viewState.setState {
//            copy(password = passWord)
//        }
        _viewState.value = _viewState.value.copy(password = passWord)
    }

    private fun login() {
        viewModelScope.launch(Dispatchers.Main) {
            flow {
                if (loginLogic()) {
                    emit("登录成功")
                } else {
                    emit("登录失败")
                }
            }.flowOn(Dispatchers.IO).onStart {
//                _viewEvent.setEvent(LoginViewEvent.ShowLoadingDialog)
                _viewEvent.emit(LoginViewEvent.ShowLoadingDialog)
            }.onEach {
//                _viewEvent.setEvent(LoginViewEvent.DismissLoadingDialog)
//                _viewEvent.setEvent(LoginViewEvent.ShowToast(it))
                _viewEvent.emit(LoginViewEvent.DismissLoadingDialog)
                _viewEvent.emit(LoginViewEvent.ShowToast(it))
            }.catch {
//                _viewState.setState {
//                    copy(password = "")
//                }
                _viewState.value = _viewState.value.copy(userName = "")
                _viewState.value = _viewState.value.copy(password = "")
                _viewEvent.emit(LoginViewEvent.DismissLoadingDialog)
                _viewEvent.emit(LoginViewEvent.ShowToast(it.message.toString()))
            }.collect()
        }
    }

    private suspend fun loginLogic(): Boolean {
        viewStates.value.let {
            // 执行 http 请求
            if (it.userName == "Kobe" && it.password == "123456") {
                delay(2000)
                return true
            } else {
                return false
            }
        }
    }
}