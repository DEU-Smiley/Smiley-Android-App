package com.example.domain.magazine

import com.example.domain.common.base.ResponseState
import com.example.domain.magazine.model.MagazineList
import kotlinx.coroutines.flow.Flow

interface MagazineRepository {

    /**
     * 모든 매거진을 가져오는 메소드
     */
    suspend fun getAllMagazines(): Flow<ResponseState<MagazineList>>

    /**
     * 최근 매거진을 가져오는 메소드
     * @param cnt 가져올 개수
     * @return MagazineList
     */
    suspend fun getRecentMagazine(cnt: Int): Flow<ResponseState<MagazineList>>
}