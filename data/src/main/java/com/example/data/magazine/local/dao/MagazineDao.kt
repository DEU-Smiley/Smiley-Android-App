package com.example.data.magazine.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.data.magazine.local.entity.MagazineEntity

@Dao
interface MagazineDao {

    @Query("SELECT * FROM magazine")
    suspend fun findAll(): List<MagazineEntity>
}