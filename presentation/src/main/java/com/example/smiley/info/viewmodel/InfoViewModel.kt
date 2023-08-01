package com.example.smiley.info.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.common.base.ResponseState
import com.example.domain.reservation.model.Reserv
import com.example.domain.user.model.User
import com.example.domain.user.usecase.UserSignUpUseCase
import com.example.smiley.App
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class InfoViewModel @Inject constructor(
    private val userLoginUseCase: UserSignUpUseCase,
    private val userSignUpUseCase: UserSignUpUseCase
): ViewModel() {
    private val _state = MutableStateFlow<SignUpFragmentState>(SignUpFragmentState.Init)
    val state:StateFlow<SignUpFragmentState> get() = _state

    private suspend fun setLoading() {
        _state.value = SignUpFragmentState.IsLoading
    }

    private suspend fun showToast(message: String){
        _state.value = SignUpFragmentState.ShowToast(message)
    }

    private suspend fun setSuccess(){
        _state.value = SignUpFragmentState.SuccessSignUp
    }

    private suspend fun setError(message: String){
        _state.value = SignUpFragmentState.Error(message)
    }


    fun signUp(userId: String, name: String, birth: LocalDate){
        viewModelScope.launch(Dispatchers.IO) {
            userSignUpUseCase(
                userId = userId,
                name = name,
                birthDate = birth.toString(),
                deviceToken = App.getDeviceToken()
            ).onStart {
                setLoading()
            }.catch { exception ->
                showToast(message = exception.message.toString())
                Log.e("InfoViewModel", exception.message.toString())
            }.collect { state ->
                when(state){
                    is ResponseState.Success -> {
                        App.user = state.data
                        setSuccess()
                    }
                    is ResponseState.Error -> {
                        setError(state.error.message)
                    }
                }
            }
        }
    }

    /**
     * 입력받은 사용자 정보를 서버로 전송하는 메소드
     */
//    fun sendUserInfoToServer(name: String, userId: String, birth: LocalDate) {
//        viewModelScope.launch(Dispatchers.IO) {
//            userLoginUseCase(
//                name = name,
//                userId = userId,
//                birthDate = birth.toString(),
//                deviceToken = App.getDeviceToken()
//            )
//                .onStart { setLoading() }
//                .catch { exception ->
//                    showToast(message = exception.message.toString())
//                    Log.e("InfoViewModel", exception.message.toString())
//                }
//                .onEach { state ->
//                    when (state) {
//                        is ResponseState.Success -> {
//                            App.user = state.data
//                            setSuccess(user = state.data)
//                        }
//                        is ResponseState.Error -> {
//                            setError(state.error.message)
//                        }
//                    }
//                }
//                .collect()
//        }
//    }
}

sealed class SignUpFragmentState {
    object Init : SignUpFragmentState()
    object IsLoading  : SignUpFragmentState()
    object SuccessSignUp : SignUpFragmentState()
    data class Error(val message: String)           : SignUpFragmentState()
    data class ShowToast(val message: String)       : SignUpFragmentState()
}