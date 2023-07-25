package com.example.domain.magazine.usecase

import com.example.domain.common.base.ResponseState
import com.example.domain.magazine.MagazineRepository
import com.example.domain.magazine.model.MagazineList
import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@Reusable
class GetAllMagazinesUseCase @Inject constructor(
    private val magazineRepository: MagazineRepository
){
    suspend operator fun invoke(): Flow<ResponseState<MagazineList>> {
        return magazineRepository.getAllMagazines()
    }
}