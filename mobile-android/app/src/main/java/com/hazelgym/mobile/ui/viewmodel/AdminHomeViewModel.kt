package com.hazelgym.mobile.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.hazelgym.mobile.data.model.ClassSessionResponse
import com.hazelgym.mobile.data.model.GymClassResponse
import com.hazelgym.mobile.data.model.MachineResponse
import com.hazelgym.mobile.data.model.MachineUpsertRequest
import com.hazelgym.mobile.data.model.QrCodeResponse
import com.hazelgym.mobile.data.model.SessionUser
import com.hazelgym.mobile.data.model.AttendanceResponse
import com.hazelgym.mobile.data.model.UserSummaryResponse
import com.hazelgym.mobile.data.model.UserUpsertRequest
import com.hazelgym.mobile.data.remote.ApiClient
import com.hazelgym.mobile.data.repository.AttendanceRepository
import com.hazelgym.mobile.data.repository.ClassSessionRepository
import com.hazelgym.mobile.data.repository.GymClassRepository
import com.hazelgym.mobile.data.repository.MachineRepository
import com.hazelgym.mobile.data.repository.QrCodeRepository
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
    val classSessions: List<ClassSessionResponse> = emptyList(),
    val classes: List<GymClassResponse> = emptyList(),
    val machines: List<MachineResponse> = emptyList(),
    val qrCodes: List<QrCodeResponse> = emptyList(),
    val attendances: List<AttendanceResponse> = emptyList(),
    val users: List<UserSummaryResponse> = emptyList(),
    val userEditingId: Long? = null,
    val userNameInput: String = "",
    val userEmailInput: String = "",
    val userPasswordInput: String = "",
    val userRoleInput: String = "CLIENT",
    val userActiveInput: Boolean = true,
    val userSaveMessage: String? = null,
    val isSavingUser: Boolean = false,
    val qrClassSessionIdInput: String = "",
    val qrMachineIdInput: String = "",
    val machineEditingId: Long? = null,
    val machineNameInput: String = "",
    val machineDescriptionInput: String = "",
    val machineMuscleGroupInput: String = "",
    val machineStatusInput: String = "ACTIVA",
    val machineSaveMessage: String? = null,
    val isSavingMachine: Boolean = false,
    val qrCreateMessage: String? = null,
    val isCreatingQr: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class AdminHomeViewModel(application: Application) : AndroidViewModel(application) {
    private val sessionStorage = SessionStorage(application.applicationContext)
    private val classSessionRepository = ClassSessionRepository(ApiClient.classSessionApi)
    private val gymClassRepository = GymClassRepository(ApiClient.gymClassApi)
    private val machineRepository = MachineRepository(ApiClient.machineApi)
    private val qrCodeRepository = QrCodeRepository(ApiClient.qrCodeApi)
    private val attendanceRepository = AttendanceRepository(ApiClient.attendanceApi)
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
                val classSessionsDeferred = async { classSessionRepository.getClassSessions(session) }
                val classesDeferred = async { gymClassRepository.getClasses(session) }
                val machinesDeferred = async { machineRepository.getMachines(session) }
                val qrCodesDeferred = async { qrCodeRepository.getQrCodes(session) }
                val attendancesDeferred = async { attendanceRepository.getAttendances(session) }
                val usersDeferred = async { userRepository.getUsers(session) }
                val classSessions = classSessionsDeferred.await()
                val classes = classesDeferred.await()
                val machines = machinesDeferred.await()
                val qrCodes = qrCodesDeferred.await()
                val attendances = attendancesDeferred.await()
                val users = usersDeferred.await()
                AdminDashboardPayload(classSessions, classes, machines, qrCodes, attendances, users)
            }.onSuccess { (classSessions, classes, machines, qrCodes, attendances, users) ->
                _uiState.update {
                    it.copy(
                        classSessions = classSessions,
                        classes = classes,
                        machines = machines,
                        qrCodes = qrCodes,
                        attendances = attendances,
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

    fun updateQrClassSessionIdInput(value: String) {
        _uiState.update { it.copy(qrClassSessionIdInput = value, qrCreateMessage = null) }
    }

    fun updateQrMachineIdInput(value: String) {
        _uiState.update { it.copy(qrMachineIdInput = value, qrCreateMessage = null) }
    }

    fun startNewUser() {
        _uiState.update {
            it.copy(
                userEditingId = null,
                userNameInput = "",
                userEmailInput = "",
                userPasswordInput = "",
                userRoleInput = "CLIENT",
                userActiveInput = true,
                userSaveMessage = null
            )
        }
    }

    fun editUser(user: UserSummaryResponse) {
        _uiState.update {
            it.copy(
                userEditingId = user.id,
                userNameInput = user.nombre,
                userEmailInput = user.email,
                userPasswordInput = "",
                userRoleInput = user.role,
                userActiveInput = user.activo,
                userSaveMessage = "Editando usuario #${user.id}"
            )
        }
    }

    fun updateUserNameInput(value: String) {
        _uiState.update { it.copy(userNameInput = value, userSaveMessage = null) }
    }

    fun updateUserEmailInput(value: String) {
        _uiState.update { it.copy(userEmailInput = value, userSaveMessage = null) }
    }

    fun updateUserPasswordInput(value: String) {
        _uiState.update { it.copy(userPasswordInput = value, userSaveMessage = null) }
    }

    fun updateUserRoleInput(value: String) {
        _uiState.update { it.copy(userRoleInput = value, userSaveMessage = null) }
    }

    fun updateUserActiveInput(value: Boolean) {
        _uiState.update { it.copy(userActiveInput = value, userSaveMessage = null) }
    }

    fun saveUser() {
        val name = uiState.value.userNameInput.trim()
        val email = uiState.value.userEmailInput.trim()
        val password = uiState.value.userPasswordInput.trim()
        val editingId = uiState.value.userEditingId

        if (name.isBlank()) {
            _uiState.update { it.copy(userSaveMessage = "Introduce un nombre para el usuario.") }
            return
        }
        if (email.isBlank()) {
            _uiState.update { it.copy(userSaveMessage = "Introduce un email para el usuario.") }
            return
        }
        if (editingId == null && password.length < 6) {
            _uiState.update { it.copy(userSaveMessage = "La contrasena debe tener al menos 6 caracteres.") }
            return
        }

        val request = UserUpsertRequest(
            nombre = name,
            email = email,
            password = password.ifBlank { null },
            roleName = normalizeRole(uiState.value.userRoleInput),
            activo = uiState.value.userActiveInput
        )

        viewModelScope.launch {
            _uiState.update { it.copy(isSavingUser = true, userSaveMessage = null) }
            val session = sessionStorage.session.filterNotNull().first()
            runCatching {
                if (editingId == null) {
                    userRepository.createUser(session, request)
                } else {
                    userRepository.updateUser(session, editingId, request)
                }
            }.onSuccess { user ->
                _uiState.update {
                    it.copy(
                        isSavingUser = false,
                        userEditingId = null,
                        userNameInput = "",
                        userEmailInput = "",
                        userPasswordInput = "",
                        userRoleInput = "CLIENT",
                        userActiveInput = true,
                        userSaveMessage = "Usuario #${user.id} guardado correctamente."
                    )
                }
                refresh()
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isSavingUser = false,
                        userSaveMessage = error.message ?: "No se pudo guardar el usuario."
                    )
                }
            }
        }
    }

    fun deleteEditingUser() {
        val userId = uiState.value.userEditingId
        if (userId == null) {
            _uiState.update { it.copy(userSaveMessage = "Selecciona un usuario para eliminarlo.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSavingUser = true, userSaveMessage = null) }
            val session = sessionStorage.session.filterNotNull().first()
            runCatching {
                userRepository.deleteUser(session, userId)
            }.onSuccess {
                _uiState.update {
                    it.copy(
                        isSavingUser = false,
                        userEditingId = null,
                        userNameInput = "",
                        userEmailInput = "",
                        userPasswordInput = "",
                        userRoleInput = "CLIENT",
                        userActiveInput = true,
                        userSaveMessage = "Usuario eliminado correctamente."
                    )
                }
                refresh()
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isSavingUser = false,
                        userSaveMessage = error.message ?: "No se pudo eliminar el usuario."
                    )
                }
            }
        }
    }

    fun startNewMachine() {
        _uiState.update {
            it.copy(
                machineEditingId = null,
                machineNameInput = "",
                machineDescriptionInput = "",
                machineMuscleGroupInput = "",
                machineStatusInput = "ACTIVA",
                machineSaveMessage = null
            )
        }
    }

    fun editMachine(machine: MachineResponse) {
        _uiState.update {
            it.copy(
                machineEditingId = machine.id,
                machineNameInput = machine.nombre,
                machineDescriptionInput = machine.descripcion.orEmpty(),
                machineMuscleGroupInput = machine.grupoMuscular.orEmpty(),
                machineStatusInput = machine.estado,
                machineSaveMessage = "Editando maquina #${machine.id}"
            )
        }
    }

    fun updateMachineNameInput(value: String) {
        _uiState.update { it.copy(machineNameInput = value, machineSaveMessage = null) }
    }

    fun updateMachineDescriptionInput(value: String) {
        _uiState.update { it.copy(machineDescriptionInput = value, machineSaveMessage = null) }
    }

    fun updateMachineMuscleGroupInput(value: String) {
        _uiState.update { it.copy(machineMuscleGroupInput = value, machineSaveMessage = null) }
    }

    fun updateMachineStatusInput(value: String) {
        _uiState.update { it.copy(machineStatusInput = value, machineSaveMessage = null) }
    }

    fun saveMachine() {
        val name = uiState.value.machineNameInput.trim()
        if (name.isBlank()) {
            _uiState.update { it.copy(machineSaveMessage = "Introduce un nombre para la maquina.") }
            return
        }

        val status = if (uiState.value.machineStatusInput.equals("FUERA_DE_SERVICIO", ignoreCase = true)) {
            "FUERA_DE_SERVICIO"
        } else {
            "ACTIVA"
        }

        val request = MachineUpsertRequest(
            nombre = name,
            descripcion = uiState.value.machineDescriptionInput.trim().ifBlank { null },
            grupoMuscular = uiState.value.machineMuscleGroupInput.trim().ifBlank { null },
            estado = status
        )

        viewModelScope.launch {
            _uiState.update { it.copy(isSavingMachine = true, machineSaveMessage = null) }
            val session = sessionStorage.session.filterNotNull().first()
            val editingId = uiState.value.machineEditingId

            runCatching {
                if (editingId == null) {
                    machineRepository.createMachine(session, request)
                } else {
                    machineRepository.updateMachine(session, editingId, request)
                }
            }.onSuccess { machine ->
                _uiState.update {
                    it.copy(
                        isSavingMachine = false,
                        machineEditingId = null,
                        machineNameInput = "",
                        machineDescriptionInput = "",
                        machineMuscleGroupInput = "",
                        machineStatusInput = "ACTIVA",
                        machineSaveMessage = "Maquina #${machine.id} guardada correctamente."
                    )
                }
                refresh()
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isSavingMachine = false,
                        machineSaveMessage = error.message ?: "No se pudo guardar la maquina."
                    )
                }
            }
        }
    }

    fun deleteEditingMachine() {
        val machineId = uiState.value.machineEditingId
        if (machineId == null) {
            _uiState.update { it.copy(machineSaveMessage = "Selecciona una maquina para eliminarla.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSavingMachine = true, machineSaveMessage = null) }
            val session = sessionStorage.session.filterNotNull().first()
            runCatching {
                machineRepository.deleteMachine(session, machineId)
            }.onSuccess {
                _uiState.update {
                    it.copy(
                        isSavingMachine = false,
                        machineEditingId = null,
                        machineNameInput = "",
                        machineDescriptionInput = "",
                        machineMuscleGroupInput = "",
                        machineStatusInput = "ACTIVA",
                        machineSaveMessage = "Maquina eliminada correctamente."
                    )
                }
                refresh()
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isSavingMachine = false,
                        machineSaveMessage = error.message ?: "No se pudo eliminar la maquina."
                    )
                }
            }
        }
    }

    fun createEntryQrCode() {
        createQrCode {
            qrCodeRepository.createEntryQrCode(it)
        }
    }

    fun createMachineQrCode() {
        val machineId = uiState.value.qrMachineIdInput.toLongOrNull()
        if (machineId == null) {
            _uiState.update { it.copy(qrCreateMessage = "Introduce un ID de maquina valido.") }
            return
        }

        createQrCode {
            qrCodeRepository.createMachineQrCode(it, machineId)
        }
    }

    fun createClassSessionQrCode() {
        val classSessionId = uiState.value.qrClassSessionIdInput.toLongOrNull()
        if (classSessionId == null) {
            _uiState.update { it.copy(qrCreateMessage = "Introduce un ID de sesion valido.") }
            return
        }

        createQrCode {
            qrCodeRepository.createClassSessionQrCode(it, classSessionId)
        }
    }

    private fun createQrCode(
        action: suspend (SessionUser) -> QrCodeResponse
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isCreatingQr = true, qrCreateMessage = null) }
            val session = sessionStorage.session.filterNotNull().first()
            runCatching {
                action(session)
            }.onSuccess { qrCode ->
                _uiState.update {
                    it.copy(
                        isCreatingQr = false,
                        qrClassSessionIdInput = "",
                        qrMachineIdInput = "",
                        qrCreateMessage = "QR #${qrCode.id} creado correctamente."
                    )
                }
                refresh()
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isCreatingQr = false,
                        qrCreateMessage = error.message ?: "No se pudo crear el codigo QR."
                    )
                }
            }
        }
    }

    private data class AdminDashboardPayload(
        val classSessions: List<ClassSessionResponse>,
        val classes: List<GymClassResponse>,
        val machines: List<MachineResponse>,
        val qrCodes: List<QrCodeResponse>,
        val attendances: List<AttendanceResponse>,
        val users: List<UserSummaryResponse>
    )

    private fun normalizeRole(role: String): String {
        return when (role.trim().uppercase()) {
            "ADMIN" -> "ADMIN"
            "TRAINER", "ENTRENADOR" -> "TRAINER"
            else -> "CLIENT"
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

