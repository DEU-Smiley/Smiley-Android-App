package com.example.smiley.common.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.TextView
import androidx.annotation.RawRes
import com.airbnb.lottie.LottieAnimationView
import com.example.smiley.R
import com.example.smiley.common.extension.gone

class LottieGenericDialog constructor(context: Context): Dialog(context) {
    private var confirmBtn : TextView
    private var cancleBtn  : TextView

    init {
        setCanceledOnTouchOutside(false)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(R.layout.dialog_generic_with_lottie)

        confirmBtn  = findViewById(R.id.dialog_confirm_btn)
        cancleBtn   = findViewById(R.id.dialog_cancle_btn)
    }

    /**
     * 다이얼로그의 제목을 설정하는 메소드
     */
    fun setTitle(title:String){
        val titleView = findViewById<TextView>(R.id.dialog_title)
        titleView.text = title
    }

    /**
     * 다이얼로그의 내용을 설정하는 메소드
     */
    fun setContent(content:String){
        val contentView = findViewById<TextView>(R.id.dialog_content)
        contentView.text = content
    }

    /**
     * 다이얼로그의 부가 내용을 설정하는 메소드
     */
    fun setSubContent(subContent:String){
        val subContentView = findViewById<TextView>(R.id.dialog_sub_content)
        if(subContent == "") subContentView.gone()

        subContentView.text = subContent
    }

    /**
     * 다이얼로그의 LottieView에 애니메이션을 지정하는 메소드
     */
    fun setLottieView(@RawRes rawRes:Int){
        val lottieView = findViewById<LottieAnimationView>(R.id.lottie_view)
        lottieView.setAnimation(rawRes)
    }

    /**
     * 확인 버튼의 텍스트를 변경하는 메소드
     */
    fun setConfirmBtnText(text:String){
        confirmBtn.text = text
    }

    /**
     * 취소 버튼의 텍스트를 변경하는 메소드
     */
    fun setCancleBtnText(text:String){
        cancleBtn.text = text
    }

    /**
     * 다이얼로그의 확인 버튼 클릭 이벤트 등록 메소드
     */
    fun setConfirmBtnClickListener(listner: View.OnClickListener) {
        this.confirmBtn.setOnClickListener(listner)
    }

    /**
     * 다이얼로그의 취소 버튼 클릭 이벤트 등록 메소드
     */
    fun setCancleBtnClickListener(listner: View.OnClickListener) {
        this.cancleBtn.setOnClickListener(listner)
    }
}