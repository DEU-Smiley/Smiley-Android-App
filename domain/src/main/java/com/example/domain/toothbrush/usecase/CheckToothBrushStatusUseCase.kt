package com.example.domain.toothbrush.usecase

import android.graphics.Bitmap
import com.example.domain.common.base.ResponseState
import com.example.domain.toothbrush.ToothBrushRepository
import com.example.domain.toothbrush.model.ToothBrush
import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@Reusable
class CheckToothBrushStatusUseCase @Inject constructor(
    private val toothBrushRepository: ToothBrushRepository
){
    suspend operator fun invoke(userId: String, image: ByteArray): Flow<ResponseState<ToothBrush>>{
        return toothBrushRepository.checkToothBrushStatus(userId, image)
    }
}