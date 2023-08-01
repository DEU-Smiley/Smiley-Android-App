package com.example.smiley.splash.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.common.base.ResponseState
import com.example.domain.user.usecase.GetUserInfoUseCase
import com.example.domain.user.usecase.UserLoginUseCase
import com.example.smiley.App
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getUserInfoUserCase: GetUserInfoUseCase,
    private val userLoginUseCase: UserLoginUseCase
): ViewModel() {
    private val _state = MutableStateFlow<SplashActivityState>(SplashActivityState.Init)
    val state: StateFlow<SplashActivityState> get() = _state

    private fun setFirstAccess(){
        _state.value = SplashActivityState.FirstAccess
    }

    private fun setSuccessLoadUserId(){
        _state.value = SplashActivityState.SuccessLoadUserId
    }

    private fun setError(message: String){
        _state.value = SplashActivityState.Error(message)
    }

    fun getUserInfo(){
        viewModelScope.launch(Dispatchers.IO) {
            getUserInfoUserCase().collect {userId ->
                if(userId == null) {
                    setFirstAccess()
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
                        setSuccessLoadUserId()
                    }
                    is ResponseState.Error -> {
                        setError(state.error.message)
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
    data class Error(val message: String): SplashActivityState()
}