package com.example.smiley

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

/**
 * @HiltAndroidApp
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()

        catchAllError()

        //Kakao Sdk 초기화
        KakaoSdk.init(this, BuildConfig.KAKAO_SDK_APPKEY)
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
}