package com.example.smiley.onboarding

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.example.smiley.R
import com.example.smiley.common.extension.changeActivity
import com.example.smiley.databinding.ActivityOnBoardingBinding
import com.example.smiley.login.LoginActivity
import com.example.smiley.onboarding.adapter.OnBoardingAdapter

class OnBoardingActivity : AppCompatActivity() {

    private lateinit var bind: ActivityOnBoardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
            changeActivity(LoginActivity::class.java)
        }
    }
}