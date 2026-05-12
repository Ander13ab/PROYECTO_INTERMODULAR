package com.hazelgym.mobile.data.remote

import com.hazelgym.mobile.data.model.ClassSessionResponse
import retrofit2.http.GET
import retrofit2.http.Header

interface ClassSessionApi {
    @GET("api/class-sessions")
    suspend fun getClassSessions(
        @Header("Authorization") authorization: String
    ): List<ClassSessionResponse>
}
