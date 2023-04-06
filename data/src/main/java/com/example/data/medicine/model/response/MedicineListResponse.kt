package com.example.data.medicine.model.response

import com.example.data.common.mapper.DataMapper
import com.example.data.common.network.BaseResponse
import com.example.data.medicine.model.response.MedicineResponse.Companion.toDataModel
import com.example.domain.medicine.model.MedicineList
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class MedicineListResponse (
    @SerializedName("medicines") val medicines: List<MedicineResponse>?,
): BaseResponse {
    companion object: DataMapper<MedicineListResponse, MedicineList> {
        override fun MedicineListResponse.toDataModel(): MedicineList {
            return MedicineList(
                medicines = medicines?.map { it.toDataModel() } ?: emptyList()
            )
        }
    }
}