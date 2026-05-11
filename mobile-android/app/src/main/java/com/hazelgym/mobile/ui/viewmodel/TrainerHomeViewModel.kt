package com.hazelgym.mobile.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.hazelgym.mobile.data.model.AttendanceResponse
import com.hazelgym.mobile.data.model.GymClassResponse
import com.hazelgym.mobile.data.model.RoutineAssignmentResponse
import com.hazelgym.mobile.data.remote.ApiClient
import com.hazelgym.mobile.data.repository.AttendanceRepository
import com.hazelgym.mobile.data.repository.GymClassRepository
import com.hazelgym.mobile.data.repository.RoutineAssignmentRepository
import com.hazelgym.mobile.data.session.SessionStorage
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TrainerHomeUiState(
    val userName: String = "",
    val email: String = "",
    val role: String = "",
    val classes: List<GymClassResponse> = emptyList(),
    val routineAssignments: List<RoutineAssignmentResponse> = emptyList(),
    val attendances: List<AttendanceResponse> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class TrainerHomeViewModel(application: Application) : AndroidViewModel(application) {
    private val sessionStorage = SessionStorage(application.applicationContext)
    private val gymClassRepository = GymClassRepository(ApiClient.gymClassApi)
    private val routineAssignmentRepository = RoutineAssignmentRepository(ApiClient.routineAssignmentApi)
    private val attendanceRepository = AttendanceRepository(ApiClient.attendanceApi)

    private val _uiState = MutableStateFlow(TrainerHomeUiState(isLoading = true))
    val uiState: StateFlow<TrainerHomeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val session = sessionStorage.session.filterNotNull().first()
            _uiState.update {
                it.copy(
                    userName = session.nombre,
                    email = session.email,
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
                val classesDeferred = async { gymClassRepository.getClasses(session) }
                val assignmentsDeferred = async { routineAssignmentRepository.getRoutineAssignments(session) }
                val attendancesDeferred = async { attendanceRepository.getAttendances(session) }
                Triple(
                    classesDeferred.await(),
                    assignmentsDeferred.await(),
                    attendancesDeferred.await()
                )
            }.onSuccess { (classes, assignments, attendances) ->
                _uiState.update {
                    it.copy(
                        classes = classes,
                        routineAssignments = assignments,
                        attendances = attendances,
                        isLoading = false
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "No se pudieron cargar los datos del panel entrenador"
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
                return TrainerHomeViewModel(application) as T
            }
        }
    }
}
