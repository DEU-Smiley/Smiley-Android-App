package com.example.smiley.info.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.common.base.ResponseState
import com.example.domain.user.model.User
import com.example.domain.user.usecase.UserLoginUseCase
import com.example.smiley.App
import com.example.smiley.hospital.viewmodel.HospitalSearchFragmentState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class InfoViewModel @Inject constructor(
    private val userLoginUseCase: UserLoginUseCase
): ViewModel() {
    private val _state = MutableStateFlow<SignUpFragmentState>(SignUpFragmentState.Init)
    val state:StateFlow<SignUpFragmentState> get() = _state

    private suspend fun setLoading() {
        _state.emit(SignUpFragmentState.IsLoading)
    }

    private suspend fun showToast(message: String){
        _state.emit(SignUpFragmentState.ShowToast(message))
    }

    private suspend fun setSuccess(user: User){
        _state.emit(SignUpFragmentState.SuccessSendInfo(user))
    }

    private suspend fun setError(message: String){
        _state.emit(SignUpFragmentState.Error(message))
    }


    /**
     * 입력받은 사용자 정보를 서버로 전송하는 메소드
     */
    fun sendUserInfoToServer(name: String, userId: String, birth: LocalDate) {
        viewModelScope.launch(Dispatchers.IO) {
            userLoginUseCase(
                name = name,
                userId = userId,
                birthDate = birth.toString(),
                deviceToken = App.getDeviceToken()
            )
                .onStart { setLoading() }
                .catch { exception ->
                    showToast(message = exception.message.toString())
                    Log.e("InfoViewModel", exception.message.toString())
                }
                .onEach { state ->
                    when (state) {
                        is ResponseState.Success -> {
                            App.user = state.data
                            setSuccess(user = state.data)
                        }
                        is ResponseState.Error -> {
                            setError(state.error.message)
                        }
                    }
                }
                .collect()
        }
    }
}

sealed class SignUpFragmentState {
    object Init : SignUpFragmentState()
    object IsLoading  : SignUpFragmentState()
    data class SuccessSendInfo(val user: User)      : SignUpFragmentState()
    data class Error(val message: String)           : SignUpFragmentState()
    data class ShowToast(val message: String)       : SignUpFragmentState()
}