package com.example.data.toothbrush.repository

import android.graphics.Bitmap
import com.example.data.common.network.ApiResponse
import com.example.data.common.network.ApiResponseHandler
import com.example.data.common.network.ErrorResponse.Companion.toDomainModel
import com.example.data.toothbrush.remote.api.ToothBrushApi
import com.example.data.toothbrush.remote.response.ToothBrushResponse.Companion.toDomainModel
import com.example.domain.common.base.ResponseState
import com.example.domain.toothbrush.ToothBrushRepository
import com.example.domain.toothbrush.model.ToothBrush
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

internal class ToothBrushRepositoryImpl @Inject constructor(
    private val toothBrushApi: ToothBrushApi
): ToothBrushRepository {

    override suspend fun checkToothBrushStatus(
        userId: String,
        image: ByteArray
    ): Flow<ResponseState<ToothBrush>> {
        val requestBody = image.toRequestBody("image/png".toMediaTypeOrNull())
        val part = MultipartBody.Part.createFormData("imageFile", "image.png", requestBody)

        return flow {
            ApiResponseHandler().handle {
                toothBrushApi.checkToothBrushStatus(userId, part)
            }.onEach { result ->
                when(result){
                    is ApiResponse.Success -> {
                        emit(ResponseState.Success(result.data.toDomainModel()))
                    }
                    is ApiResponse.Error -> {
                        emit(ResponseState.Error(result.error.toDomainModel()))
                    }
                }
            }.collect()
        }
    }
}