package com.hazelgym.mobile.data.repository

import com.hazelgym.mobile.data.model.RoutineResponse
import com.hazelgym.mobile.data.model.SessionUser
import com.hazelgym.mobile.data.remote.RoutineApi

class RoutineRepository(
    private val routineApi: RoutineApi
) {
    suspend fun getRoutines(sessionUser: SessionUser): List<RoutineResponse> {
        return routineApi.getRoutines("${sessionUser.tokenType} ${sessionUser.token}")
    }
}
