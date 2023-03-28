package com.example.smiley.common.extension

import android.app.Activity
import android.content.Intent
import com.example.smiley.onboarding.OnBoardingActivity


fun<T> Activity.changeActivity(activity:Class<T>) {
    val intent = Intent(this, activity)
    startActivity(intent) //intent 에 명시된 액티비티로 이동
    finish() //현재 액티비티 종료
}