package com.example.data.medicine.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.common.mapper.EntityMapper
import com.example.domain.medicine.model.Medicine

@Entity(tableName = "medicine")
class MedicineEntity(
    @PrimaryKey
    @ColumnInfo(name = "item_code")     val itemCode    : String,
    @ColumnInfo(name = "item_name")     val itemName    : String,
    @ColumnInfo(name = "item_name_eng") val itemNameEng : String,
    @ColumnInfo(name = "type")          val type        : String
) {
    companion object : EntityMapper<MedicineEntity, Medicine> {
        override fun MedicineEntity.mapToDomainModel(): Medicine {
            return Medicine(
                itemCode = this.itemCode,
                itemName = this.itemName,
                itemNameEng = this.itemNameEng,
                type = this.type
            )
        }

        override fun Medicine.mapFromDomainModel(): MedicineEntity {
            return MedicineEntity(
                itemCode = this.itemCode,
                itemName = this.itemName,
                itemNameEng = this.itemNameEng,
                type = this.type
            )
        }
    }
}
