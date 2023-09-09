package com.example.smiley.selfassessment.adapter

import androidx.annotation.DrawableRes

data class AssessmentItem(
    @DrawableRes val imageRes: Int,
    val type: String,
    val title: String
)