package com.example.domain.toothbrush.model

import android.graphics.Bitmap
import com.example.domain.common.base.BaseModel
import kotlinx.parcelize.Parcelize

@Parcelize
class ToothBrush(
    val image: ByteArray
): BaseModel