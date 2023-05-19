package com.example.smiley.main.adapter

import com.example.domain.magazine.model.Magazine

sealed class ViewObject {
    data class TextObject(
        val text:String
    ) : ViewObject()

    data class MagazineObject(
        val notice: String,
        val magazine: Magazine
    ) : ViewObject()
}