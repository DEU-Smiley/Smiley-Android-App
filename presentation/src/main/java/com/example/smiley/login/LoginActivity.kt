package com.example.smiley.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.example.smiley.R
import com.example.smiley.common.extension.changeActivity
import com.example.smiley.common.extension.showGenericAlertDialog
import com.example.smiley.databinding.ActivityLoginBinding
import com.example.smiley.main.MainActivity
import com.example.smiley.permission.PermissionActivity
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApi
import com.kakao.sdk.user.UserApiClient

class LoginActivity : AppCompatActivity() {
    private lateinit var bind:ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind = DataBindingUtil.setContentView(this, R.layout.activity_login)
        kakaoLogin()
    }

    private val mCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if(error != null){
            Log.e(TAG, "카카오 로그인 에러 $error")
            showGenericAlertDialog("로그인에 실패했습니다.")
        } else if (token != null) {
            Log.d(TAG, "카카오 로그인 성공 ${token.accessToken}")
            changeActivity(PermissionActivity::class.java)
        }
    }

    /**
     * 카카오 로그인 요청 메소드
     */
    private fun kakaoLogin(){
        bind.kakaoLoginBtn.setOnClickListener {
            // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오 계정으로 로그인
            if(UserApiClient.instance.isKakaoTalkLoginAvailable(this)){
                UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                    if(error != null) {
                        Log.e(TAG, "카카오 로그인 에러 $error")
                        // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우
                        // 의도적인 로그인 취소로 보고 카카오 계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                        if(error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                            return@loginWithKakaoTalk
                        }

                        // 카카오톡에 연결된 카카오 계정이 없는 경우, 카카오 계정으로 로그인 시도
                        UserApiClient.instance.loginWithKakaoAccount(this, callback = mCallback)
                    } else if (token != null) {
                        Log.d(TAG, "카카오 로그인 성공 ${token.accessToken}")
                        changeActivity(PermissionActivity::class.java)
                    }
                }
            } else {
                // 카카오톡이 설치되어 있지 않은 경우 이메일 로그인 방식으로 유도
                UserApiClient.instance.loginWithKakaoAccount(this, callback = mCallback)
            }
        }
    }

    companion object{
        const val TAG = "LoginActivity"
    }
}