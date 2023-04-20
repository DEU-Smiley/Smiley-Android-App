package com.example.smiley.common.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.example.smiley.R

class ConfirmDialog constructor(context: Context): Dialog(context) {
    private var confirmBtn : TextView

    init {
        setCanceledOnTouchOutside(false)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(R.layout.dialog_confirm)

        confirmBtn  = findViewById(R.id.dialog_confirm_btn)
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
}