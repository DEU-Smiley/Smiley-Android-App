package com.example.domain.hospital.model

import com.example.domain.common.base.BaseModel
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime
import java.util.Calendar

@Parcelize
class Hospital (
    val hpid: String,           /* 병원 아이디 */
    val isPartner: Boolean,     /* 파트너 여부 */
    val name: String,           /* 기관명 (병원 이름) */
    val address: String,        /* 병원 주소 */
    val detailAddress: String,  /* 병원 상세 주소 (간이 약도) */
    val etc: String,            /* 비고 (ex: 주말 점심시간 안내 등) */
    val tel: String,            /* 대표 전화번호 */
    val emergencyTel: String,   /* 응급실 전화번호 */

    val wgs84Lat: String,       /* 병원 위도 */
    val wgs84Lon: String,       /* 병원 경도 */

    val monStartTime: String,   /* 진료시간 (월요일) 시작 시간 */
    val monCloseTime: String,   /* 진료시간 (월요일) 종료 시간 */

    val tueStartTime: String,   /* 진료시간 (화요일) 시작 시간 */
    val tueCloseTime: String,   /* 진료시간 (화요일) 종료 시간 */

    val wedStartTime: String,   /* 진료시간 (수요일) 시작 시간 */
    val wedCloseTime: String,   /* 진료시간 (수요일) 종료 시간 */

    val thuStartTime: String,   /* 진료시간 (목요일) 시작 시간 */
    val thuCloseTime: String,   /* 진료시간 (목요일) 종료 시간 */

    val friStartTime: String,   /* 진료시간 (금요일) 시작 시간 */
    val friCloseTime: String,   /* 진료시간 (금요일) 종료 시간 */

    val satStartTime: String,   /* 진료시간 (토요일) 시작 시간 */
    val satCloseTime: String,   /* 진료시간 (토요일) 종료 시간 */

    val sunStartTime: String,   /* 진료시간 (일요일) 시작 시간 */
    val sunCloseTime: String,   /* 진료시간 (일요일) 종료 시간 */

    val holiStartTime: String, /* 진료시간 (공휴일) 시작 시간 */
    val holiCloseTime: String, /* 진료시간 (공휴일) 종료 시간 */
) : BaseModel {
    fun getRunnginTimeAt(dayOfWeek: Int): Pair<String, String> {
        return when(dayOfWeek){
            Calendar.SUNDAY     -> Pair(sunStartTime, sunCloseTime)
            Calendar.MONDAY     -> Pair(monStartTime, monCloseTime)
            Calendar.TUESDAY    -> Pair(tueStartTime, tueCloseTime)
            Calendar.WEDNESDAY  -> Pair(wedStartTime, wedCloseTime)
            Calendar.THURSDAY   -> Pair(thuStartTime, thuCloseTime)
            Calendar.FRIDAY     -> Pair(friStartTime, friCloseTime)
            Calendar.SATURDAY   -> Pair(satStartTime, satCloseTime)
            else -> {Pair(holiStartTime,holiCloseTime)}
        }
    }

    /**
     * 요일별 진료 시간을 반환하는 메소드
     */
    fun parseRunningTimeAt(dayOfWeek: Int): String {
        val time = this.getRunnginTimeAt(dayOfWeek)
        if(time.first.isEmpty() || time.second.isEmpty()) return "휴진"

        val startTime = "${time.first.substring(0..1)}:${time.first.substring(2..3)}"
        val endTime = "${time.second.substring(0..1)}:${time.second.substring(2..3)}"

        return "$startTime ~ $endTime"
    }

    /**
     * ex)
     * startTime : 1000 -> 10:00
     * closeTime : 1830 -> 18:30
     */
    fun isRunningNow(): Boolean {
        val today = LocalDateTime.now()
        val now = String.format("%d%d", today.hour, today.minute).toInt()
        val runningTime = getRunnginTimeAt(today.dayOfWeek.value+1).also {
            if(it.first.isEmpty() || it.second.isEmpty()) return false
        }

        val (startTime, endTime) = runningTime.run {
            this.first.toInt() to this.second.toInt()
        }

        return (now in startTime..endTime)
    }
}