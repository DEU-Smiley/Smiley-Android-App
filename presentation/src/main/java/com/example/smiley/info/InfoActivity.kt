package com.example.smiley.info

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.example.smiley.R
import com.example.smiley.databinding.ActivityInfoBinding
import com.example.smiley.onboarding.adapter.OnBoardingAdapter

class InfoActivity : AppCompatActivity() {

    private lateinit var bind:ActivityInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        bind = DataBindingUtil.setContentView(this, R.layout.activity_info)
        initViewPager()
    }

    private fun initViewPager(){
        bind.viewpager.apply {
            adapter = InfoAdapter(this@InfoActivity, 1)
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            currentItem = 0
            offscreenPageLimit = 1
            isUserInputEnabled = false
        }
    }

    fun getNextButton(): Button{
        return bind.nextBtn
    }
}