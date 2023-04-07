package com.example.smiley.common.extension

import androidx.fragment.app.Fragment

fun Fragment.dismiss(){
    requireActivity()
        .supportFragmentManager
        .beginTransaction()
        .remove(this)
        .commit()
}