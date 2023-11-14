package com.example.smiley.common.utils.decorutils

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.smiley.common.extension.dpToPx

/**
 * RecyclerView 아이템 사이의 간격을 설정하는 클래스
 * @param context Context
 * @param dp Int
 * @param isEdgeSpacingEnabled Boolean - 양 끝의 아이템에도 여백을 적용할 지 여부
 */
class SideSpaceDecoration(
    private val context: Context,
    private val dp: Int,
    private val isEdgeSpacingEnabled: Boolean = false
) : RecyclerView.ItemDecoration() {
    private val pixel = dp.dpToPx(context)

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)

        if (isEdgeSpacingEnabled || position != 0) {
            outRect.left = pixel
        }
        outRect.right = pixel
    }
}