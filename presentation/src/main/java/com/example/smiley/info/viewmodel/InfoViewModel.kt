package com.example.smiley.info.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.common.base.ResponseState
import com.example.domain.user.usecase.UserSignUpUseCase
import com.example.smiley.App
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class InfoViewModel @Inject constructor(
    private val userSignUpUseCase: UserSignUpUseCase
): ViewModel() {
    private val _state = MutableStateFlow<SignUpFragmentState>(SignUpFragmentState.Init)
    val state:StateFlow<SignUpFragmentState> get() = _state

    private fun setLoading() {
        _state.value = SignUpFragmentState.IsLoading
    }

    private fun showToast(message: String){
        _state.value = SignUpFragmentState.ShowToast(message)
    }

    private fun setSuccess(){
        _state.value = SignUpFragmentState.SuccessSignUp
    }

    private fun setError(message: String){
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
}

sealed class SignUpFragmentState {
    object Init : SignUpFragmentState()
    object IsLoading  : SignUpFragmentState()
    object SuccessSignUp : SignUpFragmentState()
    data class Error(val message: String)           : SignUpFragmentState()
    data class ShowToast(val message: String)       : SignUpFragmentState()
}