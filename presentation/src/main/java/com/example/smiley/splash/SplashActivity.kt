package com.example.smiley.splash

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.smiley.R
import com.example.smiley.common.extension.changeActivity
import com.example.smiley.common.extension.showGenericAlertDialog
import com.example.smiley.login.LoginActivity
import com.example.smiley.main.MainActivity
import com.example.smiley.onboarding.OnBoardingActivity
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.*

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        splashLogo(3)
    }

    private fun splashLogo(sec:Long){
        Handler().postDelayed(Runnable {
            checkKakaoAccessToken()
        }, 1000L * sec)
    }


    /**
     * 카카오 토큰이 있는지 검증하는 메소드
     */
    private fun checkKakaoAccessToken(){
        // 토큰이 이미 있는 경우
        if(AuthApiClient.instance.hasToken()) {
            UserApiClient.instance.accessTokenInfo{ _, error ->
                if(error == null) { // 토큰 유효성 체크 성공 (필요시 토큰 갱신)
                    changeActivity(MainActivity::class.java)
                    return@accessTokenInfo
                } else if (error !is KakaoSdkError || !error.isInvalidTokenError()){
                    this.showGenericAlertDialog("사용자 검증에 실패하였습니다. 로그인 화면으로 이동합니다.")
                }
                changeActivity(OnBoardingActivity::class.java)
            }
        } else { // 로그인 필요
            changeActivity(OnBoardingActivity::class.java)
        }
    }

}