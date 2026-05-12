package com.hazelgym.mobile.data.repository

import com.hazelgym.mobile.data.model.MachineUpsertRequest
import com.hazelgym.mobile.data.model.MachineResponse
import com.hazelgym.mobile.data.model.SessionUser
import com.hazelgym.mobile.data.remote.MachineApi

class MachineRepository(
    private val machineApi: MachineApi
) {
    suspend fun getMachines(sessionUser: SessionUser): List<MachineResponse> {
        return machineApi.getMachines("${sessionUser.tokenType} ${sessionUser.token}")
    }

    suspend fun createMachine(sessionUser: SessionUser, request: MachineUpsertRequest): MachineResponse {
        return machineApi.createMachine("${sessionUser.tokenType} ${sessionUser.token}", request)
    }

    suspend fun updateMachine(sessionUser: SessionUser, id: Long, request: MachineUpsertRequest): MachineResponse {
        return machineApi.updateMachine("${sessionUser.tokenType} ${sessionUser.token}", id, request)
    }

    suspend fun deleteMachine(sessionUser: SessionUser, id: Long) {
        machineApi.deleteMachine("${sessionUser.tokenType} ${sessionUser.token}", id)
    }
}
