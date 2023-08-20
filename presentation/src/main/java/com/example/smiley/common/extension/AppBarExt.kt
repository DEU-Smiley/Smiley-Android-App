package com.example.smiley.common.extension

import android.view.View.OnClickListener
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

fun LayoutCommonAppBarBinding.setBasicMode(){
    this.ivLogo.visible()
    this.menu.visible()
    this.nofify.visible()

    this.tvDateText.gone()
    this.tvTitleBarText.gone()
    this.tvTitleBarSubText.gone()
}

fun LayoutCommonAppBarBinding.setCalendarMode(){
    this.tvDateText.visible()
    this.tvTitleBarSubText.visible()

    this.ivLogo.gone()
    this.nofify.gone()
    this.menu.gone()
    this.tvTitleBarText.gone()
    this.tvTitleBarSubText.gone()
}