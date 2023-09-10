package com.example.smiley.selfassessment.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.smiley.R
import com.example.smiley.common.extension.getViewDataBinding
import com.example.smiley.common.listener.TransparentTouchListener
import com.example.smiley.databinding.SubCommonTileItemBinding
import eightbitlab.com.blurview.RenderScriptBlur

class AssessmentAdapter(
    private val context: Context,
    private val itemList: ArrayList<AssessmentItem>
): RecyclerView.Adapter<AssessmentAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            getViewDataBinding(parent, R.layout.sub_common_tile_item)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount() = itemList.size

    inner class ViewHolder(
        private val bind: SubCommonTileItemBinding
    ) : RecyclerView.ViewHolder(bind.root) {
        fun bind(item: AssessmentItem){
            with(bind){
                tvType.text = item.type
                tvTitle.text = item.title

                Glide.with(context)
                    .load(item.imageRes)
                    .into(ivThumbnail)

                val decorView = bind.root
                val rootView = decorView.findViewById<ViewGroup>(R.id.clContainer)
                val windowBackground = decorView.background

                with(bind.blurViewDimd){
                    setupWith(rootView, RenderScriptBlur(context))
                        .setFrameClearDrawable(windowBackground)
                        .setBlurRadius(5f)
                }

                cvParent.setOnTouchListener(TransparentTouchListener())
            }
        }
    }
}