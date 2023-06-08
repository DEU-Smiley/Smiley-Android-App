package com.example.domain.stats.model

import com.example.domain.common.base.BaseModel
import kotlinx.parcelize.Parcelize

@Parcelize
class Stats(
    val wearTime            : Int, // 당일 착용 시간(분)
    val readMagazineCnt     : Int, // 읽은 매거진 수
    val totalExp            : Int, // 총 획득 경험치
    val exp                 : List<Exp>,
): BaseModel