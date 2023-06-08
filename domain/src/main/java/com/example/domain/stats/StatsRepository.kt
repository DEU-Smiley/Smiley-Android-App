package com.example.domain.stats

import com.example.domain.common.base.ResponseState
import com.example.domain.stats.model.Stats
import kotlinx.coroutines.flow.Flow

interface StatsRepository {
    suspend fun getStatsAt(/* 날짜 */): Flow<ResponseState<Stats>>
}