package com.example.llc.android_r.mvi

import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.llc.android_r.R
import com.example.llc.android_r.mvi.intent.LoginViewAction
import com.example.llc.android_r.mvi.intent.LoginViewEvent
import com.example.llc.android_r.mvi.intent.LoginViewState
import com.example.llc.android_r.mvi.model.LoginViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * com.example.llc.android_r.mvi.LoginActivity
 * @author liulongchao
 * @since 2024/5/30
 */
class LoginActivity: AppCompatActivity() {

    private val userName: EditText by lazy { findViewById(R.id.userName) }
    private val passWord: EditText by lazy { findViewById(R.id.passWord) }
    private val login: Button by lazy { findViewById(R.id.login) }
    private val pwdTips: TextView by lazy { findViewById(R.id.pwdTips) }
    private val viewModel: LoginViewModel = LoginViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initView()
        initViewStates()
        initViewEvents()
    }

    private fun initView() {
        userName.addTextChangedListener {
            viewModel.dispatch(LoginViewAction.UpdateUserName(userName = it.toString()))
        }
        passWord.addTextChangedListener {
            viewModel.dispatch(LoginViewAction.UpdatePassword(password = it.toString()))
        }
        login.setOnClickListener {
            viewModel.dispatch(LoginViewAction.Login)
        }
    }

    private fun initViewStates() {
        viewModel.viewStates.let { states ->
            states.observeState(this, LoginViewState::userName) {
                userName.setText(it)
                userName.setSelection(it.length)
            }
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    states.map {
                        it.userName
                    }.distinctUntilChanged().collect{
                        userName.setText(it)
                        userName.setSelection(it.length)
                    }
                }
            }
            states.observeState(this, LoginViewState::password){
                passWord.setText(it)
                passWord.setSelection(it.length)
            }
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    states.map {
                        it.password
                    }.distinctUntilChanged().collect{
                        passWord.setText(it)
                        passWord.setSelection(it.length)
                    }
                }
            }

            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    states.map {
                        it.isLoginEnable
                    }.distinctUntilChanged().collect {
                        login.isEnabled = it
                        login.alpha = if (it) 1f else 0.5f
                    }
                }
            }

            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    states.map {
                        it.passwordTipVisible
                    }.distinctUntilChanged().collect {
                        pwdTips.visibility = if (it) { View.VISIBLE } else { View.GONE }
                    }
                }
            }
        }
    }

    private fun initViewEvents() {
        viewModel.viewEvents.let {
            lifecycleScope.launchWhenStarted {
                it.collect {
                    when(it) {
                        is LoginViewEvent.ShowLoadingDialog -> {
                            showLoadingDialog()
                        }
                        is LoginViewEvent.DismissLoadingDialog -> {
                            dismissLoadingDialog()
                        }
                        is LoginViewEvent.ShowToast -> {
                            Toast.makeText(this@LoginActivity, it.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private var progressDialog: ProgressDialog? = null

    private fun showLoadingDialog() {
        if (progressDialog == null)
            progressDialog = ProgressDialog(this)
        progressDialog?.show()
    }

    private fun dismissLoadingDialog() {
        progressDialog?.takeIf { it.isShowing }?.dismiss()
    }
}