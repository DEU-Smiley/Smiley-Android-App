package com.example.domain.magazine.model

import com.example.domain.common.base.BaseModel
import com.example.domain.magazine.enum.ContentType
import kotlinx.android.parcel.Parcelize

@Parcelize
class Content(
    val content: String,
    val contentType: ContentType,
    val img: ByteArray?
): BaseModel