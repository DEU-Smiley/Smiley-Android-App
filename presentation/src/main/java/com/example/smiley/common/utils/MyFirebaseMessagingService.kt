package com.example.smiley.common.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.smiley.R
import com.example.smiley.login.LoginActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService: FirebaseMessagingService() {

    /**
     * FCM 서버에 앱이 등록되면 호출되는 메소드
     * 파라미터로 토큰 값이 전달 됨
     */
    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    /**
     * 클라우드로부터 메세지를 수신했을 때 동작하는 메소드
     * 수신된 메세지를 푸쉬 알림으로 띄우면 된다.
     */
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        if(message.data.isNotEmpty()){
            sendNotification(
                "${message.data["title"]}",
                "${message.data["body"]}"
            )
        } else {
            message.notification?.let {
                sendNotification(
                    "${it.title}",
                    "${it.body}"
                )
            }
        }
    }

    private fun sendNotification(title: String, body: String){
        val notifyId = (System.currentTimeMillis() / 7).toInt()

        val intent = Intent(this, LoginActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }

        val pendingIntent =
            PendingIntent.getActivity(this, notifyId, intent, PendingIntent.FLAG_IMMUTABLE)

        val channelId = getString(R.string.app_name)
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val largeIcon = BitmapFactory.decodeResource(resources, R.mipmap.app_icon)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setLargeIcon(largeIcon)
            .setSmallIcon(R.mipmap.app_icon)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationManagerCompat.IMPORTANCE_HIGH)
            .setAutoCancel(true)
            .setSound(soundUri)
            .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            channelId,
            channelId,
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)

        notificationManager.notify(notifyId, notificationBuilder.build())
    }
}