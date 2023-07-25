package com.example.data.common.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

@Dao
interface BaseDao<T> {
    @Insert
    suspend fun insert(obj: T)

    @Insert
    suspend fun insertAll(objList: List<T>)

    @Update
    suspend fun update(obj: T)

    @Delete
    suspend fun delete(obj: T)
}