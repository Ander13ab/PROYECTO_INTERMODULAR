package com.hazelgym.mobile.data.repository

import com.hazelgym.mobile.data.model.GymClassResponse
import com.hazelgym.mobile.data.model.GymClassUpsertRequest
import com.hazelgym.mobile.data.model.SessionUser
import com.hazelgym.mobile.data.remote.GymClassApi

class GymClassRepository(
    private val gymClassApi: GymClassApi
) {
    suspend fun getClasses(sessionUser: SessionUser): List<GymClassResponse> {
        return gymClassApi.getClasses("${sessionUser.tokenType} ${sessionUser.token}")
    }

    suspend fun createClass(sessionUser: SessionUser, request: GymClassUpsertRequest): GymClassResponse {
        return gymClassApi.createClass("${sessionUser.tokenType} ${sessionUser.token}", request)
    }

    suspend fun updateClass(sessionUser: SessionUser, id: Long, request: GymClassUpsertRequest): GymClassResponse {
        return gymClassApi.updateClass("${sessionUser.tokenType} ${sessionUser.token}", id, request)
    }

    suspend fun deleteClass(sessionUser: SessionUser, id: Long) {
        gymClassApi.deleteClass("${sessionUser.tokenType} ${sessionUser.token}", id)
    }
}
