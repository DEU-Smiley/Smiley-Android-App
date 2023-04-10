package com.example.data.medicine.local.dao

import androidx.room.*
import com.example.data.medicine.local.entity.MedicineEntity

@Dao
interface MedicineDao {
    @Insert
    suspend fun insert(medicine: MedicineEntity)

    @Insert
    suspend fun insertAll(medicines: List<MedicineEntity>)

    @Update
    suspend fun update(medicine: MedicineEntity)

    @Delete
    suspend fun delete(medicine: MedicineEntity)

    @Query("DELETE FROM medicine WHERE item_code = :itemCode")
    suspend fun deleteByItemCode(itemCode: String)

    @Query("SELECT * FROM medicine")
    suspend fun findAll(): List<MedicineEntity>
}