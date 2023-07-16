package com.example.data.magazine.repository

import android.util.Log
import com.example.data.magazine.local.dao.MagazineDao
import com.example.data.magazine.local.entity.MagazineEntity.Companion.toModel
import com.example.domain.common.base.NetworkError
import com.example.domain.common.base.ResponseState
import com.example.domain.magazine.MagazineRepository
import com.example.domain.magazine.model.MagazineList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MagazineRepositoryImpl @Inject constructor(
    private val magazineDao: MagazineDao
): MagazineRepository {
    override suspend fun getRecentMagazine(cnt: Int): Flow<ResponseState<MagazineList>> {
        return flow {
            try {
                val magazines = magazineDao.findAll().map { it.toModel() }
                emit(ResponseState.Success(MagazineList(magazines)))
            } catch (e: Exception) {
                Log.e("MagazineRepositoryImpl", e.message.toString())
                emit(
                    ResponseState.Error(
                        NetworkError(
                            "MAGAZINE ERROR",
                            "500",
                            "매거진 조회에 실패 하였습니다."
                        )
                    )
                )
            }
        }
    }
}