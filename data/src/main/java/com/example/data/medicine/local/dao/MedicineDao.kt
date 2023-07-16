package com.example.data.medicine.local.dao

import androidx.room.*
import com.example.data.common.local.BaseDao
import com.example.data.medicine.local.entity.MedicineEntity

@Dao
interface MedicineDao : BaseDao<MedicineEntity> {

    @Query("DELETE FROM medicine WHERE item_code = :itemCode")
    suspend fun deleteByItemCode(itemCode: String)

    @Query("SELECT * FROM medicine")
    suspend fun findAll(): List<MedicineEntity>
}