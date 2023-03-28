package com.example.smiley.common.extension

import android.annotation.SuppressLint
import android.view.View
import android.widget.Button
import com.example.smiley.R

fun View.gone(){ visibility = View.GONE }

fun View.visible(){ visibility = View.VISIBLE }

fun View.invisible(){ visibility = View.INVISIBLE }

fun Button.setDisabled(){
    this.setTextColor(resources.getColor(R.color.gray3_8E))
    this.isEnabled = false
}

fun Button.setEnabled(){
    this.setTextColor(resources.getColor(R.color.white))
    this.isEnabled = true
}