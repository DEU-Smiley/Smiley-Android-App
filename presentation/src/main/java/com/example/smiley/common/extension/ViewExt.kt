package com.example.smiley.common.extension

import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.ScrollView
import androidx.core.view.get
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
 * @param subView:View
 * @param nextView:View?
 * @param hideView:List<View>?
 * @param inputChecker: (()->Unit)?
 */
fun RadioGroup.showViewThenCheckedChanged(subView:View, nextView:View?, hideView:List<View>? = null, inputChecker: (() -> Unit)? = null) {
    this.setOnCheckedChangeListener { _, checkedId ->
        if(checkedId == this[0].id || nextView == null) subView.visible()
        else {
            subView.gone()
            nextView.visible()
            hideView?.map { it.gone() }
        }

        if(inputChecker != null) inputChecker()
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
 * @param inputChecker: (()->Unit)?
 */
fun EditText.showViewThenEnterPressed(nextView: View, scrollView: ScrollView?, inputChecker: (() -> Unit)? = null) {
    setOnKeyListener { view, keyCode, _ ->
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            val input = (view as EditText).text
            if (input.isNotEmpty() && input.isNotBlank()) {
                nextView.visible()
                scrollView?.scrollDown()
            }
        }
        if(inputChecker != null) inputChecker()
        false
    }
}