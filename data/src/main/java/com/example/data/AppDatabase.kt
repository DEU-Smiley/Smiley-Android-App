package com.example.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.core.content.ContextCompat
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.magazine.local.dao.MagazineDao
import com.example.data.magazine.local.entity.MagazineEntity
import com.example.data.medicine.local.dao.MedicineDao
import com.example.data.medicine.local.entity.MedicineEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

/**
 * entities에 포함된 클래스들이 각각의 테이블이 됨
 * 여러 개의 entity는 arrayOf() 안에 콤마로 구분해서 작성
 *
 * version은 테이블의 구조의 버전을 나타냄
 * 만약, 만약 구조가 바뀌었는데 버전이 같다면 에러 발생
 */
@Database(
    entities = [
        MedicineEntity::class,
        MagazineEntity::class
    ],
    version = 2,
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun medicineDao(): MedicineDao
    abstract fun magazineDao(): MagazineDao

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
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}