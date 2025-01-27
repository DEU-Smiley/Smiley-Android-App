package com.example.domain.banner

import com.example.domain.common.base.BaseModel
import kotlinx.parcelize.Parcelize

enum class BannerType {
    BANNER_MEDICINE,
    BANNER_AI,
    BANNER_CHAT_BOT,
    BANNER_NEAR_BY_HOSPITAL
}

@Parcelize
class Banner(
    val id: Int,
    val imageRes: Int,
    val type: BannerType
): BaseModel