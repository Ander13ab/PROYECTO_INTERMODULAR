package com.hazelgym.mobile.data.remote

import com.hazelgym.mobile.data.model.QrCodeCreateRequest
import com.hazelgym.mobile.data.model.QrCodeResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface QrCodeApi {
    @GET("api/qr-codes")
    suspend fun getQrCodes(
        @Header("Authorization") authorization: String
    ): List<QrCodeResponse>

    @POST("api/qr-codes")
    suspend fun createQrCode(
        @Header("Authorization") authorization: String,
        @Body request: QrCodeCreateRequest
    ): QrCodeResponse
}
