package com.example.smiley.common.extension

import android.app.Activity
import android.content.Intent
import com.example.smiley.common.dialog.ConfirmDialog
import com.example.smiley.common.dialog.GenericDialog


fun<T> Activity.changeActivity(activity:Class<T>) {
    val intent = Intent(this, activity)
    startActivity(intent) //intent 에 명시된 액티비티로 이동
    finish() //현재 액티비티 종료
}

/**
 * 기본 다이얼로그 출력 메소드
 * 예 / 아니오
 * @param confirmListener: ()->Unit (확인 버튼을 눌렀을 때 동작)
 * @param cancleListner: ()->Unit (취소 버튼을 눌렀을 때 동작)
 */
fun Activity.showGenericDialog(
    title: String,
    content: String,
    confirmListener : (()->Unit) = {},
    cancleListner   : (()->Unit) = {}
){
    val dialog = GenericDialog(this)
    dialog.apply {
        setDialogTitle(title)
        setDialogContent(content)
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
 * @param confirmListener: ()->Unit (확인 버튼을 눌렀을 때 동작)
 */
fun Activity.showConfirmDialog(
    title: String,
    content: String,
    confirmListener: (() -> Unit) = {},
){
    val dialog = ConfirmDialog(this)
    dialog.apply {
        setDialogTitle(title)
        setDialogContent(content)
        setConfirmBtnClickListener{
            confirmListener()
            this.dismiss()
        }
    }.show()
}