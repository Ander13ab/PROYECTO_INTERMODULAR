package com.hazelgym.mobile.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.hazelgym.mobile.data.model.ClassSessionResponse
import com.hazelgym.mobile.data.model.GymClassResponse
import com.hazelgym.mobile.data.model.MachineResponse
import com.hazelgym.mobile.data.model.QrCodeResponse
import com.hazelgym.mobile.data.model.SessionUser
import com.hazelgym.mobile.data.model.AttendanceResponse
import com.hazelgym.mobile.data.model.UserSummaryResponse
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
    val qrClassSessionIdInput: String = "",
    val qrMachineIdInput: String = "",
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

    fun createEntryQrCode() {
        createQrCode {
            qrCodeRepository.createEntryQrCode(it)
        }
    }

    fun createMachineQrCode() {
        val machineId = uiState.value.qrMachineIdInput.toLongOrNull()
        if (machineId == null) {
            _uiState.update { it.copy(qrCreateMessage = "Introduce un ID de máquina válido.") }
            return
        }

        createQrCode {
            qrCodeRepository.createMachineQrCode(it, machineId)
        }
    }

    fun createClassSessionQrCode() {
        val classSessionId = uiState.value.qrClassSessionIdInput.toLongOrNull()
        if (classSessionId == null) {
            _uiState.update { it.copy(qrCreateMessage = "Introduce un ID de sesión válido.") }
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
                        qrCreateMessage = error.message ?: "No se pudo crear el código QR."
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
