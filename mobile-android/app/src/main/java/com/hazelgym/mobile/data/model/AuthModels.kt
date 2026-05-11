package com.hazelgym.mobile.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
