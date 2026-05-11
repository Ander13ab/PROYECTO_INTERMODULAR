package com.hazelgym.mobile.data.repository

import com.hazelgym.mobile.data.model.GymClassResponse
import com.hazelgym.mobile.data.model.SessionUser
import com.hazelgym.mobile.data.remote.GymClassApi

class GymClassRepository(
    private val gymClassApi: GymClassApi
) {
    suspend fun getClasses(sessionUser: SessionUser): List<GymClassResponse> {
        return gymClassApi.getClasses("${sessionUser.tokenType} ${sessionUser.token}")
    }
}
