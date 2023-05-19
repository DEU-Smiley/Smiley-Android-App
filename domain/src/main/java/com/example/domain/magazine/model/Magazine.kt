package com.example.domain.magazine.model

import com.example.domain.common.base.BaseModel
import kotlinx.android.parcel.Parcelize

@Parcelize
class Magazine(
    val id: Int,
    val author: String,
    val title: String,
    val subTitle: String,
    val thumbnail: ByteArray,
    val likes: Int,
    val viewCount: Int,
    val mainContent: Content?
): BaseModel

