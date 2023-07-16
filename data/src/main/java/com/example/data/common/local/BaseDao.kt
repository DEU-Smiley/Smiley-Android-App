package com.example.data.common.local

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

interface BaseDao<T> {
    @Insert
    fun insert(obj: T)

    @Insert
    fun insertAll(objList: List<T>)

    @Update
    fun update(obj: T)

    @Delete
    fun delete(obj: T)
}