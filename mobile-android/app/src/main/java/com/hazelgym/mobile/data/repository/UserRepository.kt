package com.hazelgym.mobile.data.repository

import com.hazelgym.mobile.data.model.SessionUser
import com.hazelgym.mobile.data.model.UserSummaryResponse
import com.hazelgym.mobile.data.remote.UserApi

class UserRepository(
    private val userApi: UserApi
) {
    suspend fun getUsers(sessionUser: SessionUser): List<UserSummaryResponse> {
        return userApi.getUsers("${sessionUser.tokenType} ${sessionUser.token}")
    }
}
