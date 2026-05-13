package com.hazelgym.mobile.data.remote

import com.hazelgym.mobile.data.model.RoutineAssignmentUpsertRequest
import com.hazelgym.mobile.data.model.RoutineAssignmentResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface RoutineAssignmentApi {
    @GET("api/routine-assignments")
    suspend fun getRoutineAssignments(
        @Header("Authorization") authorization: String
    ): List<RoutineAssignmentResponse>

    @POST("api/routine-assignments")
    suspend fun createRoutineAssignment(
        @Header("Authorization") authorization: String,
        @Body request: RoutineAssignmentUpsertRequest
    ): RoutineAssignmentResponse

    @DELETE("api/routine-assignments/{id}")
    suspend fun deleteRoutineAssignment(
        @Header("Authorization") authorization: String,
        @Path("id") id: Long
    )
}
