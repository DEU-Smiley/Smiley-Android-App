package com.example.smiley.splash.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.domain.common.base.ResponseState
import com.example.domain.user.usecase.GetUserInfoUseCase
import com.example.domain.user.usecase.UserLoginUseCase
import com.example.smiley.App
import com.example.smiley.common.base.BaseStateFlowViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getUserInfoUserCase: GetUserInfoUseCase,
    private val userLoginUseCase: UserLoginUseCase
): BaseStateFlowViewModel<SplashActivityState>() {

    override fun initialState(): SplashActivityState {
        return SplashActivityState.Init
    }

    fun getUserInfo(){
        viewModelScope.launch(Dispatchers.IO) {
            getUserInfoUserCase().collect {userId ->
                if(userId == null) {
                    setState(SplashActivityState.FirstAccess)
                } else {
                    requestLogin(userId)
                }
            }
        }
    }

    private fun requestLogin(userId: String){
        viewModelScope.launch(Dispatchers.IO){
            userLoginUseCase(userId).collect { state ->
                when(state){
                    is ResponseState.Success -> {
                        App.user = state.data
                        setState(SplashActivityState.SuccessLoadUserId)
                    }
                    is ResponseState.Error -> {
                        if(state.error.code == "NEW_USER") setState(SplashActivityState.RequiredLogin)
                        else setState(SplashActivityState.Error(state.error.message))
                    }
                }
            }
        }
    }
}

sealed class SplashActivityState {
    object Init : SplashActivityState()
    object FirstAccess : SplashActivityState()
    object SuccessLoadUserId : SplashActivityState()
    object RequiredLogin : SplashActivityState()
    data class Error(val message: String): SplashActivityState()
}