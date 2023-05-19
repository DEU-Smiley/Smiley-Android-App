package com.example.domain.magazine.model

import com.example.domain.common.base.BaseModel
import kotlinx.android.parcel.Parcelize


@Parcelize
class MagazineList(
    val magazines: List<Magazine>
): BaseModel