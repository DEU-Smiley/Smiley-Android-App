package com.example.smiley.common.utils.decorutils

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.smiley.common.extension.dpToPx

class AllSpaceDecoration(
    context: Context,
    private var start: Int,
    private var end: Int,
    private var top: Int,
    private var bottom: Int,
) : RecyclerView.ItemDecoration() {

    init {
        start = start.dpToPx(context)
        end = end.dpToPx(context)
        top = top.dpToPx(context)
        bottom = bottom.dpToPx(context)
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.top = top
        outRect.bottom = bottom
        outRect.left = start
        outRect.right = end
    }
}