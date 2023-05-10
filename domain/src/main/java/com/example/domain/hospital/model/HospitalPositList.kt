package com.example.domain.hospital.model

import com.example.domain.common.base.BaseModel
import kotlinx.android.parcel.Parcelize


@Parcelize
class HospitalPositList(
    val hospitalPosits: List<HospitalPosit>
) : BaseModel