package com.example.smiley.common.extension

import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.smiley.R
import com.example.smiley.common.listener.TransparentTouchListener

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

fun Fragment.setCustomColorStatusBarAndNavigationBar(@ColorRes statusBarColor: Int, @ColorRes navigationBarColor: Int, isFullScreen: Boolean){
    (requireActivity() as AppCompatActivity)
        .setCustomColorStatusBarAndNavigationBar(statusBarColor, navigationBarColor, isFullScreen)
}

fun Fragment.resetStatusBarAndNavigationBar(){
    (requireActivity() as AppCompatActivity).resetStatusBarAndNavigationBar()
}

fun Fragment.applyTouchEffectToAllViews(viewGroup: ViewGroup) {
    for (i in 0 until viewGroup.childCount) {
        val child = viewGroup.getChildAt(i)
        if (child.isClickable) {
            child.setOnTouchListener(TransparentTouchListener())
        }

        if (child is ViewGroup) {
            applyTouchEffectToAllViews(child)
        }
    }
}
