package com.example.smiley.common.listener

import android.view.MotionEvent
import android.view.View

class TransparentTouchListener(
    private val onTouchChanged: ((View, MotionEvent) -> Unit)? = null
) : View.OnTouchListener {

    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        if (view.isClickable) {
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    view.alpha = 0.5f // 50% 투명도로 설정
                }
                MotionEvent.ACTION_UP,
                MotionEvent.ACTION_CANCEL,
                MotionEvent.ACTION_OUTSIDE -> {
                    view.alpha = 1.0f // 100% 불투명도로 설정
                }
            }
        }
        onTouchChanged?.invoke(view, motionEvent)
        return false
    }
}