package com.hazelgym.mobile.data.repository

import com.hazelgym.mobile.data.model.SessionUser
import com.hazelgym.mobile.data.model.UserSummaryResponse
import com.hazelgym.mobile.data.model.UserUpsertRequest
import com.hazelgym.mobile.data.remote.UserApi

class UserRepository(
    private val userApi: UserApi
) {
    suspend fun getUsers(sessionUser: SessionUser): List<UserSummaryResponse> {
        return userApi.getUsers("${sessionUser.tokenType} ${sessionUser.token}")
    }

    suspend fun createUser(sessionUser: SessionUser, request: UserUpsertRequest): UserSummaryResponse {
        return userApi.createUser("${sessionUser.tokenType} ${sessionUser.token}", request)
    }

    suspend fun updateUser(sessionUser: SessionUser, id: Long, request: UserUpsertRequest): UserSummaryResponse {
        return userApi.updateUser("${sessionUser.tokenType} ${sessionUser.token}", id, request)
    }

    suspend fun deleteUser(sessionUser: SessionUser, id: Long) {
        userApi.deleteUser("${sessionUser.tokenType} ${sessionUser.token}", id)
    }
}
