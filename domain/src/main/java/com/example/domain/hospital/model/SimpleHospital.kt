package com.example.domain.hospital.model

import com.example.domain.common.base.BaseModel
import kotlinx.parcelize.Parcelize

@Parcelize
class SimpleHospital(
    val hpid        : String,
    val dutyName    : String,
    val dutyAddress : String,
    val lat         : Double,
    val lng         : Double
) : BaseModel