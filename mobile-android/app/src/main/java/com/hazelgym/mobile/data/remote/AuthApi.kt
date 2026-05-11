package com.hazelgym.mobile.data.remote

import com.hazelgym.mobile.data.model.AuthResponse
import com.hazelgym.mobile.data.model.AuthenticatedUserResponse
import com.hazelgym.mobile.data.model.LoginRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @GET("api/auth/me")
    suspend fun me(@Header("Authorization") authorization: String): AuthenticatedUserResponse
}
