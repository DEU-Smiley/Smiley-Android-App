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

        fun getMagazineData(context: Context): List<MagazineEntity> {
            return mutableListOf<MagazineEntity>().apply {
                add(
                    MagazineEntity(
                        id = 1,
                        author = "test",
                        title = "국산 vs 수입산 임플란트,\n뭐가 더 좋아요?",
                        subTitle = "치아 교정시 주의사항",
                        thumbnail = ByteArrayOutputStream().run
                        {
                            val bitmap = (ContextCompat.getDrawable(context, R.drawable.mock_thumb_magazine_1) as BitmapDrawable).bitmap
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, this)
                            toByteArray()
                        },
                        likes = 923,
                        viewCount = 1023,
                        contentUrl = "https://deu-smiley.github.io/Smiley-Magazine/magazine_1"
                    )
                )
                add(
                    MagazineEntity(
                        id = 2,
                        author = "test",
                        title = "꼭 알아야 할\n임플란트 상식!",
                        subTitle = "치아 관리 방법",
                        thumbnail = ByteArrayOutputStream().run {
                            val bitmap = (ContextCompat.getDrawable(context, R.drawable.mock_thumb_magazine_2) as BitmapDrawable).bitmap
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, this)
                            toByteArray()
                        },
                        likes = 923,
                        viewCount = 1023,
                        contentUrl = "https://deu-smiley.github.io/Smiley-Magazine/magazine_2"
                    )
                )
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room
                .databaseBuilder(context, AppDatabase::class.java, "smiley-database")
                .addCallback(object : Callback(){
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)

                        GlobalScope.launch {
                            withContext(Dispatchers.IO){
                                getInstance(context).run {
                                    magazineDao().insertAll(
                                        getMagazineData(context)
                                    )
                                }
                            }
                        }
                    }
                })
                .fallbackToDestructiveMigration()
                .build()
        }

    }
}