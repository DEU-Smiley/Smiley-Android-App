package com.example.smiley.main.home.adapter.magazine

import android.annotation.SuppressLint
import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.domain.magazine.model.Magazine
import com.example.smiley.R
import com.example.smiley.common.extension.getViewDataBinding
import com.example.smiley.common.listener.OnItemClickListener
import com.example.smiley.common.listener.TransparentTouchListener
import com.example.smiley.databinding.SubCommonTileItemBinding
import eightbitlab.com.blurview.RenderScriptBlur

class RecentMagazineAdapter(
    private val context:Context,
    private var items: ArrayList<Magazine>,
    private val magazineClickListener: OnItemClickListener<Magazine>?
): RecyclerView.Adapter<RecentMagazineAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            getViewDataBinding(parent, R.layout.sub_common_tile_item)
        )
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateDataSet(items: ArrayList<Magazine>){
        this.items = items
        notifyDataSetChanged()
    }

    inner class ViewHolder(
        private val bind: SubCommonTileItemBinding
    ) : RecyclerView.ViewHolder(bind.root) {
        @SuppressLint("ClickableViewAccessibility")
        fun bind(item: Magazine){
            with(bind){
                tvType.text = "덴탈 매거진"
                tvTitle.text = item.title

                Glide.with(context)
                    .load(item.thumbnail)
                    .into(ivThumbnail)

                val decorView = bind.root
                val rootView = decorView.findViewById<ViewGroup>(R.id.cvParent)
                val windowBackground = decorView.background

                with(bind.blurViewDimd) {
                    setupWith(rootView, RenderScriptBlur(context))
                        .setFrameClearDrawable(windowBackground)
                        .setBlurRadius(5f)
                }
            }
            bind.cvParent.setOnClickListener {
                magazineClickListener?.onItemClicked(it, item)
            }

            bind.cvParent.setOnTouchListener(TransparentTouchListener())
        }
    }
}