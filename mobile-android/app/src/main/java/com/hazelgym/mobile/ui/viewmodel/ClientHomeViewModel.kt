package com.hazelgym.mobile.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.hazelgym.mobile.data.model.GymClassResponse
import com.hazelgym.mobile.data.model.MachineResponse
import com.hazelgym.mobile.data.model.RoutineResponse
import com.hazelgym.mobile.data.remote.ApiClient
import com.hazelgym.mobile.data.repository.GymClassRepository
import com.hazelgym.mobile.data.repository.MachineRepository
import com.hazelgym.mobile.data.repository.RoutineRepository
import com.hazelgym.mobile.data.session.SessionStorage
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ClientHomeUiState(
    val userName: String = "",
    val role: String = "",
    val machines: List<MachineResponse> = emptyList(),
    val routines: List<RoutineResponse> = emptyList(),
    val classes: List<GymClassResponse> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class ClientHomeViewModel(application: Application) : AndroidViewModel(application) {
    private val sessionStorage = SessionStorage(application.applicationContext)
    private val machineRepository = MachineRepository(ApiClient.machineApi)
    private val routineRepository = RoutineRepository(ApiClient.routineApi)
    private val gymClassRepository = GymClassRepository(ApiClient.gymClassApi)

    private val _uiState = MutableStateFlow(ClientHomeUiState(isLoading = true))
    val uiState: StateFlow<ClientHomeUiState> = _uiState.asStateFlow()

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
                val routinesDeferred = async { routineRepository.getRoutines(session) }
                val classesDeferred = async { gymClassRepository.getClasses(session) }
                Triple(
                    machinesDeferred.await(),
                    routinesDeferred.await(),
                    classesDeferred.await()
                )
            }.onSuccess { (machines, routines, classes) ->
                _uiState.update {
                    it.copy(
                        machines = machines,
                        routines = routines,
                        classes = classes,
                        isLoading = false
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "No se pudieron cargar los datos del panel cliente"
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
                return ClientHomeViewModel(application) as T
            }
        }
    }
}
