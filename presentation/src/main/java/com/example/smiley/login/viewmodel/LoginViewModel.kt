package com.example.smiley.login.viewmodel

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.example.smiley.common.extension.changeActivity
import com.example.smiley.common.extension.showGenericAlertDialog
import com.example.smiley.login.LoginActivity
import com.example.smiley.permission.PermissionActivity
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(): ViewModel() {
    @SuppressLint("StaticFieldLeak")
    private val state = MutableStateFlow<LoginActivityState>(LoginActivityState.Init)
    val mState: StateFlow<LoginActivityState> get() = state

    private val mCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if(error != null){
            Log.e(LoginActivity.TAG, "카카오 로그인 에러 $error")
            setErrorLogin("로그인에 실패했습니다.")
        } else if (token != null) {
            Log.d(LoginActivity.TAG, "카카오 로그인 성공 ${token.accessToken}")
            setSuccessLogin()
        }
    }

    private fun setSuccessLogin(){
        // 사용자 정보 요청 (기본)
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e("LoginViewModel", "사용자 정보 요청 실패", error)
            }
            else if (user != null) {
                Log.i("LoginViewModel", "사용자 정보 요청 성공" +
                        "\n회원번호: ${user.id}" +
                        "\n이메일: ${user.kakaoAccount?.email}" +
                        "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
                        "\n프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}" +
                        "\n생일: ${user.kakaoAccount?.birthday}"
                )
            }
        }
        state.value = LoginActivityState.SuccessLogin()
    }

    private fun setErrorLogin(message: String){
        state.value = LoginActivityState.ErrorLogin(message = message)
    }

    /**
     * 카카오 로그인 요청 메소드
     */
    fun kakaoLogin(activity:Activity){
            // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오 계정으로 로그인
            if(UserApiClient.instance.isKakaoTalkLoginAvailable(activity)){
                UserApiClient.instance.loginWithKakaoTalk(activity) { token, error ->
                    /**
                     * 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우
                     * 의도적인 로그인 취소로 보고 카카오 계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                     */
                    if(error != null) {
                        Log.e(LoginActivity.TAG, "카카오 로그인 에러 $error")
                        if(error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                            return@loginWithKakaoTalk
                        }

                        // 카카오톡에 연결된 카카오 계정이 없는 경우, 카카오 계정으로 로그인 시도
                        UserApiClient.instance.loginWithKakaoAccount(activity, callback = mCallback)
                    } else if (token != null) {
                        Log.d(LoginActivity.TAG, "카카오 로그인 성공 ${token.accessToken}")
                        setSuccessLogin()
                    }
                }
            } else {
                // 카카오톡이 설치되어 있지 않은 경우 이메일 로그인 방식으로 유도
                UserApiClient.instance.loginWithKakaoAccount(activity, callback = mCallback)
            }
        }
    }

    sealed class LoginActivityState {
        object Init             : LoginActivityState()
        class SuccessLogin()    : LoginActivityState()
        data class ErrorLogin(val message:String)      : LoginActivityState()
}