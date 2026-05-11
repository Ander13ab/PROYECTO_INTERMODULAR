package com.hazelgym.mobile.data.remote

import com.hazelgym.mobile.data.model.RoutineAssignmentResponse
import retrofit2.http.GET
import retrofit2.http.Header

interface RoutineAssignmentApi {
    @GET("api/routine-assignments")
    suspend fun getRoutineAssignments(
        @Header("Authorization") authorization: String
    ): List<RoutineAssignmentResponse>
}
