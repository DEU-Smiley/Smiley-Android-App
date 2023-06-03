package com.example.smiley.login.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.user.usecase.GetAccessFlagUseCase
import com.example.smiley.login.LoginActivity
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val getAccessFlagUseCase: GetAccessFlagUseCase
) : ViewModel() {
    @SuppressLint("StaticFieldLeak")
    private val _state = MutableStateFlow<LoginActivityState>(LoginActivityState.Init)
    val state: StateFlow<LoginActivityState> get() = _state

    private val mCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            setErrorLogin(error.message?:"카카오 로그인 에러")
        } else if (token != null) {
            Log.d(LoginActivity.TAG, "카카오 로그인 성공 ${token.accessToken}")
            setSuccessLogin()
        }
    }

    private fun setSuccessLogin(user:User, accessFlag:Boolean){
        _state.value = LoginActivityState.SuccessLogin(user, accessFlag)
    }
    private fun setErrorLogin(message: String) {
        _state.value = LoginActivityState.ErrorLogin(message = message)
    }


    private fun setSuccessLogin() {
        viewModelScope.launch(Dispatchers.IO) {
            // 최초 접속인지 먼저 확인
            getAccessFlagUseCase().collect{ accessFlag ->
                // 사용자 정보 요청 (기본)
                UserApiClient.instance.me { user, error ->
                    if (error != null) {
                        setErrorLogin(error.message?:"사용자 정보 요청 실패")
                    } else if (user != null) {
                        Log.i(
                            "LoginViewModel", "사용자 정보 요청 성공" +
                                    "\n회원번호: ${user.id}" +
                                    "\n이메일: ${user.kakaoAccount?.email}" +
                                    "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
                                    "\n프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}" +
                                    "\n생일: ${user.kakaoAccount?.birthday}"
                        )
                        setSuccessLogin(user, !accessFlag)
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
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                /**
                 * 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우
                 * 의도적인 로그인 취소로 보고 카카오 계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                 */
                if (error != null) {
                    setErrorLogin(error.message?:"카카오 로그인 에러")
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    // 카카오톡에 연결된 카카오 계정이 없는 경우, 카카오 계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(context, callback = mCallback)
                } else if (token != null) {
                    setSuccessLogin()
                }
            }
        } else {
            // 카카오톡이 설치되어 있지 않은 경우 이메일 로그인 방식으로 유도
            UserApiClient.instance.loginWithKakaoAccount(context, callback = mCallback)
        }
    }
}


sealed class LoginActivityState {
    object Init : LoginActivityState()
    data class SuccessLogin(val user: User, val isFirstAccess: Boolean) : LoginActivityState()
    data class ErrorLogin(val message: String) : LoginActivityState()
}