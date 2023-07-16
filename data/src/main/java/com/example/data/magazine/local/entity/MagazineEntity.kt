package com.example.data.magazine.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.common.mapper.EntityMapper
import com.example.domain.magazine.model.Magazine

@Entity(tableName = "magazine")
class MagazineEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")            val id: Int,
    @ColumnInfo(name = "author")        val author: String,
    @ColumnInfo(name = "title")         val title: String,
    @ColumnInfo(name = "sub_title")     val subTitle: String,
    @ColumnInfo(name = "thumbnail")     val thumbnail: ByteArray,
    @ColumnInfo(name = "likes")         val likes: Int,
    @ColumnInfo(name = "view_cnt")      val viewCount: Int,
    @ColumnInfo(name = "content_url")    val contentUrl: String,
) {
    companion object : EntityMapper<MagazineEntity, Magazine> {
        override fun MagazineEntity.toModel(): Magazine {
            return Magazine(
                id = id,
                author = author,
                title = title,
                subTitle = subTitle,
                thumbnail = thumbnail,
                likes = likes,
                viewCount = viewCount,
                contentUrl = contentUrl
            )
        }

        override fun Magazine.toEntity(): MagazineEntity {
            return MagazineEntity(
                id = id,
                author = author,
                title = title,
                subTitle = subTitle,
                thumbnail = thumbnail,
                likes = likes,
                viewCount = viewCount,
                contentUrl = contentUrl
            )
        }
    }
}
