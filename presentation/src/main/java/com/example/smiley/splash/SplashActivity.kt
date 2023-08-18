package com.example.smiley.splash

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.smiley.R
import com.example.smiley.common.extension.changeActivity
import com.example.smiley.common.extension.repeatOnStarted
import com.example.smiley.common.extension.showConfirmDialog
import com.example.smiley.main.MainActivity
import com.example.smiley.onboarding.OnBoardingActivity
import com.example.smiley.splash.viewmodel.SplashActivityState
import com.example.smiley.splash.viewmodel.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private val splashVm: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        observe()
        splashLogo(2)
    }

    private fun splashLogo(sec:Long){
        Handler().postDelayed(Runnable {
            splashVm.getUserInfo()
        }, 1000L * sec)
    }

    private fun observe(){
        repeatOnStarted {
            splashVm.state.collect { state ->
                when (state) {
                    is SplashActivityState.Init -> Unit
                    is SplashActivityState.FirstAccess,
                    SplashActivityState.RequiredLogin -> {
                        changeActivity(OnBoardingActivity::class.java)
                    }
                    is SplashActivityState.SuccessLoadUserId -> {
                        changeActivity(MainActivity::class.java)
                    }
                    is SplashActivityState.Error -> {
                        showConfirmDialog("로그인 에러", state.message)
                    }
                }
            }
        }
    }
}