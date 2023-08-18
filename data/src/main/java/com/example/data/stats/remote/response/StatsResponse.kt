package com.example.data.stats.remote.response

import com.example.data.common.mapper.DataMapper
import com.example.data.common.network.BaseResponse
import com.example.domain.stats.model.Exp
import com.example.domain.stats.model.Stats
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class StatsResponse(
    @SerializedName("wearTime")             val wearTime            : Int, // 당일 착용 시간(분)
    @SerializedName("readMagazineNumber")   val readMagazineCnt     : Int, // 읽은 매거진 수
    @SerializedName("totalExp")             val totalExp            : Int, // 총 획득 경험치
    @SerializedName("totalWearTimeExp")     val totalWearTimeExp    : Int, // 일일 착용 시간으로 획득한 경험치
    @SerializedName("targetWearTimeEXP")    val targetWearTimeExp   : Int, // 목표 착용 시간으로 획득한 경험치
    @SerializedName("magazineExp")          val magazineExp         : Int, // 매거진 읽기로 획득한 경험치
    @SerializedName("badgeExp")             val badgeExp            : Int, // 뱃지로 획득한 경험치
    @SerializedName("commentExp")           val commentExp          : Int, // 댓글 채택으로 획득한 경험치
): BaseResponse {
    /**
     * 경험치의 비율을 반환하는 메소드
     */
    private fun getExpRatio(exp:Int): Int{
        return ((exp / totalExp.toDouble()) * 100).toInt()
    }
    
    companion object: DataMapper<StatsResponse, Stats>{
        override fun StatsResponse.toDomainModel(): Stats {
            val expList = mutableListOf<Exp>(
                Exp("착용 시간 ${getExpRatio(totalWearTimeExp)}%", exp = totalWearTimeExp),
                Exp("뱃지 획득 ${getExpRatio(badgeExp)}%", exp = badgeExp),
                Exp("목표 시간 ${getExpRatio(targetWearTimeExp)}%", exp = targetWearTimeExp),
                Exp("댓글 채택 ${getExpRatio(commentExp)}%" , exp = commentExp),
                Exp("매거진 ${getExpRatio(magazineExp)}%", exp = magazineExp),
            )

            return Stats(
                wearTime = wearTime,
                readMagazineCnt = readMagazineCnt,
                totalExp = totalExp,
                exp = expList,
            )
        }
    }
}