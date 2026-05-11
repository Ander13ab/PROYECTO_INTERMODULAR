package com.hazelgym.mobile.data.remote

import com.hazelgym.mobile.data.model.AttendanceResponse
import retrofit2.http.GET
import retrofit2.http.Header

interface AttendanceApi {
    @GET("api/attendances")
    suspend fun getAttendances(
        @Header("Authorization") authorization: String
    ): List<AttendanceResponse>
}
