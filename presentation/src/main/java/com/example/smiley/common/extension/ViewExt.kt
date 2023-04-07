package com.example.smiley.common.extension

import android.graphics.Typeface
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextWatcher
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.view.KeyEvent
import android.view.View
import android.widget.*
import androidx.core.view.get
import androidx.core.widget.addTextChangedListener
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

/**
 * TextView의 텍스트가 변경 되면 다음 뷰를 표시하는 메소드
 * @param nextView:View
 * @param scrollView:ScrollView?
 * @param inputChecker: (()->Unit)?
 */
fun TextView.showViewThenTextChanged(nextView: View, scrollView: ScrollView?, inputChecker: (() -> Unit)? = null){
    addTextChangedListener(object : TextWatcher{
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(p0: Editable?) {
            val input = "$p0"
            if (input.isNotEmpty() && input.isNotBlank()) {
                nextView.visible()
                scrollView?.scrollDown()
            }
            if (inputChecker != null) inputChecker()
        }
    })
}

/**
 * start~end까지의 텍스트의 전경색 변경
 * @param color :Int
 * @param start :Int
 * @param end   :Int
 */
fun TextView.setForegroundColor(color:Int, start:Int, end:Int){
    val span = SpannableStringBuilder(this.text)
    span.setSpan(ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE)
    this.text = span
}

/**
 * start~end까지의 텍스트의 배경색 변경
 * @param color :Int
 * @param start :Int
 * @param end   :Int
 */
fun TextView.setBackGroundColor(color:Int, start:Int, end:Int){
    val span = SpannableStringBuilder(this.text)
    span.setSpan(BackgroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE)
    this.text = span
}

/**
 * start~end까지의 텍스트에 밑줄 표시
 * @param start :Int
 * @param end   :Int
 */
fun TextView.setUnderLine(start:Int, end:Int){
    val span = SpannableStringBuilder(this.text)
    span.setSpan(UnderlineSpan(), start, end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE)
    this.text = span
}

/**
 * start~end까지의 텍스트를 Bold로 변환
 * @param start :Int
 * @param end   :Int
 */
fun TextView.setBold(start:Int, end:Int){
    val span = SpannableStringBuilder(this.text)
    span.setSpan(StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE)
    this.text = span
}