package com.example.smiley.common.extension

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.smiley.R

fun Context.showToast(message: String){
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Context.showGenericAlertDialog(message: String){
    AlertDialog.Builder(this).apply {
        setMessage(message)
        setPositiveButton("확인"){ dialog, _ ->
            dialog.dismiss()
        }
    }.show()
}