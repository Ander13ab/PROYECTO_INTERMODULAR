package com.hazelgym.mobile.data.remote

import com.hazelgym.mobile.data.model.GymClassResponse
import com.hazelgym.mobile.data.model.GymClassUpsertRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface GymClassApi {
    @GET("api/classes")
    suspend fun getClasses(
        @Header("Authorization") authorization: String
    ): List<GymClassResponse>

    @POST("api/classes")
    suspend fun createClass(
        @Header("Authorization") authorization: String,
        @Body request: GymClassUpsertRequest
    ): GymClassResponse

    @PUT("api/classes/{id}")
    suspend fun updateClass(
        @Header("Authorization") authorization: String,
        @Path("id") id: Long,
        @Body request: GymClassUpsertRequest
    ): GymClassResponse

    @DELETE("api/classes/{id}")
    suspend fun deleteClass(
        @Header("Authorization") authorization: String,
        @Path("id") id: Long
    )
}
