package com.example.smiley.main.banner.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.domain.banner.Banner
import com.example.domain.banner.BannerType
import com.example.smiley.R
import com.example.smiley.common.extension.getViewDataBinding
import com.example.smiley.common.listener.TransparentTouchListener
import com.example.smiley.databinding.SubBannerItemBinding

class AutoScrollBannerAdapter(
    private val context: Context,
    private var items: ArrayList<Banner> = arrayListOf(
        Banner(
            id = 1,
            imageRes = R.drawable.img_banner_medicine,
            type = BannerType.BANNER_MEDICINE
        ),
        Banner(
            id = 2,
            imageRes = R.drawable.img_banner_ai,
            type = BannerType.BANNER_AI
        ),
        Banner(
            id = 3,
            imageRes = R.drawable.img_banner_chat_bot,
            type = BannerType.BANNER_CHAT_BOT
        ),
        Banner(
            id = 4,
            imageRes = R.drawable.img_banner_find_dental,
            type = BannerType.BANNER_NEAR_BY_HOSPITAL
        ),
    )
) : RecyclerView.Adapter<AutoScrollBannerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            getViewDataBinding(parent, R.layout.sub_banner_item)
        )
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateDataSet(items: ArrayList<Banner>) {
        this.items = items
        notifyDataSetChanged()
    }

    inner class ViewHolder(
        private val bind: SubBannerItemBinding
    ) : RecyclerView.ViewHolder(bind.root) {
        @SuppressLint("ClickableViewAccessibility")
        fun bind(item: Banner) {
            with(bind) {
                Glide.with(context)
                    .load(item.imageRes)
                    .into(ivBanner)

                ivBanner.setOnTouchListener(TransparentTouchListener())
            }
        }
    }
}