package com.hazelgym.mobile.data.remote

import com.hazelgym.mobile.data.model.RoutineResponse
import retrofit2.http.GET
import retrofit2.http.Header

interface RoutineApi {
    @GET("api/routines")
    suspend fun getRoutines(
        @Header("Authorization") authorization: String
    ): List<RoutineResponse>
}
