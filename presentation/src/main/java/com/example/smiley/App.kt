package com.example.smiley

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        _context = applicationContext
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
                Log.d("디바이스 토큰", deviceToken.toString())
            }
        }
    }

    companion object{
        @SuppressLint("StaticFieldLeak")
        private var _context: Context? = null
        private var deviceToken: String? = null

        @JvmStatic
        fun ApplicationContext(): Context {
            return _context!!
        }
    }
}