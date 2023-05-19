package com.example.smiley.main.adapter

import android.graphics.BitmapFactory
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.smiley.databinding.TimelineMagazineViewBinding
import com.example.smiley.databinding.TimelineTextViewBinding

sealed class TimeLineViewHolder(
    bind: ViewDataBinding
): RecyclerView.ViewHolder(bind.root){
    abstract fun bind(item: TimeLineItem)

    class TextObjectViewHolder(
        private val bind: TimelineTextViewBinding
    ): TimeLineViewHolder(bind){
        override fun bind(item: TimeLineItem) {
            val viewObject = item.viewObject as ViewObject.TextObject
            bind.title.text = viewObject.text
        }
    }

    class MagazineObjectViewHolder(
        private val bind: TimelineMagazineViewBinding
    ): TimeLineViewHolder(bind){
        override fun bind(item: TimeLineItem) {
            val viewObject = item.viewObject as ViewObject.MagazineObject
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
        }
    }
}