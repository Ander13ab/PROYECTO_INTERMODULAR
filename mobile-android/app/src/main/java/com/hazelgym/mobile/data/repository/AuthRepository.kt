package com.hazelgym.mobile.data.repository

import com.hazelgym.mobile.data.model.AuthenticatedUserResponse
import com.hazelgym.mobile.data.model.LoginRequest
import com.hazelgym.mobile.data.model.SessionUser
import com.hazelgym.mobile.data.remote.AuthApi

class AuthRepository(
    private val authApi: AuthApi
) {
    suspend fun login(email: String, password: String): SessionUser {
        val response = authApi.login(LoginRequest(email = email, password = password))
        return SessionUser(
            token = response.token,
            tokenType = response.tokenType,
            userId = response.userId,
            nombre = response.nombre,
            email = response.email,
            role = response.role
        )
    }

    suspend fun getAuthenticatedUser(sessionUser: SessionUser): AuthenticatedUserResponse {
        return authApi.me("${sessionUser.tokenType} ${sessionUser.token}")
    }
}
