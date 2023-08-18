package com.example.data.magazine.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.data.common.local.BaseDao
import com.example.data.magazine.local.entity.MagazineEntity

@Dao
interface MagazineDao: BaseDao<MagazineEntity> {
  
    @Query("SELECT * FROM magazine")
    suspend fun findAll(): List<MagazineEntity>

    @Query("SELECT * FROM magazine ORDER BY id DESC LIMIT :cnt")
    suspend fun findByLimits(cnt: Int): List<MagazineEntity>
}