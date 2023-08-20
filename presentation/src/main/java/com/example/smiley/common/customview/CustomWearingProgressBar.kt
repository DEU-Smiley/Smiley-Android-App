package com.example.smiley.common.customview

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.smiley.R
import com.example.smiley.common.extension.dpToPx
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
        // updateProgressTint(progressTintXml)

        attributes.recycle()
    }

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
}