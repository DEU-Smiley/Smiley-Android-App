package com.example.smiley.info

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.smiley.R
import com.example.smiley.common.extension.addFragment
import com.example.smiley.common.extension.gone
import com.example.smiley.databinding.ActivityInfoBinding
import com.example.smiley.info.fragment.SignUpFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InfoActivity : AppCompatActivity() {

    private lateinit var bind:ActivityInfoBinding
    private var currentPage = 0 // ViewPager의 현재 페이지를 저장하는 변수

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        bind = DataBindingUtil.setContentView(this, R.layout.activity_info)
        initViewPager()
    }

    private fun initViewPager(){
        this.addFragment(SignUpFragment())
    }
}