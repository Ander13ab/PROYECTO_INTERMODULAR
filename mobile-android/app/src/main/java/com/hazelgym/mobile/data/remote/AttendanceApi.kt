package com.hazelgym.mobile.data.remote

import com.hazelgym.mobile.data.model.AttendanceCreateRequest
import com.hazelgym.mobile.data.model.AttendanceResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Body
import retrofit2.http.POST

interface AttendanceApi {
    @GET("api/attendances")
    suspend fun getAttendances(
        @Header("Authorization") authorization: String
    ): List<AttendanceResponse>

    @POST("api/attendances")
    suspend fun createAttendance(
        @Header("Authorization") authorization: String,
        @Body request: AttendanceCreateRequest
    ): AttendanceResponse
}
