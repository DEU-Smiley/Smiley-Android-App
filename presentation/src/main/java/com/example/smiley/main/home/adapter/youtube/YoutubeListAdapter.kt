package com.example.smiley.main.home.adapter.youtube

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.domain.youtube.model.YoutubeVideo
import com.example.smiley.R
import com.example.smiley.common.listener.TransparentTouchListener
import com.example.smiley.databinding.SubRecommendVideoItemBinding

class YoutubeListAdapter(
    private val context: Context,
    private var youtubeList: ArrayList<YoutubeVideo>
): RecyclerView.Adapter<YoutubeListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            getViewDataBinding(parent, R.layout.sub_recommend_video_item)
        )
    }

    override fun getItemCount(): Int = youtubeList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(youtubeList[position], position)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateDataSet(youtubeList: ArrayList<YoutubeVideo>){
        this.youtubeList = youtubeList
        notifyDataSetChanged()
    }

    inner class ViewHolder(
        private val bind: SubRecommendVideoItemBinding
    ) : RecyclerView.ViewHolder(bind.root) {
        @SuppressLint("ClickableViewAccessibility")
        fun bind(item: YoutubeVideo, position: Int){
            with(bind){
                tvVideoTitle.text = item.title
                Glide.with(context)
                    .load(item.thumbnail)
                    .into(ivThumbnail)
            }

            bind.clContainer.setOnTouchListener(TransparentTouchListener())
        }
    }

    private fun <T: ViewDataBinding> getViewDataBinding(parent: ViewGroup, layoutRes: Int): T {
        return DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            layoutRes,
            parent,
            false
        )
    }


}