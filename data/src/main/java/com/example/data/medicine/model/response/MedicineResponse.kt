package com.example.data.medicine.model.response

import com.example.data.common.mapper.DataMapper
import com.example.data.common.network.BaseResponse
import com.example.domain.medicine.model.Medicine
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class MedicineResponse (
    @SerializedName("id")               var id          : Int,
    @SerializedName("itemCode")         var itemCode    : String,
    @SerializedName("itemName")         var itemName    : String,
    @SerializedName("mainIngredient")   var ingredient  : String,
    @SerializedName("professionalism")  var type        : String,
): BaseResponse {
    companion object: DataMapper<MedicineResponse, Medicine> {
        override fun MedicineResponse.toDataModel(): Medicine {
            return Medicine(
                id          = id,
                itemCode    = itemCode,
                itemName    = itemName,
                ingredient  = ingredient,
                type        = type
            )
        }
    }
}