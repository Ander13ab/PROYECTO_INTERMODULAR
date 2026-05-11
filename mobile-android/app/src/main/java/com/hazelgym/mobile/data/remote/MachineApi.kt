package com.hazelgym.mobile.data.remote

import com.hazelgym.mobile.data.model.MachineResponse
import retrofit2.http.GET
import retrofit2.http.Header

interface MachineApi {
    @GET("api/machines")
    suspend fun getMachines(
        @Header("Authorization") authorization: String
    ): List<MachineResponse>
}
