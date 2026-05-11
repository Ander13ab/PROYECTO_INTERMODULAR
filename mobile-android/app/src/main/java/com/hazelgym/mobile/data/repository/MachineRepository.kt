package com.hazelgym.mobile.data.repository

import com.hazelgym.mobile.data.model.MachineResponse
import com.hazelgym.mobile.data.model.SessionUser
import com.hazelgym.mobile.data.remote.MachineApi

class MachineRepository(
    private val machineApi: MachineApi
) {
    suspend fun getMachines(sessionUser: SessionUser): List<MachineResponse> {
        return machineApi.getMachines("${sessionUser.tokenType} ${sessionUser.token}")
    }
}
