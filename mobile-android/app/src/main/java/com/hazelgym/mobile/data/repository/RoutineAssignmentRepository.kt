package com.hazelgym.mobile.data.repository

import com.hazelgym.mobile.data.model.RoutineAssignmentResponse
import com.hazelgym.mobile.data.model.SessionUser
import com.hazelgym.mobile.data.remote.RoutineAssignmentApi

class RoutineAssignmentRepository(
    private val routineAssignmentApi: RoutineAssignmentApi
) {
    suspend fun getRoutineAssignments(sessionUser: SessionUser): List<RoutineAssignmentResponse> {
        return routineAssignmentApi.getRoutineAssignments("${sessionUser.tokenType} ${sessionUser.token}")
    }
}
