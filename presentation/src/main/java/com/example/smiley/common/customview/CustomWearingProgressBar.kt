package com.example.smiley.common.customview

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.smiley.R
import com.example.smiley.common.extension.dpToPx
import com.example.smiley.common.extension.invisible
import com.example.smiley.common.extension.visible
import com.example.smiley.databinding.CustomEmojiProgressBarBinding
import com.example.smiley.databinding.CustomWearingProgressBarBinding

@SuppressLint("CustomViewStyleable")
class CustomWearingProgressBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {
    private val bind: CustomWearingProgressBarBinding = DataBindingUtil.inflate(
        LayoutInflater.from(context),
        R.layout.custom_wearing_progress_bar,
        this,
        true
    )
    private val progressBar by lazy { bind.pbWearProgressBar }
    private val starIndicator by lazy { bind.ivCircleStar }

    var progress: Int = 0
        set(value) {
            field = value
            updateProgress(value)
        }

    init {
        progressBar.progress = progress

        val attributes = context.obtainStyledAttributes(attrs, R.styleable.WearingProgressBar)
        val progressFromXml = attributes.getInt(R.styleable.WearingProgressBar_progress, 0)
        val maxFromXml = attributes.getInt(R.styleable.WearingProgressBar_max, 100)

        post{
            updateProgress(progressFromXml)
        }
        setMax(maxFromXml)
        attributes.recycle()
    }

    /**
     * ProgressBar의 진행률을 업데이트하는 메소드 (+ 애니메이션)
     *
     * @param progress Int
     */
    private fun updateProgress(progress: Int) {
        progressBar.progress = progress
        val animator = ObjectAnimator.ofInt(progressBar, "progress", 0, progressBar.progress)
        animator.apply {
            duration = 1000
            start()
            doOnEnd {
                if(progressBar.progress == 100){
                    starIndicator.setImageResource(R.drawable.ic_circle_star_primary)
                } else {
                    starIndicator.setImageResource(R.drawable.ic_circle_star)
                }
            }
        }
    }

    private fun setMax(max: Int){
        progressBar.max = max
    }

    /**
     * 그래프의 세로 구분선의 표시 여부를 지정하는 메소드
     *
     * @param isShow Boolean
     */
    fun showDevideLine(isShow: Boolean){
        val divides = arrayListOf(
            bind.view1, bind.view2, bind.view3, bind.view4, bind.view5, bind.view6
        )

        if(isShow){
            divides.forEach { it.visible() }
        } else {
            divides.forEach { it.invisible() }
        }
    }

    /**
     * 그래프의 퍼센트 라벨 표시 여부를 지정하는 메소드
     *
     * @param isShow Boolean
     */
    fun showPercentLabel(isShow: Boolean){
        val labels = arrayListOf(
            bind.tvPercent20, bind.tvPercent40, bind.tvPercent70, bind.tvPercent100
        )

        if(isShow){
            labels.forEach { it.visible() }
        } else {
            labels.forEach { it.invisible() }
        }
    }
}