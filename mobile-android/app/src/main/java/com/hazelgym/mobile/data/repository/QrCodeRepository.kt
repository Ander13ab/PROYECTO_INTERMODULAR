package com.hazelgym.mobile.data.repository

import com.hazelgym.mobile.data.model.QrCodeCreateRequest
import com.hazelgym.mobile.data.model.QrCodeResponse
import com.hazelgym.mobile.data.model.SessionUser
import com.hazelgym.mobile.data.remote.QrCodeApi

class QrCodeRepository(
    private val qrCodeApi: QrCodeApi
) {
    suspend fun getQrCodes(sessionUser: SessionUser): List<QrCodeResponse> {
        return qrCodeApi.getQrCodes("${sessionUser.tokenType} ${sessionUser.token}")
    }

    suspend fun createEntryQrCode(sessionUser: SessionUser): QrCodeResponse {
        return qrCodeApi.createQrCode(
            authorization = "${sessionUser.tokenType} ${sessionUser.token}",
            request = QrCodeCreateRequest(
                tipo = "ENTRY",
                esEntradaGimnasio = true
            )
        )
    }

    suspend fun createMachineQrCode(sessionUser: SessionUser, machineId: Long): QrCodeResponse {
        return qrCodeApi.createQrCode(
            authorization = "${sessionUser.tokenType} ${sessionUser.token}",
            request = QrCodeCreateRequest(
                tipo = "MACHINE",
                esEntradaGimnasio = false,
                maquinaId = machineId
            )
        )
    }

    suspend fun createClassSessionQrCode(sessionUser: SessionUser, classSessionId: Long): QrCodeResponse {
        return qrCodeApi.createQrCode(
            authorization = "${sessionUser.tokenType} ${sessionUser.token}",
            request = QrCodeCreateRequest(
                tipo = "CLASS_SESSION",
                esEntradaGimnasio = false,
                sesionClaseId = classSessionId
            )
        )
    }
}
