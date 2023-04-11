package com.example.domain.hospital.model

import com.example.domain.common.base.BaseModel
import kotlinx.parcelize.Parcelize

@Parcelize
class Hospital(
    val hpid        : String,
    val dutyName    : String,
    val dutyAddress : String,
) : BaseModel