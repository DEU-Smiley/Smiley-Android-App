package com.example.smiley.main.home.adapter

import com.example.domain.magazine.model.Magazine

sealed class TimeLineObject {
    data class TextObject(
        val text:String
    ) : TimeLineObject()

    data class MagazineObject(
        val notice: String,
        val magazine: Magazine
    ) : TimeLineObject()
}