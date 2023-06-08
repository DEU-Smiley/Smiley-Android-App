package com.example.domain.stats.model

import com.example.domain.common.base.BaseModel
import kotlinx.parcelize.Parcelize

@Parcelize
class Exp(
    val title: String,
    val exp: Int
): BaseModel

