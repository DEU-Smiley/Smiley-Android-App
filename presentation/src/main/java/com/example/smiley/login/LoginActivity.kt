package com.example.smiley.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.smiley.R
import com.example.smiley.common.extension.changeActivity
import com.example.smiley.common.extension.showGenericAlertDialog
import com.example.smiley.databinding.ActivityLoginBinding
import com.example.smiley.login.viewmodel.LoginActivityState
import com.example.smiley.login.viewmodel.LoginViewModel
import com.example.smiley.permission.PermissionActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var bind:ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind = DataBindingUtil.setContentView(this, R.layout.activity_login)
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        observeLoginState()
        addLoginBtnClickEvent()
    }

    /**
     * 카카오 로그인 요청 메소드
     */
    private fun addLoginBtnClickEvent(){
        bind.kakaoLoginBtn.setOnClickListener {
            viewModel.kakaoLogin(this)
        }
    }

    private fun observeLoginState(){
        viewModel.mState
            .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { state -> handleLoginStateChange(state) }
            .launchIn(lifecycleScope)
    }

    private fun handleLoginStateChange(state: LoginActivityState){
        when(state){
            is LoginActivityState.Init -> Unit
            is LoginActivityState.SuccessLogin -> handleSuccessLogin()
            is LoginActivityState.ErrorLogin -> handleErrorLogin(state.message)
        }
    }

    private fun handleSuccessLogin(){
        changeActivity(PermissionActivity::class.java)
    }

    private fun handleErrorLogin(message:String){
        showGenericAlertDialog(message)
    }

    companion object{
        const val TAG = "LoginActivity"
    }
}