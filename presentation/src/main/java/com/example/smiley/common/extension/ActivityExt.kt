package com.example.smiley.common.extension

import android.app.Activity
import android.content.Intent
import androidx.annotation.RawRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.smiley.App
import com.example.smiley.R
import com.example.smiley.common.dialog.ConfirmDialog
import com.example.smiley.common.dialog.GenericDialog
import com.example.smiley.common.dialog.LottieConfirmDialog
import com.example.smiley.common.dialog.LottieGenericDialog


fun<T> Activity.changeActivity(activity:Class<T>) {
    val intent = Intent(this, activity)
    startActivity(intent) //intent 에 명시된 액티비티로 이동
    finish() //현재 액티비티 종료
}

/**
 * Activity에 Fragment를 추가하는 메소드
 * @param fragment: Fragment
 */
fun AppCompatActivity.addFragment(fragment: Fragment){
    supportFragmentManager
        .beginTransaction()
        .add(R.id.base_layout, fragment)
        .addToBackStack(null)
        .commit()
}

fun AppCompatActivity.addFragmentToFullScreen(fragment: Fragment){
    supportFragmentManager
        .beginTransaction()
        .add(R.id.parent_layout, fragment)
        .addToBackStack(null)
        .commit()
}
/**
 * 기본 다이얼로그 출력 메소드
 * 확인 / 취소
 * @param title: String
 * @param content: String
 * @param subContent: String
 * @param confirmText: String (확인 버튼 텍스트)
 * @param cancleText: String (취소 버튼 텍스트)
 * @param confirmListener: ()->Unit (확인 버튼을 눌렀을 때 동작)
 * @param cancleListner: ()->Unit (취소 버튼을 눌렀을 때 동작)
 */
fun Activity.showGenericDialog(
    title: String,
    content: String,
    subContent: String = "",
    confirmText: String = "확인",
    cancleText: String = "취소",
    confirmListener : (()->Unit) = {},
    cancleListner   : (()->Unit) = {}
){
    val dialog = GenericDialog(this)
    dialog.apply {
        setTitle(title)
        setContent(content)
        setSubContent(subContent)
        setConfirmBtnText(confirmText)
        setCancleBtnText(cancleText)
        setConfirmBtnClickListener{
            confirmListener()
            this.dismiss()
        }
        setCancleBtnClickListener{
            cancleListner()
            this.dismiss()
        }
    }.show()
}

/**
 * 확인 다이얼로그 출력 메소드
 * @param title: String
 * @param content: String
 * @param subContent: String
 * @param confirmListener: ()->Unit (확인 버튼을 눌렀을 때 동작)
 */
fun Activity.showConfirmDialog(
    title: String,
    content: String,
    subContent: String = "",
    confirmListener: (() -> Unit) = {},
){
    val dialog = ConfirmDialog(this)
    dialog.apply {
        setTitle(title)
        setContent(content)
        setSubContent(subContent)
        setConfirmBtnClickListener{
            confirmListener()
            this.dismiss()
        }
    }.show()
}

/**
 * 확인 Lottie 다이얼로그 출력 메소드
 *  * @param title: String
 * @param content: String
 * @param subContent: String
 * @param lottieView: Int
 * @param confirmListener: ()->Unit (확인 버튼을 눌렀을 때 동작)
 */
fun Activity.showLottieConfirmDialog(
    title: String,
    content: String,
    subContent: String,
    @RawRes lottieView: Int,
    confirmListener: (() -> Unit) = {},
){
    val dialog = LottieConfirmDialog(this)
    dialog.apply {
        setTitle(title)
        setContent(content)
        setSubContent(subContent)
        setLottieView(lottieView)
        setConfirmBtnClickListener{
            confirmListener()
            this.dismiss()
        }
    }
}


/**
 * Lottie 다이얼로그 출력 메소드
 *
 * @param title: String
 * @param content: String
 * @param subContent: String
 * @param lottieView: Int
 * @param confirmText: String (확인 버튼 텍스트)
 * @param cancleText: String (취소 버튼 텍스트)
 * @param confirmListener: ()->Unit (확인 버튼을 눌렀을 때 동작)
 * @param cancleListner: ()->Unit (취소 버튼을 눌렀을 때 동작)
 */
fun Activity.showLottieGenericDialog(
    title: String,
    content: String,
    subContent: String = "",
    @RawRes lottieView: Int,
    confirmText: String = "확인",
    cancleText: String = "취소",
    confirmListener: (() -> Unit) = {},
    cancleListner: (() -> Unit) = {}
) {
    val dialog = LottieGenericDialog(this)
    dialog.apply {
        setTitle(title)
        setContent(content)
        setSubContent(subContent)
        setLottieView(lottieView)
        setConfirmBtnText(confirmText)
        setCancleBtnText(cancleText)
        setConfirmBtnClickListener {
            confirmListener()
            this.dismiss()
        }
        setCancleBtnClickListener {
            cancleListner()
            this.dismiss()
        }
    }.show()
}