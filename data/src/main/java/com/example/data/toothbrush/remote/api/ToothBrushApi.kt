package com.example.data.toothbrush.remote.api

import com.example.data.toothbrush.remote.response.ToothBrushResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

internal interface ToothBrushApi {

    @Multipart
    @POST("ToothBrushStatus/{userId}")
    suspend fun checkToothBrushStatus(
        @Path("userId") userId: String,
        @Part imageFile: MultipartBody.Part
    ): Response<ToothBrushResponse>
}