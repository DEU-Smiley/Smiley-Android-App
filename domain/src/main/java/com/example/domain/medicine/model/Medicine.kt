package com.example.domain.medicine.model

import com.example.domain.common.base.BaseModel
import kotlinx.parcelize.Parcelize

@Parcelize
class Medicine (
    var id          : Int,
    var itemCode    : String,
    var itemName    : String,
    var ingredient  : String,
    var type        : String,
) : BaseModel