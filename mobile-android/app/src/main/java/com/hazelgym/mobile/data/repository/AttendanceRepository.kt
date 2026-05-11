package com.hazelgym.mobile.data.repository

import com.hazelgym.mobile.data.model.AttendanceResponse
import com.hazelgym.mobile.data.model.SessionUser
import com.hazelgym.mobile.data.remote.AttendanceApi

class AttendanceRepository(
    private val attendanceApi: AttendanceApi
) {
    suspend fun getAttendances(sessionUser: SessionUser): List<AttendanceResponse> {
        return attendanceApi.getAttendances("${sessionUser.tokenType} ${sessionUser.token}")
    }
}
