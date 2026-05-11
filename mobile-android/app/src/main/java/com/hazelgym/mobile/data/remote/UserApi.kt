package com.hazelgym.mobile.data.remote

import com.hazelgym.mobile.data.model.UserSummaryResponse
import retrofit2.http.GET
import retrofit2.http.Header

interface UserApi {
    @GET("api/users")
    suspend fun getUsers(
        @Header("Authorization") authorization: String
    ): List<UserSummaryResponse>
}
