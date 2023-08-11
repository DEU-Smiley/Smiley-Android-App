package com.example.domain.youtube.model

import com.example.domain.common.base.BaseModel
import kotlinx.parcelize.Parcelize

@Parcelize
class YoutubeVideoList(
    val youtubeList: ArrayList<YoutubeVideo>
): BaseModel