package com.example.smiley.common.utils.decorutils

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.smiley.common.extension.dpToPx

/**
 * RecyclerView의 양 끝의 여백을 지정하는 클래스
 * @param context Context
 * @param dp Int
 * @param isFirstTopPadding Boolean 상단에 여백을 적용할 지의 여부
 * @param isLastBottomPadding Boolean 하단에 역배을 적용할 지의 여부
 */
class EdgeSpaceDecoration(
    private val context: Context,
    private val dp: Int,
    private val isFirstTopPadding: Boolean = false,
    private val isLastBottomPadding: Boolean = false
) : RecyclerView.ItemDecoration() {
    private val pixel = dp.dpToPx(context)

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val isFirst = position == 0
        val isLast = position == (parent.adapter?.itemCount?: 0)-1

        if (isFirstTopPadding && isFirst) {
            outRect.top = pixel
        }
        if(isLastBottomPadding && isLast){
            outRect.bottom = pixel
        }
    }
}