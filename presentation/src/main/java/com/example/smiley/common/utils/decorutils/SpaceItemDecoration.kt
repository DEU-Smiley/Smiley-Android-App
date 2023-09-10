package com.example.smiley.common.utils.decorutils

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.smiley.common.extension.dpToPx

/**
 * RecyclerView 아이템 사이의 간격을 설정하는 클래스
 * @param dp Int
 */
class SpaceItemDecoration(
    private val context: Context,
    private val dp: Int
) : RecyclerView.ItemDecoration() {
    private val pixel = dp.dpToPx(context)

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.left = pixel
        outRect.right = pixel
    }
}