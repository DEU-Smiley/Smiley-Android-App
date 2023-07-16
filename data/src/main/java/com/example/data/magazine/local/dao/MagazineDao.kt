package com.example.data.magazine.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.common.local.BaseDao
import com.example.data.magazine.local.entity.MagazineEntity

@Dao
interface MagazineDao: BaseDao<MagazineEntity> {
    @Query("SELECT * FROM magazine")
    suspend fun findAll(): List<MagazineEntity>
}