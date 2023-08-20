package com.example.smiley.common.extension

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.WindowManager
import androidx.annotation.RawRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
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

fun AppCompatActivity.setCustomColorStatusBarAndNavigationBar(statusBarColor: Int, navigationBarColor: Int, isFullScreen: Boolean = false){
    // 상단바(Status Bar)와 하단바(Navigation Bar) 투명 처리
    window.apply {
        addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        this.statusBarColor = ContextCompat.getColor(context, statusBarColor)
        this.navigationBarColor = ContextCompat.getColor(context, navigationBarColor)

        if(isFullScreen){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                // 레이아웃이 상단바 영역까지 덮을 수 있게 됨
                setDecorFitsSystemWindows(false)
            } else {
                @Suppress("DEPRECATION")
                decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
            }
        }
    }
}

fun AppCompatActivity.setTransparentStatusBarAndNavigationBar(){
    // 상단바(Status Bar)와 하단바(Navigation Bar) 투명 처리
    window.apply {
        addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        statusBarColor = Color.TRANSPARENT
        navigationBarColor = Color.TRANSPARENT

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // 레이아웃이 상단바 영역까지 덮을 수 있게 됨
            setDecorFitsSystemWindows(false)
        } else {
            @Suppress("DEPRECATION")
            decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        }
    }
}

fun AppCompatActivity.resetStatusBarAndNavigationBar(){
    window.apply {
        // clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        statusBarColor = Color.WHITE
        navigationBarColor = Color.WHITE

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // 레이아웃이 상단바 영역까지 덮을 수 있게 됨
            setDecorFitsSystemWindows(true)
        } else {
            @Suppress("DEPRECATION")
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        }
    }
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