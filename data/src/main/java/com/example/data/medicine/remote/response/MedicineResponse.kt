package com.example.data.medicine.remote.response

import com.example.data.common.mapper.DataMapper
import com.example.data.common.network.BaseResponse
import com.example.domain.medicine.model.Medicine
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class MedicineResponse (
    @SerializedName("itemCode")             var itemCode    : String,
    @SerializedName("itemNameKor")          var itemName    : String,
    @SerializedName("itemNameEng")          var itemNameEng  : String,
    @SerializedName("professionalism")      var type        : String,
): BaseResponse {
    companion object: DataMapper<MedicineResponse, Medicine> {
        override fun MedicineResponse.toDomainModel(): Medicine {
            return Medicine(
                itemCode    = itemCode,
                itemName    = itemName,
                itemNameEng = itemNameEng,
                type        = type
            )
        }
    }
}