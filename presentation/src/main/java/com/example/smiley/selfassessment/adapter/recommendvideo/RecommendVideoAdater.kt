package com.example.smiley.selfassessment.adapter.recommendvideo

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.domain.assessment.model.Assessment
import com.example.smiley.R
import com.example.smiley.common.extension.getViewDataBinding
import com.example.smiley.common.listener.TransparentTouchListener
import com.example.smiley.databinding.SubRecommendVideoItemBinding

class RecommendVideoAdater(
    private val context:Context,
    private val videoList: ArrayList<Assessment.RecommendVideoList.RecommendVideo>
): RecyclerView.Adapter<RecommendVideoAdater.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            getViewDataBinding(parent, R.layout.sub_recommend_video_item)
        )
    }

    override fun getItemCount(): Int = videoList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(videoList[position], position)
    }

    inner class ViewHolder(
        private val bind: SubRecommendVideoItemBinding
    ): RecyclerView.ViewHolder(bind.root){
        @SuppressLint("SetTextI18n", "ClickableViewAccessibility")
        fun bind(item: Assessment.RecommendVideoList.RecommendVideo, position: Int){
            with(bind){
                tvVideoTitle.text = item.title
                val resources = item.thumbnail ?: item.thumbnailRes

                Glide.with(context)
                    .load(resources)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(ivThumbnail)

                // 유튜브앱 랜딩
                clContainer.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.youtubeUrl))
                    context.startActivity(intent)
                }

                clContainer.setOnTouchListener(TransparentTouchListener())
            }
        }
    }
}