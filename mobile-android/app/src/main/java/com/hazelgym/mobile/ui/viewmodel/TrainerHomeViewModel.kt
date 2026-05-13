package com.hazelgym.mobile.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.hazelgym.mobile.data.model.AttendanceResponse
import com.hazelgym.mobile.data.model.GymClassResponse
import com.hazelgym.mobile.data.model.GymClassUpsertRequest
import com.hazelgym.mobile.data.model.RoutineResponse
import com.hazelgym.mobile.data.model.RoutineAssignmentUpsertRequest
import com.hazelgym.mobile.data.model.RoutineUpsertRequest
import com.hazelgym.mobile.data.model.RoutineAssignmentResponse
import com.hazelgym.mobile.data.model.UserSummaryResponse
import com.hazelgym.mobile.data.remote.ApiClient
import com.hazelgym.mobile.data.repository.AttendanceRepository
import com.hazelgym.mobile.data.repository.GymClassRepository
import com.hazelgym.mobile.data.repository.RoutineRepository
import com.hazelgym.mobile.data.repository.RoutineAssignmentRepository
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

data class TrainerHomeUiState(
    val userId: Long = 0,
    val userName: String = "",
    val email: String = "",
    val role: String = "",
    val classes: List<GymClassResponse> = emptyList(),
    val routines: List<RoutineResponse> = emptyList(),
    val clients: List<UserSummaryResponse> = emptyList(),
    val routineAssignments: List<RoutineAssignmentResponse> = emptyList(),
    val attendances: List<AttendanceResponse> = emptyList(),
    val classEditingId: Long? = null,
    val classNameInput: String = "",
    val classDescriptionInput: String = "",
    val classDurationInput: String = "60",
    val classActiveInput: Boolean = true,
    val classSaveMessage: String? = null,
    val isSavingClass: Boolean = false,
    val routineEditingId: Long? = null,
    val routineNameInput: String = "",
    val routineDescriptionInput: String = "",
    val routineSaveMessage: String? = null,
    val isSavingRoutine: Boolean = false,
    val assignmentEditingId: Long? = null,
    val assignmentRoutineIdInput: String = "",
    val assignmentClientIdInput: String = "",
    val assignmentSaveMessage: String? = null,
    val isSavingAssignment: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class TrainerHomeViewModel(application: Application) : AndroidViewModel(application) {
    private val sessionStorage = SessionStorage(application.applicationContext)
    private val gymClassRepository = GymClassRepository(ApiClient.gymClassApi)
    private val routineRepository = RoutineRepository(ApiClient.routineApi)
    private val routineAssignmentRepository = RoutineAssignmentRepository(ApiClient.routineAssignmentApi)
    private val attendanceRepository = AttendanceRepository(ApiClient.attendanceApi)
    private val userRepository = UserRepository(ApiClient.userApi)

    private val _uiState = MutableStateFlow(TrainerHomeUiState(isLoading = true))
    val uiState: StateFlow<TrainerHomeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            runCatching {
                sessionStorage.session.filterNotNull().first()
            }.onSuccess { session ->
                _uiState.update {
                    it.copy(
                        userId = session.userId,
                        userName = session.nombre,
                        email = session.email,
                        role = session.role
                    )
                }
                refresh()
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "No se pudo recuperar la sesion del entrenador"
                    )
                }
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            runCatching {
                val session = sessionStorage.session.filterNotNull().first()
                val classesDeferred = async { gymClassRepository.getClasses(session) }
                val routinesDeferred = async { routineRepository.getRoutines(session) }
                val clientsDeferred = async { userRepository.getUsers(session) }
                val assignmentsDeferred = async { routineAssignmentRepository.getRoutineAssignments(session) }
                val attendancesDeferred = async { attendanceRepository.getAttendances(session) }
                TrainerDashboardPayload(
                    classes = classesDeferred.await().filter { it.entrenadorId == session.userId },
                    routines = routinesDeferred.await().filter { it.entrenadorId == session.userId },
                    clients = clientsDeferred.await().filter { it.role.equals("CLIENT", ignoreCase = true) },
                    routineAssignments = assignmentsDeferred.await(),
                    attendances = attendancesDeferred.await()
                )
            }.onSuccess { payload ->
                _uiState.update {
                    it.copy(
                        classes = payload.classes,
                        routines = payload.routines,
                        clients = payload.clients,
                        routineAssignments = payload.routineAssignments,
                        attendances = payload.attendances,
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

    fun startNewAssignment() {
        _uiState.update {
            it.copy(
                assignmentEditingId = null,
                assignmentRoutineIdInput = "",
                assignmentClientIdInput = "",
                assignmentSaveMessage = null
            )
        }
    }

    fun editAssignment(assignment: RoutineAssignmentResponse) {
        _uiState.update {
            it.copy(
                assignmentEditingId = assignment.id,
                assignmentRoutineIdInput = assignment.routineId.toString(),
                assignmentClientIdInput = assignment.clientId.toString(),
                assignmentSaveMessage = "Asignacion #${assignment.id} seleccionada"
            )
        }
    }

    fun updateAssignmentRoutineIdInput(value: String) {
        _uiState.update { it.copy(assignmentRoutineIdInput = value, assignmentSaveMessage = null) }
    }

    fun updateAssignmentClientIdInput(value: String) {
        _uiState.update { it.copy(assignmentClientIdInput = value, assignmentSaveMessage = null) }
    }

    fun selectAssignmentRoutine(routineId: Long) {
        _uiState.update {
            it.copy(
                assignmentRoutineIdInput = routineId.toString(),
                assignmentSaveMessage = null
            )
        }
    }

    fun selectAssignmentClient(clientId: Long) {
        _uiState.update {
            it.copy(
                assignmentClientIdInput = clientId.toString(),
                assignmentSaveMessage = null
            )
        }
    }

    fun saveAssignment() {
        val routineId = uiState.value.assignmentRoutineIdInput.trim().toLongOrNull()
        val clientId = uiState.value.assignmentClientIdInput.trim().toLongOrNull()

        if (routineId == null) {
            _uiState.update { it.copy(assignmentSaveMessage = "Selecciona una rutina valida.") }
            return
        }
        if (clientId == null) {
            _uiState.update { it.copy(assignmentSaveMessage = "Selecciona un cliente valido.") }
            return
        }

        val request = RoutineAssignmentUpsertRequest(
            routineId = routineId,
            clientId = clientId
        )

        viewModelScope.launch {
            _uiState.update { it.copy(isSavingAssignment = true, assignmentSaveMessage = null) }
            val session = sessionStorage.session.filterNotNull().first()
            runCatching {
                routineAssignmentRepository.createRoutineAssignment(session, request)
            }.onSuccess { assignment ->
                _uiState.update {
                    it.copy(
                        isSavingAssignment = false,
                        assignmentEditingId = null,
                        assignmentRoutineIdInput = "",
                        assignmentClientIdInput = "",
                        assignmentSaveMessage = "Asignacion #${assignment.id} guardada correctamente."
                    )
                }
                refresh()
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isSavingAssignment = false,
                        assignmentSaveMessage = error.message ?: "No se pudo crear la asignacion."
                    )
                }
            }
        }
    }

    fun deleteEditingAssignment() {
        val assignmentId = uiState.value.assignmentEditingId
        if (assignmentId == null) {
            _uiState.update { it.copy(assignmentSaveMessage = "Selecciona una asignacion para eliminarla.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSavingAssignment = true, assignmentSaveMessage = null) }
            val session = sessionStorage.session.filterNotNull().first()
            runCatching {
                routineAssignmentRepository.deleteRoutineAssignment(session, assignmentId)
            }.onSuccess {
                _uiState.update {
                    it.copy(
                        isSavingAssignment = false,
                        assignmentEditingId = null,
                        assignmentRoutineIdInput = "",
                        assignmentClientIdInput = "",
                        assignmentSaveMessage = "Asignacion eliminada correctamente."
                    )
                }
                refresh()
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isSavingAssignment = false,
                        assignmentSaveMessage = error.message ?: "No se pudo eliminar la asignacion."
                    )
                }
            }
        }
    }

    fun startNewClass() {
        _uiState.update {
            it.copy(
                classEditingId = null,
                classNameInput = "",
                classDescriptionInput = "",
                classDurationInput = "60",
                classActiveInput = true,
                classSaveMessage = null
            )
        }
    }

    fun editClass(gymClass: GymClassResponse) {
        _uiState.update {
            it.copy(
                classEditingId = gymClass.id,
                classNameInput = gymClass.nombre,
                classDescriptionInput = gymClass.descripcion.orEmpty(),
                classDurationInput = gymClass.duracion?.toString() ?: "60",
                classActiveInput = gymClass.activa,
                classSaveMessage = "Editando clase #${gymClass.id}"
            )
        }
    }

    fun updateClassNameInput(value: String) {
        _uiState.update { it.copy(classNameInput = value, classSaveMessage = null) }
    }

    fun updateClassDescriptionInput(value: String) {
        _uiState.update { it.copy(classDescriptionInput = value, classSaveMessage = null) }
    }

    fun updateClassDurationInput(value: String) {
        _uiState.update { it.copy(classDurationInput = value, classSaveMessage = null) }
    }

    fun updateClassActiveInput(value: Boolean) {
        _uiState.update { it.copy(classActiveInput = value, classSaveMessage = null) }
    }

    fun saveClass() {
        val name = uiState.value.classNameInput.trim()
        val duration = uiState.value.classDurationInput.trim().toIntOrNull()
        val editingId = uiState.value.classEditingId

        if (name.isBlank()) {
            _uiState.update { it.copy(classSaveMessage = "Introduce un nombre para la clase.") }
            return
        }
        if (duration == null || duration !in 1..300) {
            _uiState.update { it.copy(classSaveMessage = "La duracion debe estar entre 1 y 300 minutos.") }
            return
        }

        val request = GymClassUpsertRequest(
            nombre = name,
            descripcion = uiState.value.classDescriptionInput.trim().ifBlank { null },
            duracion = duration,
            entrenadorId = uiState.value.userId,
            activa = uiState.value.classActiveInput
        )

        viewModelScope.launch {
            _uiState.update { it.copy(isSavingClass = true, classSaveMessage = null) }
            val session = sessionStorage.session.filterNotNull().first()
            runCatching {
                if (editingId == null) {
                    gymClassRepository.createClass(session, request)
                } else {
                    gymClassRepository.updateClass(session, editingId, request)
                }
            }.onSuccess { gymClass ->
                _uiState.update {
                    it.copy(
                        isSavingClass = false,
                        classEditingId = null,
                        classNameInput = "",
                        classDescriptionInput = "",
                        classDurationInput = "60",
                        classActiveInput = true,
                        classSaveMessage = "Clase #${gymClass.id} guardada correctamente."
                    )
                }
                refresh()
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isSavingClass = false,
                        classSaveMessage = error.message ?: "No se pudo guardar la clase."
                    )
                }
            }
        }
    }

    fun deleteEditingClass() {
        val classId = uiState.value.classEditingId
        if (classId == null) {
            _uiState.update { it.copy(classSaveMessage = "Selecciona una clase para eliminarla.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSavingClass = true, classSaveMessage = null) }
            val session = sessionStorage.session.filterNotNull().first()
            runCatching {
                gymClassRepository.deleteClass(session, classId)
            }.onSuccess {
                _uiState.update {
                    it.copy(
                        isSavingClass = false,
                        classEditingId = null,
                        classNameInput = "",
                        classDescriptionInput = "",
                        classDurationInput = "60",
                        classActiveInput = true,
                        classSaveMessage = "Clase eliminada correctamente."
                    )
                }
                refresh()
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isSavingClass = false,
                        classSaveMessage = error.message ?: "No se pudo eliminar la clase."
                    )
                }
            }
        }
    }

    fun startNewRoutine() {
        _uiState.update {
            it.copy(
                routineEditingId = null,
                routineNameInput = "",
                routineDescriptionInput = "",
                routineSaveMessage = null
            )
        }
    }

    fun editRoutine(routine: RoutineResponse) {
        _uiState.update {
            it.copy(
                routineEditingId = routine.id,
                routineNameInput = routine.nombre,
                routineDescriptionInput = routine.descripcion.orEmpty(),
                routineSaveMessage = "Editando rutina #${routine.id}"
            )
        }
    }

    fun updateRoutineNameInput(value: String) {
        _uiState.update { it.copy(routineNameInput = value, routineSaveMessage = null) }
    }

    fun updateRoutineDescriptionInput(value: String) {
        _uiState.update { it.copy(routineDescriptionInput = value, routineSaveMessage = null) }
    }

    fun saveRoutine() {
        val name = uiState.value.routineNameInput.trim()
        val editingId = uiState.value.routineEditingId

        if (name.isBlank()) {
            _uiState.update { it.copy(routineSaveMessage = "Introduce un nombre para la rutina.") }
            return
        }

        val request = RoutineUpsertRequest(
            nombre = name,
            descripcion = uiState.value.routineDescriptionInput.trim().ifBlank { null },
            entrenadorId = uiState.value.userId
        )

        viewModelScope.launch {
            _uiState.update { it.copy(isSavingRoutine = true, routineSaveMessage = null) }
            val session = sessionStorage.session.filterNotNull().first()
            runCatching {
                if (editingId == null) {
                    routineRepository.createRoutine(session, request)
                } else {
                    routineRepository.updateRoutine(session, editingId, request)
                }
            }.onSuccess { routine ->
                _uiState.update {
                    it.copy(
                        isSavingRoutine = false,
                        routineEditingId = null,
                        routineNameInput = "",
                        routineDescriptionInput = "",
                        routineSaveMessage = "Rutina #${routine.id} guardada correctamente."
                    )
                }
                refresh()
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isSavingRoutine = false,
                        routineSaveMessage = error.message ?: "No se pudo guardar la rutina."
                    )
                }
            }
        }
    }

    fun deleteEditingRoutine() {
        val routineId = uiState.value.routineEditingId
        if (routineId == null) {
            _uiState.update { it.copy(routineSaveMessage = "Selecciona una rutina para eliminarla.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSavingRoutine = true, routineSaveMessage = null) }
            val session = sessionStorage.session.filterNotNull().first()
            runCatching {
                routineRepository.deleteRoutine(session, routineId)
            }.onSuccess {
                _uiState.update {
                    it.copy(
                        isSavingRoutine = false,
                        routineEditingId = null,
                        routineNameInput = "",
                        routineDescriptionInput = "",
                        routineSaveMessage = "Rutina eliminada correctamente."
                    )
                }
                refresh()
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isSavingRoutine = false,
                        routineSaveMessage = error.message ?: "No se pudo eliminar la rutina."
                    )
                }
            }
        }
    }

    private data class TrainerDashboardPayload(
        val classes: List<GymClassResponse>,
        val routines: List<RoutineResponse>,
        val clients: List<UserSummaryResponse>,
        val routineAssignments: List<RoutineAssignmentResponse>,
        val attendances: List<AttendanceResponse>
    )

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
