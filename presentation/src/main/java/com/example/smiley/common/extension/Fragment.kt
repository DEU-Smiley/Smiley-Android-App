package com.example.smiley.common.extension

import androidx.fragment.app.Fragment
import com.example.smiley.R

fun Fragment.dismiss(){
    requireActivity()
        .supportFragmentManager
        .beginTransaction()
        .remove(this)
        .commit()
}

fun Fragment.addFragment(fragment:Fragment){
    requireActivity().supportFragmentManager
        .beginTransaction()
        .add(R.id.parent_layout, fragment)
        .addToBackStack(null)
        .commit()
}