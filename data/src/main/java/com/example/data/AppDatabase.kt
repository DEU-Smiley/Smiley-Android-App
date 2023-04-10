package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.medicine.local.dao.MedicineDao
import com.example.data.medicine.local.entity.MedicineEntity

/**
 * entities에 포함된 클래스들이 각각의 테이블이 됨
 * 여러 개의 entity는 arrayOf() 안에 콤마로 구분해서 작성
 *
 * version은 테이블의 구조의 버전을 나타냄
 * 만약, 만약 구조가 바뀌었는데 버전이 같다면 에러 발생
 */
@Database(entities = [MedicineEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun medicineDao(): MedicineDao

    companion object {
        /* Singleton */
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this){
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room
                .databaseBuilder(context, AppDatabase::class.java, "smiley-database")
                .build()
        }

    }
}