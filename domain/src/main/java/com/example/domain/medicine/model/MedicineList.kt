package com.example.domain.medicine.model

import com.example.domain.common.base.BaseModel
import kotlinx.parcelize.Parcelize

@Parcelize
class MedicineList (
    var medicines: List<Medicine>
) : BaseModel