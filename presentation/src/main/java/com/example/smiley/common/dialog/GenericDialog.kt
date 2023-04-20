package com.example.smiley.common.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.example.smiley.R

class GenericDialog constructor(context: Context): Dialog(context) {
    private var confirmBtn : TextView
    private var cancleBtn  : TextView

    init {
        setCanceledOnTouchOutside(false)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(R.layout.dialog_generic)

        confirmBtn  = findViewById(R.id.dialog_confirm_btn)
        cancleBtn   = findViewById(R.id.dialog_cancle_btn)
    }

    fun setDialogTitle(title:String){
        val titleView = findViewById<TextView>(R.id.dialog_title)
        titleView.text = title
    }

    fun setDialogContent(content:String){
        val contentView = findViewById<TextView>(R.id.dialog_content)
        contentView.text = content
    }

    fun setConfirmBtnClickListener(listner: View.OnClickListener) {
        this.confirmBtn.setOnClickListener(listner)
    }

    fun setCancleBtnClickListener(listner: View.OnClickListener) {
        this.cancleBtn.setOnClickListener(listner)
    }
}