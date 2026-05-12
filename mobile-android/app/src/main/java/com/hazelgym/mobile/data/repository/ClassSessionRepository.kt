package com.hazelgym.mobile.data.repository

import com.hazelgym.mobile.data.model.ClassSessionResponse
import com.hazelgym.mobile.data.model.SessionUser
import com.hazelgym.mobile.data.remote.ClassSessionApi

class ClassSessionRepository(
    private val classSessionApi: ClassSessionApi
) {
    suspend fun getClassSessions(sessionUser: SessionUser): List<ClassSessionResponse> {
        return classSessionApi.getClassSessions("${sessionUser.tokenType} ${sessionUser.token}")
    }
}
