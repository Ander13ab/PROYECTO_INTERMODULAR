package com.hazelgym.mobile.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.hazelgym.mobile.data.model.MachineResponse
import com.hazelgym.mobile.data.model.UserSummaryResponse
import com.hazelgym.mobile.data.remote.ApiClient
import com.hazelgym.mobile.data.repository.MachineRepository
import com.hazelgym.mobile.data.repository.UserRepository
import com.hazelgym.mobile.data.session.SessionStorage
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AdminHomeUiState(
    val userName: String = "",
    val role: String = "",
    val machines: List<MachineResponse> = emptyList(),
    val users: List<UserSummaryResponse> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class AdminHomeViewModel(application: Application) : AndroidViewModel(application) {
    private val sessionStorage = SessionStorage(application.applicationContext)
    private val machineRepository = MachineRepository(ApiClient.machineApi)
    private val userRepository = UserRepository(ApiClient.userApi)

    private val _uiState = MutableStateFlow(AdminHomeUiState(isLoading = true))
    val uiState: StateFlow<AdminHomeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val session = sessionStorage.session.filterNotNull().first()
            _uiState.update {
                it.copy(
                    userName = session.nombre,
                    role = session.role
                )
            }
            refresh()
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val session = sessionStorage.session.filterNotNull().first()
            runCatching {
                val machinesDeferred = async { machineRepository.getMachines(session) }
                val usersDeferred = async { userRepository.getUsers(session) }
                machinesDeferred.await() to usersDeferred.await()
            }.onSuccess { (machines, users) ->
                _uiState.update {
                    it.copy(
                        machines = machines,
                        users = users,
                        isLoading = false
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "No se pudieron cargar los datos del panel admin"
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
                return AdminHomeViewModel(application) as T
            }
        }
    }
}
