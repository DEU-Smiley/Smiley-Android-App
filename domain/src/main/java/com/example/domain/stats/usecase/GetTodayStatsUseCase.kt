package com.example.domain.stats.usecase

import com.example.domain.common.base.ResponseState
import com.example.domain.stats.StatsRepository
import com.example.domain.stats.model.Stats
import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@Reusable
class GetTodayStatsUseCase @Inject constructor(
    private val statsRepository: StatsRepository
){
    suspend operator fun invoke(): Flow<ResponseState<Stats>> {
        return statsRepository.getStatsAt()
    }
}