package com.example.smiley.info

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.example.smiley.R
import com.example.smiley.databinding.ActivityInfoBinding
import com.example.smiley.info.adapter.InfoAdapter

class InfoActivity : AppCompatActivity() {

    private lateinit var bind:ActivityInfoBinding
    private var currentPage = 0 // ViewPager의 현재 페이지를 저장하는 변수

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        bind = DataBindingUtil.setContentView(this, R.layout.activity_info)
        initViewPager()
        setBackBtnStatus()
        addNextBtnClickEvent()
        addBackBtnClickEvent()
    }



    private fun initViewPager(){
        bind.viewpager.apply {
            adapter = InfoAdapter(this@InfoActivity, R.id.indicator_layout, PAGE_COUNT)
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            currentItem = 0
            offscreenPageLimit = ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT
            isUserInputEnabled = false
        }
    }

    /**
     * 다음 버튼 클릭 이벤트 (다음 페이지로 이동)
     */
    private fun addNextBtnClickEvent(){
        bind.nextBtn.setOnClickListener {
            currentPage = (if(currentPage + 1 >= PAGE_COUNT) PAGE_COUNT-1 else currentPage + 1) % PAGE_COUNT
            setBackBtnStatus()
            setViewPagerStatus(currentPage)
        }
    }

    /**
     * 뒤로 가기 버튼(상단바) 클릭 이벤트 (이전 페이지로 이동)
     */
    private fun addBackBtnClickEvent(){
        bind.backBtn.setOnClickListener {
            currentPage = (if(currentPage - 1 < 0) 0 else currentPage - 1) % PAGE_COUNT
            setBackBtnStatus()
            setViewPagerStatus(currentPage)
        }
    }

    /**
     * 뷰페이저 상태를 결정하는 메소드
     */
    private fun setViewPagerStatus(currentPage:Int){
        bind.viewpager.apply {
            setCurrentItem(currentPage, true)
            (adapter as InfoAdapter).apply {
                setIndicatorPosit(currentPage)
                (getFragmentAt(currentPage) as ButtonClickable).setButtonStatus()
            }
        }
    }
    /**
     * 상단 뒤로가기 버튼의 상태를 지정하는 ㅔㅁ소드
     */
    private fun setBackBtnStatus(){
        bind.backBtn.isVisible = currentPage != 0
    }

    /**
     * 부모의 다음 버튼을 가져오는 메소드
     */
    fun getNextButton(): Button {
        return bind.nextBtn
    }

    companion object {
        const val PAGE_COUNT:Int = 3
    }
}