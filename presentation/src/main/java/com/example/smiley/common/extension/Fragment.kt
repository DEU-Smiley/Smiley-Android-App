package com.example.smiley.common.extension

import androidx.appcompat.app.AppCompatActivity
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
        .add(R.id.base_layout, fragment)
        .addToBackStack(null)
        .commit()
}

fun Fragment.addFragmentToFullScreen(fragment: Fragment){
    requireActivity().supportFragmentManager
        .beginTransaction()
        .add(R.id.parent_layout, fragment)
        .addToBackStack(null)
        .commit()
}

fun Fragment.setCustomColorStatusBarAndNavigationBar(statusBarColor: Int, navigationBarColor: Int){
    (requireActivity() as AppCompatActivity)
        .setCustomColorStatusBarAndNavigationBar(statusBarColor, navigationBarColor)
}

fun Fragment.resetStatusBarAndNavigationBar(){
    (requireActivity() as AppCompatActivity).resetStatusBarAndNavigationBar()
}