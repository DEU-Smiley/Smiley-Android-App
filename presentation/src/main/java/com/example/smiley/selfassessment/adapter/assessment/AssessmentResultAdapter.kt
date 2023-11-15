package com.example.smiley.selfassessment.adapter.assessment

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.assessment.model.Assessment
import com.example.smiley.R
import com.example.smiley.common.extension.getViewDataBinding
import com.example.smiley.selfassessment.viewholder.AssessmentResultViewHolder

class AssessmentResultAdapter(
    private val context: Context,
    private val itemList: ArrayList<Assessment>
) : RecyclerView.Adapter<AssessmentResultViewHolder>() {

    enum class HolderType {
        HEADER,
        STATS,
        FAQ,
        RECOMMEND_VIDEO
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AssessmentResultViewHolder {
        return when(viewType){
            HolderType.HEADER.ordinal -> {
                AssessmentResultViewHolder.Header(
                    getViewDataBinding(parent, R.layout.layout_assessment_header)
                )
            }
            HolderType.STATS.ordinal -> {
                AssessmentResultViewHolder.Stats(
                    getViewDataBinding(parent, R.layout.layout_assessment_stats)
                )
            }
            HolderType.FAQ.ordinal -> {
                AssessmentResultViewHolder.Faq(
                    getViewDataBinding(parent, R.layout.layout_assessment_faq)
                )
            }
            else -> {
                AssessmentResultViewHolder.RecommendVideo(
                    getViewDataBinding(parent, R.layout.layout_assessment_recommend_video)
                )
            }
        }
    }

    override fun onBindViewHolder(holder: AssessmentResultViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount() = itemList.size

    override fun getItemViewType(position: Int): Int {
        return when (itemList[position]) {
            is Assessment.Header -> {
                HolderType.HEADER
            }
            is Assessment.Stats -> {
                HolderType.STATS
            }
            is Assessment.FaqList -> {
                HolderType.FAQ
            }
            is Assessment.RecommendVideoList -> {
                HolderType.RECOMMEND_VIDEO
            }
        }.ordinal
    }
}