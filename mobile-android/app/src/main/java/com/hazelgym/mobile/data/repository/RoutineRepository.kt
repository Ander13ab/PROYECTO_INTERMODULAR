package com.hazelgym.mobile.data.repository

import com.hazelgym.mobile.data.model.RoutineResponse
import com.hazelgym.mobile.data.model.RoutineUpsertRequest
import com.hazelgym.mobile.data.model.SessionUser
import com.hazelgym.mobile.data.remote.RoutineApi

class RoutineRepository(
    private val routineApi: RoutineApi
) {
    suspend fun getRoutines(sessionUser: SessionUser): List<RoutineResponse> {
        return routineApi.getRoutines("${sessionUser.tokenType} ${sessionUser.token}")
    }

    suspend fun createRoutine(sessionUser: SessionUser, request: RoutineUpsertRequest): RoutineResponse {
        return routineApi.createRoutine("${sessionUser.tokenType} ${sessionUser.token}", request)
    }

    suspend fun updateRoutine(sessionUser: SessionUser, id: Long, request: RoutineUpsertRequest): RoutineResponse {
        return routineApi.updateRoutine("${sessionUser.tokenType} ${sessionUser.token}", id, request)
    }

    suspend fun deleteRoutine(sessionUser: SessionUser, id: Long) {
        routineApi.deleteRoutine("${sessionUser.tokenType} ${sessionUser.token}", id)
    }
}
