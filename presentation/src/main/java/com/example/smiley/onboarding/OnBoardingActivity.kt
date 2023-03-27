package com.example.smiley.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewParent
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.example.smiley.R
import com.example.smiley.databinding.ActivityOnBoardingBinding
import com.example.smiley.main.MainActivity

class OnBoardingActivity : AppCompatActivity() {

    private lateinit var bind: ActivityOnBoardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding)

        bind = DataBindingUtil.setContentView(this, R.layout.activity_on_boarding)
        setOnBoarding()
        addStartBtnClickEvent()
    }

    private fun setOnBoarding(){
        bind.viewPager.apply {
            adapter = OnBoardingAdapter(this@OnBoardingActivity, 3)
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            currentItem = 0
            offscreenPageLimit = 3

            bind.indicator.attachTo(this)
        }
    }

    private fun addStartBtnClickEvent(){
        bind.startBtn.setOnClickListener {
            Log.d("OnBoardingActivity", "버튼 클릭됨")
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent) //intent 에 명시된 액티비티로 이동
            finish() //현재 액티비티 종료
        }
    }
}