package com.example.smiley.common.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import com.example.smiley.R
import com.example.smiley.common.extension.dpToPx
import com.example.smiley.databinding.CustomEmojiProgressBarBinding

class CustomEmojiProgressBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {
    private val bind: CustomEmojiProgressBarBinding = DataBindingUtil.inflate(
        LayoutInflater.from(context),
        R.layout.custom_emoji_progress_bar,
        this,
        true
    )
    private val progressBar by lazy { bind.pbProgressBar }
    private val emojiIndicator by lazy { bind.ivEmojiIndicator }
    var progress: Int = 0
        set(value) {
            field = value
            updateProgress(value)
        }

    init {
        progressBar.progress = progress

        val attributes = context.obtainStyledAttributes(attrs, R.styleable.CustomEmojiProgressBar)
        val progressFromXml = attributes.getInt(R.styleable.CustomEmojiProgressBar_progress, 0)
        val maxFromXml = attributes.getInt(R.styleable.CustomEmojiProgressBar_max, 100)

        post{
            updateProgress(progressFromXml)
        }
        setMax(maxFromXml)

        attributes.recycle()
    }

    /**
     * ProgressBar가 줄어든 픽셀만큼 emoji의 BottomMargin을 줄이는 메소드
     * @param progress Int
     */
    private fun adjustEmojiPositionByProgress() {
        val totalHeight = progressBar.measuredHeight
        val delta:Float = totalHeight.toFloat() / progressBar.max
        val reducedPixel: Int = (delta * (progressBar.max - progressBar.progress)).toInt()

        val params = emojiIndicator.layoutParams as LayoutParams
        params.bottomMargin = (-reducedPixel) + 10.dpToPx(context) // -줄어든_간격.toInt().pxToDp(context).toInt()
        emojiIndicator.layoutParams = params
    }

    private fun updateProgress(progress: Int) {
        progressBar.progress = progress
        adjustEmojiPositionByProgress()
    }

    private fun setMax(max: Int){
        progressBar.max = max
    }
}