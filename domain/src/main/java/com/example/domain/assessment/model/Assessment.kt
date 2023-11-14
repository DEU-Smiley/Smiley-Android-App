package com.example.domain.assessment.model

import androidx.annotation.DrawableRes
import com.example.domain.common.base.BaseModel
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class Assessment : BaseModel {

    class Header (
        val resultTag: String,
        val title: String,
        val explain: String,
    ): Assessment()

    class Stats (
        val facialAsymmetry: Int,
        val eyeDegree: Int,
        val lipDegree: Int,
    ): Assessment()

    class FaqList(val faqList: ArrayList<Faq>) : Assessment() {

        constructor(question: Array<String>, answer: Array<String>): this(arrayListOf()) {
            for(i in question.indices){
                this.faqList.add(
                    Faq(
                        question = question[i],
                        answer = answer[i]
                    )
                )
            }
        }

        @Parcelize
        data class Faq(
            val question: String,
            val answer: String,
            var isExpanded: Boolean = false
        ) : BaseModel
    }

    class RecommendVideoList(
        val videos: ArrayList<RecommendVideo>
    ): Assessment() {

        /**
         * Thumbnail(ByteArray, Resource)
         * 두 가지 중 하나의 타입으로 전달 받을 수 있음
         */
        @Parcelize
        data class RecommendVideo(
            val title: String,
            val youtubeUrl: String,
            @DrawableRes val thumbnailRes: Int? = null,
            val thumbnail: ByteArray? = null,
        ) : BaseModel
    }
}


