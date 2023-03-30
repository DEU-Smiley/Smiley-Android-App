package com.example.smiley.common.extension

import android.annotation.SuppressLint
import android.content.Context
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.ScrollView
import androidx.core.view.get
import com.example.smiley.App
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


/**
 * 선택된 라디오 버튼에 따라 다음 뷰를 표시하는 메소드
 * 버튼 2개인 경우만 해당
 * @param visibleView:View
 * @param goneView:View?
 */
fun RadioGroup.showViewThenCheckedChanged(visibleView:View, goneView:View?) {
    this.setOnCheckedChangeListener { _, checkedId ->
        if(checkedId == this[0].id || goneView == null) visibleView.visible()
        else {
            visibleView.gone()
            goneView.visible()
        }
    }
}

/**
 * ScrollView를 맨 밑으로 내리는 메소드
 */
fun ScrollView.scrollDown(){
    this.post{
        this.fullScroll(ScrollView.FOCUS_DOWN)
    }
}


/**
 * EditText에서 엔터 누르면 다음 뷰를 표시하는 메소드
 * @param nextView:View
 * @param scrollView:ScrollView?
 */
fun EditText.showViewThenEnterPressed(nextView: View, scrollView: ScrollView?) {
    setOnKeyListener { view, keyCode, _ ->
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            val input = (view as EditText).text
            if (input.isNotEmpty() && input.isNotBlank()) {
                nextView.visible()
                scrollView?.scrollDown()
            }
        }
        false
    }
}