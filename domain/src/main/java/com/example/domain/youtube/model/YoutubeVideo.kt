package com.example.domain.youtube.model

import com.example.domain.common.base.BaseModel
import kotlinx.parcelize.Parcelize

@Parcelize
class YoutubeVideo(
    val id          : Int,
    val title       : String,
    val youtubeUrl  : String,
    val thumbnail   : ByteArray
): BaseModel