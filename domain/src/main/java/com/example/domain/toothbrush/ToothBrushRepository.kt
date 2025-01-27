package com.example.domain.toothbrush

import com.example.domain.common.base.ResponseState
import com.example.domain.toothbrush.model.ToothBrush
import kotlinx.coroutines.flow.Flow

interface ToothBrushRepository {
    suspend fun checkToothBrushStatus(userId: String, image:ByteArray): Flow<ResponseState<ToothBrush>>
}