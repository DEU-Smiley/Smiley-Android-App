package com.example.smiley.selfassessment.adapter.faq

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.assessment.model.Assessment
import com.example.smiley.R
import com.example.smiley.common.customview.ToggleAnimation
import com.example.smiley.common.extension.getViewDataBinding
import com.example.smiley.databinding.SubFaqItemBinding

class ExpandFaqAdater(
    private val faqList: ArrayList<Assessment.FaqList.Faq>
): RecyclerView.Adapter<ExpandFaqAdater.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            getViewDataBinding(parent, R.layout.sub_faq_item)
        )
    }

    override fun getItemCount(): Int = faqList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(faqList[position], position)
    }

    /**
     * ExpandLayout 토글 메소드
     */
    private fun toggleLayout(isExpanded: Boolean, view: View, expandLayout: ConstraintLayout): Boolean{
        ToggleAnimation.toggleArrow(view, isExpanded)

        if(isExpanded) ToggleAnimation.expand(expandLayout)
        else ToggleAnimation.collapse(expandLayout)

        return isExpanded
    }

    inner class ViewHolder(
        private val bind: SubFaqItemBinding
    ): RecyclerView.ViewHolder(bind.root){
        @SuppressLint("SetTextI18n")
        fun bind(item: Assessment.FaqList.Faq, position: Int){
            with(bind){
                tvQuestionIdx.text = "Q${position+1}."
                tvQuestion.text = item.question
                tvAnswer.text = item.answer

                clQuestionLayout.setOnClickListener {
                    val show = toggleLayout(!item.isExpanded, ivArrayDown, clExpandLayout)
                    item.isExpanded = show
                }
            }
        }
    }
}