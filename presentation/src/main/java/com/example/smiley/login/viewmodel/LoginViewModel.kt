package com.example.smiley.login.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.common.base.ResponseState
import com.example.domain.user.usecase.UserLoginUseCase
import com.example.smiley.App
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userLoginUseCase: UserLoginUseCase
) : ViewModel() {
    @SuppressLint("StaticFieldLeak")
    private val _state = MutableStateFlow<LoginActivityState>(LoginActivityState.Init)
    val state: StateFlow<LoginActivityState> get() = _state

    private val mCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            setError(error.message?:"카카오 로그인 에러")
        } else if (token != null) {
            loadUserInfo()
        }
    }

    private fun setSuccessLogin(){
        _state.value = LoginActivityState.SuccessLogin
    }

    private fun setRequiredSignUp(userId: String){
        _state.value = LoginActivityState.RequiredSignUp(userId)
    }

    private fun setError(message: String) {
        _state.value = LoginActivityState.Error(message = message)
    }

    private fun requestLogin(userId: String){
        viewModelScope.launch(Dispatchers.IO){
            userLoginUseCase(userId)
                .onStart { }
                .catch {
                    Log.d("로그인 뷰모델", "캐치로 옴 ${it}")
                }
                .collect { state ->
                    when (state) {
                        is ResponseState.Success -> {
                            App.user = state.data
                            setSuccessLogin()
                        }

                        is ResponseState.Error -> {
                            Log.d("로그인 뷰모델", "에러로 옴 ${state.error}")
                            if (state.error.code == "510") setRequiredSignUp(userId)
                            else setError(state.error.message)
                        }
                    }
                }
        }
    }

    /**
     * 카카오 로그인 요청 메소드
     */
    fun kakaoLogin(context: Context) {
        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오 계정으로 로그인
        with(UserApiClient.instance) {
            if (isKakaoTalkLoginAvailable(context)) {
                // 카카오톡으로 로그인이 가능한 경우
                loginWithKakaoTalk(context) { token, error ->
                    handleLoginResult(context, token, error)
                }
            } else {
                // 카카오톡이 설치되어 있지 않은 경우 이메일 로그인 방식으로 유도
                loginWithKakaoAccount(context, callback = mCallback)
            }
        }
    }

    /**
     * 카카오톡 로그인 핸들러
     */
    private fun handleLoginResult(context: Context, token: OAuthToken?, error: Throwable?) {
        if (error != null) {
            setError(error.message ?: "카카오 로그인 에러")
            if (error !is ClientError || error.reason != ClientErrorCause.Cancelled) {
                // 카카오톡에 연결된 카카오 계정이 없는 경우, 카카오 계정으로 로그인 시도
                UserApiClient.instance.loginWithKakaoAccount(context, callback = mCallback)
            }
        } else if (token != null) {
            loadUserInfo()
        }
    }

    /**
     * 카카오 로그인 정보 가져오는 메소드
     */
    private fun loadUserInfo() {
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                setError(error.message ?: "사용자 정보 요청 실패")
            } else if (user != null) {
                requestLogin(user.id.toString())
                logUserInfo(user)
            }
        }
    }

    private fun logUserInfo(user: User) {
        Log.i(
            "LoginViewModel", "사용자 정보 요청 성공" +
                    "\n회원번호: ${user.id}" +
                    "\n이메일: ${user.kakaoAccount?.email}" +
                    "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
                    "\n프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}" +
                    "\n생일: ${user.kakaoAccount?.birthday}"
        )
    }
}


sealed class LoginActivityState {
    object Init : LoginActivityState()
    object SuccessLogin : LoginActivityState()
    data class RequiredSignUp(val userId: String) : LoginActivityState()
    data class Error(val message: String) : LoginActivityState()
}