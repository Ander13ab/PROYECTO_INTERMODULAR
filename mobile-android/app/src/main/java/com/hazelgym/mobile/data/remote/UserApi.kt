package com.hazelgym.mobile.data.remote

import com.hazelgym.mobile.data.model.UserSummaryResponse
import com.hazelgym.mobile.data.model.UserUpsertRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserApi {
    @GET("api/users")
    suspend fun getUsers(
        @Header("Authorization") authorization: String
    ): List<UserSummaryResponse>

    @POST("api/users")
    suspend fun createUser(
        @Header("Authorization") authorization: String,
        @Body request: UserUpsertRequest
    ): UserSummaryResponse

    @PUT("api/users/{id}")
    suspend fun updateUser(
        @Header("Authorization") authorization: String,
        @Path("id") id: Long,
        @Body request: UserUpsertRequest
    ): UserSummaryResponse

    @DELETE("api/users/{id}")
    suspend fun deleteUser(
        @Header("Authorization") authorization: String,
        @Path("id") id: Long
    )
}
