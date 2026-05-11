package com.hazelgym.mobile.data.remote

import com.hazelgym.mobile.data.model.GymClassResponse
import retrofit2.http.GET
import retrofit2.http.Header

interface GymClassApi {
    @GET("api/classes")
    suspend fun getClasses(
        @Header("Authorization") authorization: String
    ): List<GymClassResponse>
}
