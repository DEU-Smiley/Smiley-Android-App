package com.example.smiley

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Build
import android.util.Log
import com.example.domain.user.model.User
import com.example.smiley.common.utils.NotifyManager
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate() {
        super.onCreate()
        context = applicationContext

        // Kakao Sdk 초기화
        KakaoSdk.init(this, BuildConfig.KAKAO_SDK_APPKEY)

        // Firebase 인스턴스 초기화
        FirebaseApp.initializeApp(this.applicationContext)
        firebaseAnalytics = Firebase.analytics

        getDeviceToken()
        createNoficationChannel()
    }

    private fun getDeviceToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if(task.isSuccessful){
                deviceToken = task.result
                Log.d("디바이스 토큰", deviceToken)
            }
        }
    }

    private fun createNoficationChannel(){
        NotifyManager.createNotificationChannel(
            applicationContext,
            NotifyManager.WEARING_NOTIFY_ID,
            "교정기 착용 알림"
        )
    }

    companion object{
        @SuppressLint("StaticFieldLeak")
        private var context: Context? = null
        private var deviceToken: String=""
        var user: User?=null

        val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.BLUETOOTH_ADVERTISE,
                Manifest.permission.POST_NOTIFICATIONS
            )
        } else {
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
            )
        }

        fun getDeviceToken() = deviceToken
        @JvmStatic
        fun ApplicationContext(): Context {
            return context!!
        }
    }
}