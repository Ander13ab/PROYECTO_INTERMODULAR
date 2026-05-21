package com.hazelgym.mobile.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.hazelgym.mobile.data.model.AttendanceResponse
import com.hazelgym.mobile.data.model.GymClassResponse
import com.hazelgym.mobile.data.model.MachineResponse
import com.hazelgym.mobile.data.model.QrCodeResponse
import com.hazelgym.mobile.data.model.RoutineResponse
import com.hazelgym.mobile.data.model.describeQrDestination
import com.hazelgym.mobile.data.model.extractQrCodeId
import com.hazelgym.mobile.data.remote.ApiClient
import com.hazelgym.mobile.data.repository.AttendanceRepository
import com.hazelgym.mobile.data.repository.GymClassRepository
import com.hazelgym.mobile.data.repository.MachineRepository
import com.hazelgym.mobile.data.repository.QrCodeRepository
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
    val email: String = "",
    val role: String = "",
    val machines: List<MachineResponse> = emptyList(),
    val routines: List<RoutineResponse> = emptyList(),
    val classes: List<GymClassResponse> = emptyList(),
    val attendances: List<AttendanceResponse> = emptyList(),
    val qrCodes: List<QrCodeResponse> = emptyList(),
    val qrCodeInput: String = "",
    val scannedMachine: MachineResponse? = null,
    val machineScanMessage: String? = null,
    val attendanceMessage: String? = null,
    val isSubmittingAttendance: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class ClientHomeViewModel(application: Application) : AndroidViewModel(application) {
    private val sessionStorage = SessionStorage(application.applicationContext)
    private val attendanceRepository = AttendanceRepository(ApiClient.attendanceApi)
    private val machineRepository = MachineRepository(ApiClient.machineApi)
    private val routineRepository = RoutineRepository(ApiClient.routineApi)
    private val gymClassRepository = GymClassRepository(ApiClient.gymClassApi)
    private val qrCodeRepository = QrCodeRepository(ApiClient.qrCodeApi)

    private val _uiState = MutableStateFlow(ClientHomeUiState(isLoading = true))
    val uiState: StateFlow<ClientHomeUiState> = _uiState.asStateFlow()

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
                val machinesDeferred = async { machineRepository.getMachines(session) }
                val routinesDeferred = async { routineRepository.getRoutines(session) }
                val classesDeferred = async { gymClassRepository.getClasses(session) }
                val qrCodesDeferred = async {
                    runCatching { qrCodeRepository.getQrCodes(session) }
                        .getOrDefault(uiState.value.qrCodes)
                }
                val attendancesDeferred = async {
                    runCatching { attendanceRepository.getAttendances(session) }
                        .getOrDefault(uiState.value.attendances)
                }
                ClientDashboardPayload(
                    machinesDeferred.await(),
                    routinesDeferred.await(),
                    classesDeferred.await(),
                    qrCodesDeferred.await(),
                    attendancesDeferred.await()
                )
            }.onSuccess { (machines, routines, classes, qrCodes, attendances) ->
                _uiState.update {
                    it.copy(
                        machines = machines,
                        routines = routines,
                        classes = classes,
                        qrCodes = qrCodes,
                        scannedMachine = it.scannedMachine?.let { selected ->
                            machines.firstOrNull { machine -> machine.id == selected.id }
                        },
                        attendances = attendances,
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

    fun updateQrCodeInput(value: String) {
        _uiState.update { it.copy(qrCodeInput = value, attendanceMessage = null) }
    }

    fun registerScannedMachine(rawValue: String) {
        val qrCodeId = extractQrCodeId(rawValue)
        if (qrCodeId == null) {
            _uiState.update {
                it.copy(
                    scannedMachine = null,
                    machineScanMessage = "No se pudo detectar un identificador QR valido para maquina."
                )
            }
            return
        }

        val qrCode = uiState.value.qrCodes.firstOrNull { it.id == qrCodeId }
        if (qrCode == null) {
            _uiState.update {
                it.copy(
                    scannedMachine = null,
                    machineScanMessage = "No existe un QR cargado con ID $qrCodeId en la app."
                )
            }
            return
        }

        if (!qrCode.tipo.equals("MACHINE", ignoreCase = true) || qrCode.maquinaId == null) {
            _uiState.update {
                it.copy(
                    scannedMachine = null,
                    machineScanMessage = "Ese QR no pertenece a una maquina. Usa uno de tipo MACHINE."
                )
            }
            return
        }

        val machine = uiState.value.machines.firstOrNull { it.id == qrCode.maquinaId }
        if (machine == null) {
            _uiState.update {
                it.copy(
                    scannedMachine = null,
                    machineScanMessage = "Se detecto la maquina del QR, pero su ficha no esta cargada todavia."
                )
            }
            return
        }

        _uiState.update {
            it.copy(
                scannedMachine = machine,
                machineScanMessage = "Maquina detectada: ${machine.nombre}. Ya puedes ver su funcionamiento."
            )
        }
    }

    fun clearScannedMachine() {
        _uiState.update { it.copy(scannedMachine = null, machineScanMessage = null) }
    }

    fun registerScannedAttendance(rawValue: String) {
        val qrCodeId = extractQrCodeId(rawValue)
        if (qrCodeId == null) {
            _uiState.update {
                it.copy(attendanceMessage = "No se pudo detectar un identificador QR valido.")
            }
            return
        }

        _uiState.update {
            it.copy(
                qrCodeInput = qrCodeId.toString(),
                attendanceMessage = "QR detectado. Registrando asistencia..."
            )
        }
        registerAttendance(qrCodeId, clearInput = true, scanned = true)
    }

    fun registerAttendance() {
        val qrCodeId = extractQrCodeId(uiState.value.qrCodeInput)
        if (qrCodeId == null) {
            _uiState.update { it.copy(attendanceMessage = "Introduce un identificador QR valido.") }
            return
        }
        registerAttendance(qrCodeId, clearInput = true, scanned = false)
    }

    private fun registerAttendance(qrCodeId: Long, clearInput: Boolean, scanned: Boolean) {
        if (uiState.value.isSubmittingAttendance) {
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSubmittingAttendance = true, attendanceMessage = null) }
            val session = sessionStorage.session.filterNotNull().first()
            val qrCode = uiState.value.qrCodes.firstOrNull { it.id == qrCodeId }
            runCatching {
                attendanceRepository.createAttendance(session, qrCodeId)
            }.onSuccess { attendance ->
                _uiState.update {
                    it.copy(
                        attendances = listOf(attendance) + it.attendances.filterNot { saved -> saved.id == attendance.id },
                        isSubmittingAttendance = false,
                        qrCodeInput = if (clearInput) "" else it.qrCodeInput,
                        attendanceMessage = buildAttendanceSuccessMessage(attendance, qrCode, scanned)
                    )
                }
                refresh()
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isSubmittingAttendance = false,
                        attendanceMessage = error.message ?: "No se pudo registrar la asistencia."
                    )
                }
            }
        }
    }

    private fun buildAttendanceSuccessMessage(
        attendance: AttendanceResponse,
        qrCode: QrCodeResponse?,
        scanned: Boolean
    ): String {
        val prefix = if (scanned) "QR escaneado correctamente." else "Registro completado."
        if (qrCode == null) {
            return "$prefix Asistencia guardada con QR #${attendance.qrCodeId}."
        }

        return when {
            qrCode.esEntradaGimnasio ->
                "$prefix Entrada al gimnasio registrada con QR #${attendance.qrCodeId}."
            qrCode.tipo.equals("MACHINE", ignoreCase = true) ->
                "$prefix Uso registrado en ${describeQrDestination(qrCode)}."
            qrCode.tipo.equals("CLASS_SESSION", ignoreCase = true) ->
                "$prefix Asistencia registrada en ${describeQrDestination(qrCode)}."
            else -> "$prefix Asistencia guardada con QR #${attendance.qrCodeId}."
        }
    }

    private data class ClientDashboardPayload(
        val machines: List<MachineResponse>,
        val routines: List<RoutineResponse>,
        val classes: List<GymClassResponse>,
        val qrCodes: List<QrCodeResponse>,
        val attendances: List<AttendanceResponse>
    )

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
