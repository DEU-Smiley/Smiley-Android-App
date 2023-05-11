package com.example.smiley

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import com.example.domain.user.model.User
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        catchAllError()


        // Kakao Sdk 초기화
        KakaoSdk.init(this, BuildConfig.KAKAO_SDK_APPKEY)

        // Firebase 인스턴스 초기화
        FirebaseApp.initializeApp(this.applicationContext)
        getDeviceToken()
    }

    /**
     * 앱이 강제종료 되어도 에러 로그를 출력할 수 있도록 함
     */
    private fun catchAllError(){
        try {
            Thread.setDefaultUncaughtExceptionHandler { _, ex -> ex.printStackTrace() }
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    private fun getDeviceToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if(task.isSuccessful){
                deviceToken = task.result
                Log.d("디바이스 토큰", deviceToken)
            }
        }
    }

    companion object{
        @SuppressLint("StaticFieldLeak")
        private var context: Context? = null
        private var deviceToken: String=""
        var user: User?=null

        fun getDeviceToken() = deviceToken
        @JvmStatic
        fun ApplicationContext(): Context {
            return context!!
        }
    }
}