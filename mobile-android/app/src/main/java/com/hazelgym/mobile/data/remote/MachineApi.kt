package com.hazelgym.mobile.data.remote

import com.hazelgym.mobile.data.model.MachineUpsertRequest
import com.hazelgym.mobile.data.model.MachineResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface MachineApi {
    @GET("api/machines")
    suspend fun getMachines(
        @Header("Authorization") authorization: String
    ): List<MachineResponse>

    @POST("api/machines")
    suspend fun createMachine(
        @Header("Authorization") authorization: String,
        @Body request: MachineUpsertRequest
    ): MachineResponse

    @PUT("api/machines/{id}")
    suspend fun updateMachine(
        @Header("Authorization") authorization: String,
        @Path("id") id: Long,
        @Body request: MachineUpsertRequest
    ): MachineResponse

    @DELETE("api/machines/{id}")
    suspend fun deleteMachine(
        @Header("Authorization") authorization: String,
        @Path("id") id: Long
    )
}
