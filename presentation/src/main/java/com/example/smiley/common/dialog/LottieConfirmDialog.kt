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

class LottieConfirmDialog constructor(context: Context): Dialog(context) {
    private var confirmBtn : TextView

    init {
        setCanceledOnTouchOutside(false)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(R.layout.dialog_confirm_with_lottie)

        confirmBtn  = findViewById(R.id.dialog_confirm_btn)
    }

    fun setTitle(title:String){
        val titleView = findViewById<TextView>(R.id.dialog_title)
        titleView.text = title
    }

    fun setContent(content:String){
        val contentView = findViewById<TextView>(R.id.dialog_content)
        contentView.text = content
    }

    fun setSubContent(subContent:String){
        val subContentView = findViewById<TextView>(R.id.dialog_sub_content)
        if(subContent == "") subContentView.gone()

        subContentView.text = subContent
    }

    fun setLottieView(@RawRes rawRes:Int){
        val lottieView = findViewById<LottieAnimationView>(R.id.lottie_view)
        lottieView.setAnimation(rawRes)
    }

    fun setConfirmBtnClickListener(listner: View.OnClickListener) {
        this.confirmBtn.setOnClickListener(listner)
    }
}