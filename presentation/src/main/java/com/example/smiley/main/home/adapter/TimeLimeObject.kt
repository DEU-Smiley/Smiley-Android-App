package com.example.smiley.main.home.adapter

import com.example.domain.magazine.model.Magazine

sealed class TimeLimeObject {
    data class TextObject(
        val text:String
    ) : TimeLimeObject()

    data class MagazineObject(
        val notice: String,
        val magazine: Magazine
    ) : TimeLimeObject()
}