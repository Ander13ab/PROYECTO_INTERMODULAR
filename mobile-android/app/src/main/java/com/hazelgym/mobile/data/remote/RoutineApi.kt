package com.hazelgym.mobile.data.remote

import com.hazelgym.mobile.data.model.RoutineResponse
import com.hazelgym.mobile.data.model.RoutineUpsertRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface RoutineApi {
    @GET("api/routines")
    suspend fun getRoutines(
        @Header("Authorization") authorization: String
    ): List<RoutineResponse>

    @POST("api/routines")
    suspend fun createRoutine(
        @Header("Authorization") authorization: String,
        @Body request: RoutineUpsertRequest
    ): RoutineResponse

    @PUT("api/routines/{id}")
    suspend fun updateRoutine(
        @Header("Authorization") authorization: String,
        @Path("id") id: Long,
        @Body request: RoutineUpsertRequest
    ): RoutineResponse

    @DELETE("api/routines/{id}")
    suspend fun deleteRoutine(
        @Header("Authorization") authorization: String,
        @Path("id") id: Long
    )
}
