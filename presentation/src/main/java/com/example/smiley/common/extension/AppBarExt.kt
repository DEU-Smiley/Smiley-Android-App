package com.example.smiley.common.extension

import android.view.View.OnClickListener
import com.example.smiley.R
import com.example.smiley.databinding.LayoutCommonAppBarBinding

fun LayoutCommonAppBarBinding.setTitle(str: String){
    this.ivLogo.gone()
    this.tvDateText.gone()
    this.tvTitleBarText.visible()

    with(this.tvTitleBarText){
        text = str
    }
}

fun LayoutCommonAppBarBinding.setDateText(date: String){
    this.ivLogo.gone()
    this.tvTitleBarText.gone()
    this.tvDateText.visible()
    this.clWearTimeLayout.gone()

    with(this.tvDateText){
        text = date
    }
}

fun LayoutCommonAppBarBinding.setSubtitle(str: String, clickListener: OnClickListener? = null){
    this.ivLogo.gone()
    this.tvTitleBarSubText.visible()

    with(this.tvTitleBarSubText){
        text = str
        setOnClickListener {
            clickListener?.onClick(this)
        }
    }
}

/**
 * 기본 모드 앱바
 */
fun LayoutCommonAppBarBinding.setBasicMode(){
    this.ivLogo.visible()
    this.menu.visible()
    this.nofify.visible()

    this.tvDateText.gone()
    this.tvTitleBarText.gone()
    this.tvTitleBarSubText.gone()
    this.clWearTimeLayout.gone()
}

/**
 * 착용 시간 앱바
 */
fun LayoutCommonAppBarBinding.setWearTimeMode(isWearing: Boolean = false){
    this.ivLogo.visible()
    this.clWearTimeLayout.visible()

    this.tvWearTime.apply {
        val timeColor = if(isWearing) R.color.primary_normal else R.color.gray2_5E

        setAbsoluteSizeFromDimenRes(R.dimen.H4_M500_18_auto, 0, 2)
        setAbsoluteSizeFromDimenRes(R.dimen.H4_M500_18_auto, 5, 7)
        setForegroundColor(timeColor,0,2)
        setForegroundColor(timeColor,5,7)
    }

    this.menu.gone()
    this.nofify.gone()
    this.tvDateText.gone()
    this.tvTitleBarText.gone()
    this.tvTitleBarSubText.gone()
}

/**
 * 캘린더 모드 앱바
 */
fun LayoutCommonAppBarBinding.setCalendarMode(){
    this.tvDateText.visible()
    this.tvTitleBarSubText.visible()

    this.ivLogo.gone()
    this.nofify.gone()
    this.menu.gone()
    this.tvTitleBarText.gone()
    this.tvTitleBarSubText.gone()
    this.clWearTimeLayout.gone()
}