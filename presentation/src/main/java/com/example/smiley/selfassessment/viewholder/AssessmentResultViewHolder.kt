package com.example.smiley.selfassessment.viewholder

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.assessment.model.Assessment
import com.example.smiley.databinding.LayoutAssessmentFaqBinding
import com.example.smiley.databinding.LayoutAssessmentHeaderBinding
import com.example.smiley.databinding.LayoutAssessmentRecommendVideoBinding
import com.example.smiley.databinding.LayoutAssessmentStatsBinding
import com.example.smiley.selfassessment.adapter.faq.ExpandFaqAdater
import com.example.smiley.selfassessment.adapter.recommendvideo.RecommendVideoAdater

sealed class AssessmentResultViewHolder(
    bind: ViewDataBinding
): RecyclerView.ViewHolder(bind.root){

    abstract fun bind(item: Assessment)

    class Header (
        private val bind: LayoutAssessmentHeaderBinding
    ): AssessmentResultViewHolder(bind){
        override fun bind(item: Assessment) {
            val headerItem = item as? Assessment.Header

            with(bind){
                tvResultTag.text = headerItem?.resultTag
                tvTitle.text = headerItem?.title
                tvResultExplain.text = headerItem?.explain
            }
        }
    }

    class Stats(
        private val bind: LayoutAssessmentStatsBinding,
    ): AssessmentResultViewHolder(bind){
        override fun bind(item: Assessment) {
            val statsItem = item as? Assessment.Stats

            with(bind){
                pbFacialAsymmetry.progress = statsItem?.facialAsymmetry ?: 0
                pbEyeDegree.progress = statsItem?.eyeDegree ?: 0
                pbLipDegree.progress = statsItem?.lipDegree ?: 0

                tvFacailPercent.text = "${pbFacialAsymmetry.progress}% / 100%"
                tvEyeDegreePercent.text = "${pbEyeDegree.progress}% / 100%"
                tvLipDegreePercent.text = "${pbLipDegree.progress}% / 100%"
            }
        }
    }

    class Faq (
        private val bind: LayoutAssessmentFaqBinding,
    ): AssessmentResultViewHolder(bind){
        override fun bind(item: Assessment) {
            val faqList = (item as? Assessment.FaqList)?.faqList ?: arrayListOf()

            with(bind.rvFaq){
                adapter = ExpandFaqAdater(faqList)
                setHasFixedSize(true)
                isNestedScrollingEnabled = false
            }
        }
    }

    class RecommendVideo (
        private val bind: LayoutAssessmentRecommendVideoBinding,
    ): AssessmentResultViewHolder(bind){
        override fun bind(item: Assessment) {
            val videoList = (item as? Assessment.RecommendVideoList)?.videos ?: arrayListOf()

            with(bind.rvRecommendVideo){
                adapter = RecommendVideoAdater(context, videoList)
                setHasFixedSize(true)
                isNestedScrollingEnabled = false
            }
        }
    }
}