package com.example.smiley.splash

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.smiley.App
import com.example.smiley.R
import com.example.smiley.common.extension.changeActivity
import com.example.smiley.login.LoginActivity
import com.example.smiley.onboarding.OnBoardingActivity
import com.example.smiley.splash.viewmodel.SplashActivityState
import com.example.smiley.splash.viewmodel.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

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
            splashVm.checkFirstAccess()
        }, 1000L * sec)
    }

    private fun observe(){
        splashVm.state
            .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { state ->
                when (state) {
                    is SplashActivityState.Init -> Unit
                    is SplashActivityState.IsFirstAccess -> {
                        handleIsFirstAccess(state.flag)
                    }
                }
            }
            .launchIn(lifecycleScope)
    }

    private fun handleIsFirstAccess(flag: Boolean){
        changeActivity(
            // 최초 접속인 경우
            if(flag) OnBoardingActivity::class.java
            else LoginActivity::class.java
        )
    }
}