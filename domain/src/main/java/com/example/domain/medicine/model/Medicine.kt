package com.example.domain.medicine.model

import com.example.domain.common.base.BaseModel
import kotlinx.parcelize.Parcelize

@Parcelize
class Medicine (
    var itemCode    : String,
    var itemName    : String,
    var itemNameEng : String,
    var type        : String,
) : BaseModel