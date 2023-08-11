package com.example.smiley.main.home.adapter.timeline

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.magazine.model.Magazine
import com.example.smiley.common.extension.gone
import com.example.smiley.common.listener.OnItemClickListener
import com.example.smiley.common.listener.TransparentTouchListener
import com.example.smiley.databinding.TimelineMagazineViewBinding
import com.example.smiley.databinding.TimelineTextViewBinding

sealed class TimeLineViewHolder(
    bind: ViewDataBinding
): RecyclerView.ViewHolder(bind.root){
    abstract fun bind(item: TimeLineItem, isLastView:Boolean)

    class TextObjectViewHolder(
        private val bind: TimelineTextViewBinding
    ): TimeLineViewHolder(bind){
        override fun bind(item: TimeLineItem, isLastView: Boolean) {
            val viewObject = item.viewObject as TimeLineObject.TextObject
            bind.title.text = viewObject.text

            if(isLastView) bind.marginLayout.gone()
        }
    }

    class MagazineObjectViewHolder(
        private val bind: TimelineMagazineViewBinding,
        private val clickListener: OnItemClickListener<Magazine>?
    ): TimeLineViewHolder(bind){
        @SuppressLint("ClickableViewAccessibility")
        override fun bind(item: TimeLineItem, isLastView: Boolean) {
            val viewObject = item.viewObject as TimeLineObject.MagazineObject
            val magazine = viewObject.magazine

            with(bind){
                title.text = viewObject.notice
                magazineTitle.text = magazine.title
                thumbnail.setImageBitmap(
                    BitmapFactory.decodeByteArray(
                        magazine.thumbnail, 0, magazine.thumbnail.size
                    )
                )
            }

            // 터치 효과 적용
            with(bind.magazineLayout){
                setOnTouchListener(TransparentTouchListener())

                if(clickListener != null) {
                    setOnClickListener {
                        clickListener.onItemClicked(
                            view = it,
                            data = magazine
                        )
                    }
                }
            }

            if(isLastView) bind.marginLayout.gone()
        }
    }
}