package com.example.smiley.selfassessment.adapter.assessment

import android.annotation.SuppressLint
import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.smiley.R
import com.example.smiley.common.extension.getViewDataBinding
import com.example.smiley.common.listener.OnItemClickListener
import com.example.smiley.common.listener.TransparentTouchListener
import com.example.smiley.databinding.SubCommonTileItemBinding
import com.example.smiley.selfassessment.item.AssessmentItem
import eightbitlab.com.blurview.RenderScriptBlur

class AssessmentAdapter(
    private val context: Context,
    private val itemList: ArrayList<AssessmentItem>,
    private val onItemClickListener: OnItemClickListener<AssessmentItem>
): RecyclerView.Adapter<AssessmentAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            getViewDataBinding(parent, R.layout.sub_common_tile_item)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position], position)
    }

    override fun getItemCount() = itemList.size

    inner class ViewHolder(
        private val bind: SubCommonTileItemBinding
    ) : RecyclerView.ViewHolder(bind.root) {
        @SuppressLint("ClickableViewAccessibility")
        fun bind(item: AssessmentItem, position: Int){
            with(bind){
                tvType.text = item.type
                tvTitle.text = item.title

                Glide.with(context)
                    .load(item.imageRes)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(ivThumbnail)

                val decorView = bind.root
                val rootView = decorView.findViewById<ViewGroup>(R.id.clContainer)
                val windowBackground = decorView.background

                with(bind.blurViewDimd){
                    setupWith(rootView, RenderScriptBlur(context))
                        .setFrameClearDrawable(windowBackground)
                        .setBlurRadius(5f)
                }

                cvParent.setOnClickListener {
                    onItemClickListener.onItemClicked(position, item)
                }

                cvParent.setOnTouchListener(TransparentTouchListener())
            }
        }
    }
}