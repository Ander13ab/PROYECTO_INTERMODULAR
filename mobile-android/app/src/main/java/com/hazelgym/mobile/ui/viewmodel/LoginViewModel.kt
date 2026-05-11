package com.hazelgym.mobile.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.hazelgym.mobile.data.remote.ApiClient
import com.hazelgym.mobile.data.repository.AuthRepository
import com.hazelgym.mobile.data.session.SessionStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class LoginRole(
    val label: String,
    private vararg val backendAliases: String
) {
    CLIENTE("Cliente", "CLIENTE", "CLIENT"),
    ENTRENADOR("Entrenador", "ENTRENADOR", "TRAINER"),
    ADMIN("Admin", "ADMIN");

    fun matchesBackendRole(role: String): Boolean {
        return backendAliases.any { alias ->
            alias.equals(role, ignoreCase = true)
        }
    }
}

data class LoginUiState(
    val email: String = "admin@hazelgym.com",
    val password: String = "admin123",
    val selectedRole: LoginRole = LoginRole.ADMIN,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val authRepository = AuthRepository(ApiClient.authApi)
    private val sessionStorage = SessionStorage(application.applicationContext)

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun updateEmail(email: String) {
        _uiState.update { it.copy(email = email) }
    }

    fun updatePassword(password: String) {
        _uiState.update { it.copy(password = password) }
    }

    fun selectRole(role: LoginRole) {
        _uiState.update {
            it.copy(
                selectedRole = role,
                errorMessage = null
            )
        }
    }

    fun login() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            runCatching {
                authRepository.login(
                    email = uiState.value.email.trim(),
                    password = uiState.value.password
                )
            }.onSuccess { session ->
                val selectedRole = uiState.value.selectedRole
                if (!selectedRole.matchesBackendRole(session.role)) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Has elegido ${selectedRole.label}, pero esta cuenta pertenece al rol ${session.role}."
                        )
                    }
                    return@onSuccess
                }

                sessionStorage.save(session)
                _uiState.update { it.copy(isLoading = false) }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "No se pudo iniciar sesion"
                    )
                }
            }
        }
    }

    companion object {
        fun factory(): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: androidx.lifecycle.viewmodel.CreationExtras): T {
                val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                return LoginViewModel(application) as T
            }
        }
    }
}
