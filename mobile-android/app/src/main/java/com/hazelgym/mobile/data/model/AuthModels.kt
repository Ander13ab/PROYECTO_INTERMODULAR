package com.hazelgym.mobile.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class AuthResponse(
    val token: String,
    val tokenType: String,
    val userId: Long,
    val nombre: String,
    val email: String,
    val role: String
)

@Serializable
data class AuthenticatedUserResponse(
    val id: Long,
    val nombre: String,
    val email: String,
    val role: String,
    val activo: Boolean
)

@Serializable
data class SessionUser(
    val token: String,
    val tokenType: String,
    val userId: Long,
    val nombre: String,
    val email: String,
    val role: String
)

@Serializable
data class MachineResponse(
    val id: Long,
    val nombre: String,
    val descripcion: String? = null,
    val grupoMuscular: String? = null,
    val instrucciones: String? = null,
    val nivel: String? = null,
    val advertenciaSeguridad: String? = null,
    val imagenUrl: String? = null,
    @SerialName("estado") val estado: String
)

@Serializable
data class UserSummaryResponse(
    val id: Long,
    val nombre: String,
    val email: String,
    val role: String,
    val activo: Boolean
)

@Serializable
data class RoutineResponse(
    val id: Long,
    val nombre: String,
    val descripcion: String? = null,
    val entrenadorId: Long,
    val entrenadorNombre: String,
    val fechaCreacion: String? = null
)

@Serializable
data class GymClassResponse(
    val id: Long,
    val nombre: String,
    val descripcion: String? = null,
    val duracion: Int? = null,
    val entrenadorId: Long,
    val entrenadorNombre: String,
    val activa: Boolean
)

@Serializable
data class ClassSessionResponse(
    val id: Long,
    val gymClassId: Long,
    val gymClassName: String,
    val fecha: String,
    val horaInicio: String,
    val horaFin: String
)

@Serializable
data class RoutineAssignmentResponse(
    val id: Long,
    val routineId: Long,
    val routineName: String,
    val clientId: Long,
    val clientName: String,
    val fechaAsignacion: String? = null
)

@Serializable
data class AttendanceResponse(
    val id: Long,
    val usuarioId: Long,
    val usuarioNombre: String,
    val qrCodeId: Long,
    val qrType: String,
    val fechaHora: String? = null
)

@Serializable
data class AttendanceCreateRequest(
    val usuarioId: Long,
    val qrCodeId: Long
)

@Serializable
data class QrCodeResponse(
    val id: Long,
    val tipo: String,
    val esEntradaGimnasio: Boolean,
    val maquinaId: Long? = null,
    val maquinaNombre: String? = null,
    val sesionClaseId: Long? = null,
    val sesionClaseResumen: String? = null
)

@Serializable
data class QrCodeCreateRequest(
    val tipo: String,
    val esEntradaGimnasio: Boolean,
    val maquinaId: Long? = null,
    val sesionClaseId: Long? = null
)
