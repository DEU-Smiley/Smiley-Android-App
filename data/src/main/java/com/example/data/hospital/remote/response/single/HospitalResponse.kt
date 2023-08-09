package com.example.data.hospital.remote.response

import com.example.data.common.mapper.DataMapper
import com.example.data.common.network.BaseResponse
import com.example.domain.hospital.model.Hospital
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
class HospitalResponse(
    @SerializedName("hpid")
    val hpid: String, /* 병원 아이디 */
    @SerializedName("isPartner")
    val isPartner: Boolean, /* 파트너 여부 */
    @SerializedName("dutyName")
    val dutyName: String, /* 기관명 (병원 이름) */
    @SerializedName("dutyAddr")
    val dutyAddr: String, /* 병원 주소 */
    @SerializedName("dutyMapimg")
    val dutyMapimg: String, /* 병원 상세 주소 (간이 약도) */
    @SerializedName("dutyEtc")
    val dutyEtc: String, /* 비고 (ex: 주말 점심시간 안내 등) */
    @SerializedName("dutyTel1")
    val dutyTel1: String, /* 대표 전화번호 */
    @SerializedName("dutyTel3")
    val dutyTel3: String, /* 응급실 전화번호 */

    @SerializedName("wgs84Lat")
    val wgs84Lat: String, /* 병원 위도 */
    @SerializedName("wgs84Lon")
    val wgs84Lon: String, /* 병원 경도 */

    @SerializedName("dutyTime1s")
    val dutyTime1S: String, /* 진료시간 (월요일) 시작 시간 */
    @SerializedName("dutyTime1c")
    val dutyTime1C: String, /* 진료시간 (월요일) 종료 시간 */

    @SerializedName("dutyTime2s")
    val dutyTime2S: String, /* 진료시간 (화요일) 시작 시간*/
    @SerializedName("dutyTime2c")
    val dutyTime2C: String, /* 진료시간 (화요일) 종료 시간 */

    @SerializedName("dutyTime3s")
    val dutyTime3S: String, /* 진료시간 (수요일) 시작 시간 */
    @SerializedName("dutyTime3c")
    val dutyTime3C: String, /* 진료시간 (수요일) 종료 시간 */

    @SerializedName("dutyTime4s")
    val dutyTime4S: String, /* 진료시간 (목요일) 시작 시간 */
    @SerializedName("dutyTime4c")
    val dutyTime4C: String, /* 진료시간 (목요일) 종료 시간 */

    @SerializedName("dutyTime5s")
    val dutyTime5S: String, /* 진료시간 (금요일) 시작 시간 */
    @SerializedName("dutyTime5c")
    val dutyTime5C: String, /* 진료시간 (금요일) 종료 시간 */

    @SerializedName("dutyTime6s")
    val dutyTime6S: String, /* 진료시간 (토요일) 시작 시간 */
    @SerializedName("dutyTime6c")
    val dutyTime6C: String, /* 진료시간 (토요일) 종료 시간 */

    @SerializedName("dutyTime7s")
    val dutyTime7S: String, /* 진료시간 (일요일) 시작 시간 */
    @SerializedName("dutyTime7c")
    val dutyTime7C: String, /* 진료시간 (일요일) 종료 시간 */

    @SerializedName("dutyTime8s")
    val dutyTime8S: String, /* 진료시간 (공휴일) 시작 시간 */
    @SerializedName("dutyTime8c")
    val dutyTime8C: String, /* 진료시간 (공휴일) 종료 시간*/

    @SerializedName("postCdn1")
    val postCdn1: String, /* 우편번호 1 */
    @SerializedName("postCnd2")
    val postCdn2: String, /* 우편번호 2*/

    @SerializedName("dutyDiv")
    val dutyDiv: String, /* 병원 분류 (알파벳 1자) */
    @SerializedName("dutyDivNam")
    val dutyDivNam: String, /* 병원 분류명 (ex: 치과 의원) */
    @SerializedName("dutyEmcls")
    val dutyEmcls: String, /* 응급 의료 기관 코드 */
    @SerializedName("dutyEmclsName")
    val dutyEmclsName: String, /* 응급 의료 기관 코드명 */
    @SerializedName("dutyEryn")
    val dutyEryn: String, /* 응급실 운영 여부 (1/2) 1 : 운영, 2 : 비운영 */
    @SerializedName("dutyInf")
    val dutyInf: String, /* 기관 설명 상세 */
) : BaseResponse {
    companion object: DataMapper<HospitalResponse, Hospital>{
        override fun HospitalResponse.toDomainModel(): Hospital {
            return Hospital(
                hpid            = this.hpid,
                isPartner       = this.isPartner,
                name            = this.dutyName,
                address         = this.dutyAddr,
                detailAddress   = this.dutyMapimg,
                etc             = this.dutyEtc,
                tel             = this.dutyTel1,
                emergencyTel    = this.dutyTel3,
                wgs84Lat        = this.wgs84Lat,
                wgs84Lon        = this.wgs84Lon,
                monStartTime = this.dutyTime1S,
                monCloseTime = this.dutyTime1C,
                tueStartTime = this.dutyTime2S,
                tueCloseTime = this.dutyTime2C,
                wedStartTime = this.dutyTime3S,
                wedCloseTime = this.dutyTime3C,
                thuStartTime = this.dutyTime4S,
                thuCloseTime = this.dutyTime4C,
                friStartTime = this.dutyTime5S,
                friCloseTime = this.dutyTime5C,
                satStartTime = this.dutyTime6S,
                satCloseTime = this.dutyTime6C,
                sunStartTime = this.dutyTime7S,
                sunCloseTime = this.dutyTime7C,
                holiStartTime = this.dutyTime8S,
                holiCloseTime = this.dutyTime8C,
            )
        }
    }
}