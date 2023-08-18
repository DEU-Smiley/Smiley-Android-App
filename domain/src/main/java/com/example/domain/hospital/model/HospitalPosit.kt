package com.example.domain.hospital.model

import com.example.domain.common.base.BaseModel
import kotlinx.parcelize.Parcelize

@Parcelize
class HospitalPosit(
    val hpid: String,
    val lat: Double,
    val lng: Double
) : BaseModel