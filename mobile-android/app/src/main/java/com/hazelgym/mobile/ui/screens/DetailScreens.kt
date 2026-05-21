package com.hazelgym.mobile.ui.screens

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hazelgym.mobile.data.model.AttendanceResponse
import com.hazelgym.mobile.data.model.GymClassResponse
import com.hazelgym.mobile.data.model.MachineResponse
import com.hazelgym.mobile.data.model.buildQrPayload
import com.hazelgym.mobile.data.model.describeQrDestination
import com.hazelgym.mobile.data.model.RoutineAssignmentResponse
import com.hazelgym.mobile.data.model.RoutineResponse
import com.hazelgym.mobile.data.model.UserSummaryResponse
import com.hazelgym.mobile.data.model.QrCodeResponse
import com.hazelgym.mobile.ui.viewmodel.AdminHomeUiState
import com.hazelgym.mobile.ui.viewmodel.ClientHomeUiState
import com.hazelgym.mobile.ui.viewmodel.TrainerHomeUiState
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter

@Composable
fun ClientRoutinesDetailScreen(
    uiState: ClientHomeUiState,
    onBack: () -> Unit
) {
    DetailScreenScaffold(
        eyebrow = "Detalle de rutinas",
        title = "Rutinas disponibles",
        pillLabel = "Cliente",
        pillColor = Color(0xFFFF6B50),
        onBack = onBack
    ) {
        item {
            DetailSectionHeader("Rutinas cargadas", "${uiState.routines.size} planes")
        }
        items(uiState.routines) { routine ->
            RoutineDetailCard(routine)
        }
    }
}

@Composable
fun ClientClassesDetailScreen(
    uiState: ClientHomeUiState,
    onBack: () -> Unit
) {
    DetailScreenScaffold(
        eyebrow = "Detalle de clases",
        title = "Clases activas",
        pillLabel = "Cliente",
        pillColor = Color(0xFFFF6B50),
        onBack = onBack
    ) {
        item {
            DetailSectionHeader("Clases del gimnasio", "${uiState.classes.size} clases")
        }
        items(uiState.classes) { gymClass ->
            GymClassDetailCard(gymClass)
        }
    }
}

@Composable
fun ClientMachinesDetailScreen(
    uiState: ClientHomeUiState,
    onBack: () -> Unit
) {
    DetailScreenScaffold(
        eyebrow = "Detalle de maquinas",
        title = "Maquinas del gimnasio",
        pillLabel = "Cliente",
        pillColor = Color(0xFFFF6B50),
        onBack = onBack
    ) {
        item {
            DetailSectionHeader("Catalogo completo", "${uiState.machines.size} maquinas")
        }
        items(uiState.machines) { machine ->
            MachineDetailCard(machine)
        }
    }
}

@Composable
fun ClientAttendancesDetailScreen(
    uiState: ClientHomeUiState,
    onBack: () -> Unit
) {
    DetailScreenScaffold(
        eyebrow = "Historial personal",
        title = "Mis asistencias",
        pillLabel = "Cliente",
        pillColor = Color(0xFFFF6B50),
        onBack = onBack
    ) {
        item {
            DetailSectionHeader("Registros del cliente", "${uiState.attendances.size} asistencias")
        }
        items(uiState.attendances) { attendance ->
            AttendanceDetailCard(attendance)
        }
    }
}

@Composable
fun TrainerClassesDetailScreen(
    uiState: TrainerHomeUiState,
    onClassNameChange: (String) -> Unit,
    onClassDescriptionChange: (String) -> Unit,
    onClassDurationChange: (String) -> Unit,
    onClassActiveChange: (Boolean) -> Unit,
    onSaveClass: () -> Unit,
    onDeleteClass: () -> Unit,
    onStartNewClass: () -> Unit,
    onEditClass: (GymClassResponse) -> Unit,
    onBack: () -> Unit
) {
    DetailScreenScaffold(
        eyebrow = "Detalle operativo",
        title = "Clases de hoy",
        pillLabel = "Entrenador",
        pillColor = Color(0xFF6B8DFF),
        onBack = onBack
    ) {
        item {
            TrainerClassFormCard(
                uiState = uiState,
                onClassNameChange = onClassNameChange,
                onClassDescriptionChange = onClassDescriptionChange,
                onClassDurationChange = onClassDurationChange,
                onClassActiveChange = onClassActiveChange,
                onSaveClass = onSaveClass,
                onDeleteClass = onDeleteClass,
                onStartNewClass = onStartNewClass
            )
        }
        item {
            DetailSectionHeader("Clases cargadas", "${uiState.classes.size} clases")
        }
        items(uiState.classes) { gymClass ->
            GymClassDetailCard(
                gymClass = gymClass,
                isSelected = uiState.classEditingId == gymClass.id,
                onClick = { onEditClass(gymClass) }
            )
        }
    }
}

@Composable
fun TrainerRoutinesDetailScreen(
    uiState: TrainerHomeUiState,
    onRoutineNameChange: (String) -> Unit,
    onRoutineDescriptionChange: (String) -> Unit,
    onSaveRoutine: () -> Unit,
    onDeleteRoutine: () -> Unit,
    onStartNewRoutine: () -> Unit,
    onEditRoutine: (RoutineResponse) -> Unit,
    onBack: () -> Unit
) {
    DetailScreenScaffold(
        eyebrow = "Detalle operativo",
        title = "Mis rutinas",
        pillLabel = "Entrenador",
        pillColor = Color(0xFF6B8DFF),
        onBack = onBack
    ) {
        item {
            TrainerRoutineFormCard(
                uiState = uiState,
                onRoutineNameChange = onRoutineNameChange,
                onRoutineDescriptionChange = onRoutineDescriptionChange,
                onSaveRoutine = onSaveRoutine,
                onDeleteRoutine = onDeleteRoutine,
                onStartNewRoutine = onStartNewRoutine
            )
        }
        item {
            DetailSectionHeader("Rutinas creadas", "${uiState.routines.size} rutinas")
        }
        items(uiState.routines) { routine ->
            RoutineDetailCard(
                routine = routine,
                isSelected = uiState.routineEditingId == routine.id,
                onClick = { onEditRoutine(routine) }
            )
        }
    }
}

@Composable
fun TrainerAssignmentsDetailScreen(
    uiState: TrainerHomeUiState,
    onAssignmentRoutineIdChange: (String) -> Unit,
    onAssignmentClientIdChange: (String) -> Unit,
    onSelectRoutine: (Long) -> Unit,
    onSelectClient: (Long) -> Unit,
    onSaveAssignment: () -> Unit,
    onDeleteAssignment: () -> Unit,
    onStartNewAssignment: () -> Unit,
    onEditAssignment: (RoutineAssignmentResponse) -> Unit,
    onBack: () -> Unit
) {
    DetailScreenScaffold(
        eyebrow = "Detalle operativo",
        title = "Mis clientes",
        pillLabel = "Entrenador",
        pillColor = Color(0xFF6B8DFF),
        onBack = onBack
    ) {
        item {
            TrainerAssignmentFormCard(
                uiState = uiState,
                onAssignmentRoutineIdChange = onAssignmentRoutineIdChange,
                onAssignmentClientIdChange = onAssignmentClientIdChange,
                onSelectRoutine = onSelectRoutine,
                onSelectClient = onSelectClient,
                onSaveAssignment = onSaveAssignment,
                onDeleteAssignment = onDeleteAssignment,
                onStartNewAssignment = onStartNewAssignment
            )
        }
        item {
            DetailSectionHeader("Rutinas asignadas", "${uiState.routineAssignments.size} asignaciones")
        }
        items(uiState.routineAssignments) { assignment ->
            RoutineAssignmentDetailCard(
                assignment = assignment,
                isSelected = uiState.assignmentEditingId == assignment.id,
                onClick = { onEditAssignment(assignment) }
            )
        }
    }
}

@Composable
fun TrainerAttendancesDetailScreen(
    uiState: TrainerHomeUiState,
    onBack: () -> Unit
) {
    DetailScreenScaffold(
        eyebrow = "Detalle operativo",
        title = "Asistencias recientes",
        pillLabel = "Entrenador",
        pillColor = Color(0xFF6B8DFF),
        onBack = onBack
    ) {
        item {
            DetailSectionHeader("Registros recientes", "${uiState.attendances.size} asistencias")
        }
        items(uiState.attendances) { attendance ->
            AttendanceDetailCard(attendance)
        }
    }
}

@Composable
fun AdminUsersDetailScreen(
    uiState: AdminHomeUiState,
    onUserNameChange: (String) -> Unit,
    onUserEmailChange: (String) -> Unit,
    onUserPasswordChange: (String) -> Unit,
    onUserRoleChange: (String) -> Unit,
    onUserActiveChange: (Boolean) -> Unit,
    onSaveUser: () -> Unit,
    onDeleteUser: () -> Unit,
    onStartNewUser: () -> Unit,
    onEditUser: (UserSummaryResponse) -> Unit,
    onBack: () -> Unit
) {
    DetailScreenScaffold(
        eyebrow = "Gestion de usuarios",
        title = "Usuarios y roles",
        pillLabel = "Administrador",
        pillColor = Color(0xFF1DAA64),
        onBack = onBack
    ) {
        item {
            AdminUserFormCard(
                uiState = uiState,
                onUserNameChange = onUserNameChange,
                onUserEmailChange = onUserEmailChange,
                onUserPasswordChange = onUserPasswordChange,
                onUserRoleChange = onUserRoleChange,
                onUserActiveChange = onUserActiveChange,
                onSaveUser = onSaveUser,
                onDeleteUser = onDeleteUser,
                onStartNewUser = onStartNewUser
            )
        }
        item {
            DetailSectionHeader("Listado completo", "${uiState.users.size} usuarios")
        }
        items(uiState.users) { user ->
            UserDetailCard(
                user = user,
                isSelected = uiState.userEditingId == user.id,
                onClick = { onEditUser(user) }
            )
        }
    }
}

@Composable
fun AdminMachinesDetailScreen(
    uiState: AdminHomeUiState,
    onMachineNameChange: (String) -> Unit,
    onMachineDescriptionChange: (String) -> Unit,
    onMachineMuscleGroupChange: (String) -> Unit,
    onMachineInstructionsChange: (String) -> Unit,
    onMachineLevelChange: (String) -> Unit,
    onMachineSafetyWarningChange: (String) -> Unit,
    onMachineMediaUrlChange: (String) -> Unit,
    onMachineStatusChange: (String) -> Unit,
    onSaveMachine: () -> Unit,
    onDeleteMachine: () -> Unit,
    onStartNewMachine: () -> Unit,
    onEditMachine: (MachineResponse) -> Unit,
    onBack: () -> Unit
) {
    DetailScreenScaffold(
        eyebrow = "Gestion de maquinas",
        title = "Maquinas del gimnasio",
        pillLabel = "Administrador",
        pillColor = Color(0xFF1DAA64),
        onBack = onBack
    ) {
        item {
            AdminMachineFormCard(
                uiState = uiState,
                onMachineNameChange = onMachineNameChange,
                onMachineDescriptionChange = onMachineDescriptionChange,
                onMachineMuscleGroupChange = onMachineMuscleGroupChange,
                onMachineInstructionsChange = onMachineInstructionsChange,
                onMachineLevelChange = onMachineLevelChange,
                onMachineSafetyWarningChange = onMachineSafetyWarningChange,
                onMachineMediaUrlChange = onMachineMediaUrlChange,
                onMachineStatusChange = onMachineStatusChange,
                onSaveMachine = onSaveMachine,
                onDeleteMachine = onDeleteMachine,
                onStartNewMachine = onStartNewMachine
            )
        }
        item {
            DetailSectionHeader("Catalogo completo", "${uiState.machines.size} maquinas")
        }
        items(uiState.machines) { machine ->
            MachineDetailCard(
                machine = machine,
                isSelected = uiState.machineEditingId == machine.id,
                onClick = { onEditMachine(machine) }
            )
        }
    }
}

@Composable
fun AdminQrDetailScreen(
    uiState: AdminHomeUiState,
    onBack: () -> Unit
) {
    DetailScreenScaffold(
        eyebrow = "Generacion y consulta",
        title = "Codigos QR",
        pillLabel = "Administrador",
        pillColor = Color(0xFF1DAA64),
        onBack = onBack
    ) {
        item {
        DetailSectionHeader("Codigos creados", "${uiState.qrCodes.size} codigos")
        }
        items(uiState.qrCodes) { qrCode ->
            QrCodeDetailCard(qrCode)
        }
    }
}

@Composable
fun AdminActivityDetailScreen(
    uiState: AdminHomeUiState,
    onBack: () -> Unit
) {
    DetailScreenScaffold(
        eyebrow = "Actividad del gimnasio",
        title = "Asistencias QR",
        pillLabel = "Administrador",
        pillColor = Color(0xFF1DAA64),
        onBack = onBack
    ) {
        item {
            DetailSectionHeader("Registros recientes", "${uiState.attendances.size} asistencias")
        }
        items(uiState.attendances) { attendance ->
            AttendanceDetailCard(attendance)
        }
    }
}

@Composable
private fun DetailScreenScaffold(
    eyebrow: String,
    title: String,
    pillLabel: String,
    pillColor: Color,
    onBack: () -> Unit,
    content: androidx.compose.foundation.lazy.LazyListScope.() -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF3F4F1)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp, vertical = 18.dp)
                ) {
                    Text(
                        text = eyebrow,
                        color = Color(0xFF4B5563),
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = Color(0xFF0D0D14),
                                shape = RoundedCornerShape(28.dp)
                            )
                            .padding(22.dp)
                    ) {
                        Box(modifier = Modifier.fillMaxWidth()) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(end = 60.dp)
                            ) {
                                Text(
                                    text = title,
                                    color = Color.White,
                                    fontSize = 30.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                RolePill(pillLabel, pillColor)
                            }

                            IconButton(
                                onClick = onBack,
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .size(48.dp)
                                    .background(
                                        color = Color(0x1FFFFFFF),
                                        shape = RoundedCornerShape(16.dp)
                                    )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Volver",
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                }
            }

            content()
        }
    }
}

@Composable
private fun DetailSectionHeader(title: String, subtitle: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Text(text = subtitle, color = Color(0xFF667085))
    }
}

@Composable
private fun RoutineDetailCard(
    routine: RoutineResponse,
    isSelected: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp)
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFFEAFBF1) else Color.White
        ),
        shape = RoundedCornerShape(22.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(text = routine.nombre, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = routine.descripcion ?: "Rutina sin descripcion", color = Color(0xFF667085))
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "Entrenador: ${routine.entrenadorNombre}", color = Color(0xFF0F172A), fontWeight = FontWeight.SemiBold)
            if (onClick != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = if (isSelected) "Editando esta rutina" else "Toca para editar",
                    color = if (isSelected) Color(0xFF1DAA64) else Color(0xFF667085),
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .background(
                            if (isSelected) Color(0xFFDDF8E6) else Color(0xFFF2F4F7),
                            RoundedCornerShape(14.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }
    }
}

@Composable
private fun GymClassDetailCard(
    gymClass: GymClassResponse,
    isSelected: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    val statusText = if (gymClass.activa) "Activa" else "Inactiva"
    val statusColor = if (gymClass.activa) Color(0xFF16A34A) else Color(0xFFD92D20)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp)
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFFEAFBF1) else Color.White
        ),
        shape = RoundedCornerShape(22.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(text = gymClass.nombre, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = gymClass.descripcion ?: "Clase sin descripcion", color = Color(0xFF667085))
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "Entrenador: ${gymClass.entrenadorNombre}", color = Color(0xFF0F172A), fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Estado: $statusText", color = statusColor)
            if (onClick != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = if (isSelected) "Editando esta clase" else "Toca para editar",
                    color = if (isSelected) Color(0xFF1DAA64) else Color(0xFF667085),
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .background(
                            if (isSelected) Color(0xFFDDF8E6) else Color(0xFFF2F4F7),
                            RoundedCornerShape(14.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }
    }
}

@Composable
private fun TrainerClassFormCard(
    uiState: TrainerHomeUiState,
    onClassNameChange: (String) -> Unit,
    onClassDescriptionChange: (String) -> Unit,
    onClassDurationChange: (String) -> Unit,
    onClassActiveChange: (Boolean) -> Unit,
    onSaveClass: () -> Unit,
    onDeleteClass: () -> Unit,
    onStartNewClass: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(22.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = if (uiState.classEditingId == null) "Nueva clase" else "Editar clase #${uiState.classEditingId}",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Completa los datos de la clase y define su estado.",
                color = Color(0xFF667085)
            )

            FormSectionLabel("Datos basicos")

            OutlinedTextField(
                value = uiState.classNameInput,
                onValueChange = onClassNameChange,
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = uiState.classDescriptionInput,
                onValueChange = onClassDescriptionChange,
                label = { Text("Descripcion") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = uiState.classDurationInput,
                onValueChange = onClassDurationChange,
                label = { Text("Duracion en minutos") },
                modifier = Modifier.fillMaxWidth()
            )

            FormSectionLabel("Disponibilidad")

            SearchableChoiceField(
                label = "Estado de la clase",
                placeholder = "Selecciona el estado",
                selectedKey = uiState.classActiveInput.toString(),
                options = listOf(
                    ChoiceOption("true", "Activa", "La clase se muestra como disponible"),
                    ChoiceOption("false", "Inactiva", "La clase queda desactivada temporalmente")
                ),
                onOptionSelected = { option -> onClassActiveChange(option.key.toBoolean()) }
            )

            Text(
                text = "Estado: ${if (uiState.classActiveInput) "Activa" else "Inactiva"}",
                color = Color(0xFF667085)
            )

            FormActionsRow(
                onClear = onStartNewClass,
                onDelete = if (uiState.classEditingId != null) onDeleteClass else null,
                onSave = onSaveClass,
                isSaving = uiState.isSavingClass
            )

            if (uiState.classSaveMessage != null) {
                Text(text = uiState.classSaveMessage, color = Color(0xFF667085))
            }
        }
    }
}

@Composable
private fun TrainerRoutineFormCard(
    uiState: TrainerHomeUiState,
    onRoutineNameChange: (String) -> Unit,
    onRoutineDescriptionChange: (String) -> Unit,
    onSaveRoutine: () -> Unit,
    onDeleteRoutine: () -> Unit,
    onStartNewRoutine: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(22.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = if (uiState.routineEditingId == null) "Nueva rutina" else "Editar rutina #${uiState.routineEditingId}",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Organiza aqui la informacion principal de la rutina.",
                color = Color(0xFF667085)
            )

            FormSectionLabel("Datos basicos")

            OutlinedTextField(
                value = uiState.routineNameInput,
                onValueChange = onRoutineNameChange,
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = uiState.routineDescriptionInput,
                onValueChange = onRoutineDescriptionChange,
                label = { Text("Descripcion") },
                modifier = Modifier.fillMaxWidth()
            )

            FormActionsRow(
                onClear = onStartNewRoutine,
                onDelete = if (uiState.routineEditingId != null) onDeleteRoutine else null,
                onSave = onSaveRoutine,
                isSaving = uiState.isSavingRoutine
            )

            if (uiState.routineSaveMessage != null) {
                Text(text = uiState.routineSaveMessage, color = Color(0xFF667085))
            }
        }
    }
}

@Composable
private fun MachineDetailCard(
    machine: MachineResponse,
    isSelected: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp)
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFFEAFBF1) else Color.White
        ),
        shape = RoundedCornerShape(22.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(18.dp)
        ) {
            Text(text = machine.nombre, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = machine.descripcion ?: "Maquina sin descripcion", color = Color(0xFF667085))
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "Grupo muscular: ${machine.grupoMuscular ?: "-"}", color = Color(0xFF0F172A), fontWeight = FontWeight.SemiBold)
            machine.nivel?.takeIf { it.isNotBlank() }?.let { nivel ->
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Nivel: $nivel", color = Color(0xFF667085))
            }
            machine.instrucciones?.takeIf { it.isNotBlank() }?.let { instrucciones ->
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Uso: $instrucciones", color = Color(0xFF667085))
            }
            machine.advertenciaSeguridad?.takeIf { it.isNotBlank() }?.let { advertencia ->
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Seguridad: $advertencia", color = Color(0xFFD92D20))
            }
            machine.imagenUrl?.takeIf { it.isNotBlank() }?.let { url ->
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Recurso: $url", color = Color(0xFF667085))
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Estado: ${machine.estado}", color = Color(0xFFFF4D2E), fontWeight = FontWeight.SemiBold)
            if (onClick != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = if (isSelected) "Editando esta maquina" else "Toca para editar",
                    color = if (isSelected) Color(0xFF1DAA64) else Color(0xFF667085),
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .background(
                            if (isSelected) Color(0xFFDDF8E6) else Color(0xFFF2F4F7),
                            RoundedCornerShape(14.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }
    }
}

@Composable
private fun AdminMachineFormCard(
    uiState: AdminHomeUiState,
    onMachineNameChange: (String) -> Unit,
    onMachineDescriptionChange: (String) -> Unit,
    onMachineMuscleGroupChange: (String) -> Unit,
    onMachineInstructionsChange: (String) -> Unit,
    onMachineLevelChange: (String) -> Unit,
    onMachineSafetyWarningChange: (String) -> Unit,
    onMachineMediaUrlChange: (String) -> Unit,
    onMachineStatusChange: (String) -> Unit,
    onSaveMachine: () -> Unit,
    onDeleteMachine: () -> Unit,
    onStartNewMachine: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(22.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = if (uiState.machineEditingId == null) "Nueva maquina" else "Editar maquina #${uiState.machineEditingId}",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Mantiene el catalogo del gimnasio actualizado desde el movil.",
                color = Color(0xFF667085)
            )

            FormSectionLabel("Datos basicos")

            OutlinedTextField(
                value = uiState.machineNameInput,
                onValueChange = onMachineNameChange,
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = uiState.machineDescriptionInput,
                onValueChange = onMachineDescriptionChange,
                label = { Text("Descripcion") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = uiState.machineMuscleGroupInput,
                onValueChange = onMachineMuscleGroupChange,
                label = { Text("Grupo muscular") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = uiState.machineInstructionsInput,
                onValueChange = onMachineInstructionsChange,
                label = { Text("Instrucciones de uso") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = uiState.machineLevelInput,
                onValueChange = onMachineLevelChange,
                label = { Text("Nivel recomendado") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = uiState.machineSafetyWarningInput,
                onValueChange = onMachineSafetyWarningChange,
                label = { Text("Advertencia de seguridad") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = uiState.machineMediaUrlInput,
                onValueChange = onMachineMediaUrlChange,
                label = { Text("URL de video o recurso") },
                modifier = Modifier.fillMaxWidth()
            )

            FormSectionLabel("Estado de operacion")

            SearchableChoiceField(
                label = "Estado de la maquina",
                placeholder = "Selecciona el estado",
                selectedKey = uiState.machineStatusInput,
                options = listOf(
                    ChoiceOption("ACTIVA", "Activa", "Disponible para usar en sala"),
                    ChoiceOption("FUERA_DE_SERVICIO", "Fuera de servicio", "No disponible temporalmente")
                ),
                onOptionSelected = { option -> onMachineStatusChange(option.key) }
            )

            Text(
                text = "Estado seleccionado: ${uiState.machineStatusInput}",
                color = Color(0xFF667085)
            )

            FormActionsRow(
                onClear = onStartNewMachine,
                onDelete = if (uiState.machineEditingId != null) onDeleteMachine else null,
                onSave = onSaveMachine,
                isSaving = uiState.isSavingMachine
            )

            if (uiState.machineSaveMessage != null) {
                Text(
                    text = uiState.machineSaveMessage,
                    color = Color(0xFF667085)
                )
            }
        }
    }
}

@Composable
private fun RoutineAssignmentDetailCard(
    assignment: RoutineAssignmentResponse,
    isSelected: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp)
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFFEAFBF1) else Color.White
        ),
        shape = RoundedCornerShape(22.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(text = assignment.routineName, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = "Cliente: ${assignment.clientName}", color = Color(0xFF667085))
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "Asignacion #${assignment.id}", color = Color(0xFF0F172A), fontWeight = FontWeight.SemiBold)
            if (onClick != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = if (isSelected) "Seleccionada para borrar" else "Toca para seleccionar",
                    color = if (isSelected) Color(0xFF1DAA64) else Color(0xFF667085),
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .background(
                            if (isSelected) Color(0xFFDDF8E6) else Color(0xFFF2F4F7),
                            RoundedCornerShape(14.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }
    }
}

@Composable
private fun TrainerAssignmentFormCard(
    uiState: TrainerHomeUiState,
    onAssignmentRoutineIdChange: (String) -> Unit,
    onAssignmentClientIdChange: (String) -> Unit,
    onSelectRoutine: (Long) -> Unit,
    onSelectClient: (Long) -> Unit,
    onSaveAssignment: () -> Unit,
    onDeleteAssignment: () -> Unit,
    onStartNewAssignment: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(22.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = if (uiState.assignmentEditingId == null) "Nueva asignacion" else "Asignacion #${uiState.assignmentEditingId}",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = "Selecciona una rutina tuya y un cliente.",
                color = Color(0xFF667085)
            )

            FormSectionLabel("Rutina")

            SelectionSummaryCard(
                icon = Icons.Default.TaskAlt,
                title = "Rutina seleccionada",
                value = uiState.routines.firstOrNull { it.id.toString() == uiState.assignmentRoutineIdInput }?.nombre
                    ?: "Todavia no has elegido una rutina"
            )

            SearchableSelectionField(
                label = "Buscar rutina",
                placeholder = "Escribe el nombre de la rutina",
                selectedId = uiState.assignmentRoutineIdInput,
                options = uiState.routines.map {
                    SearchOption(
                        id = it.id,
                        title = it.nombre,
                        subtitle = it.descripcion ?: "Rutina sin descripcion"
                    )
                },
                emptyMessage = "No hay rutinas disponibles",
                onOptionSelected = { option -> onSelectRoutine(option.id) }
            )

            FormSectionLabel("Cliente")

            SelectionSummaryCard(
                icon = Icons.Default.Groups,
                title = "Cliente seleccionado",
                value = uiState.clients.firstOrNull { it.id.toString() == uiState.assignmentClientIdInput }?.nombre
                    ?: "Todavia no has elegido un cliente"
            )

            SearchableSelectionField(
                label = "Buscar cliente",
                placeholder = "Escribe el nombre del cliente",
                selectedId = uiState.assignmentClientIdInput,
                options = uiState.clients.map {
                    SearchOption(
                        id = it.id,
                        title = it.nombre,
                        subtitle = it.email
                    )
                },
                emptyMessage = "No hay clientes disponibles",
                onOptionSelected = { option -> onSelectClient(option.id) }
            )

            if (uiState.routines.isEmpty() || uiState.clients.isEmpty()) {
                Text(
                    text = "Si faltan datos para seleccionar, puedes escribir los ID manualmente.",
                    color = Color(0xFF667085)
                )

                OutlinedTextField(
                    value = uiState.assignmentRoutineIdInput,
                    onValueChange = onAssignmentRoutineIdChange,
                    label = { Text("ID de rutina") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = uiState.assignmentClientIdInput,
                    onValueChange = onAssignmentClientIdChange,
                    label = { Text("ID de cliente") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            FormActionsRow(
                onClear = onStartNewAssignment,
                onDelete = if (uiState.assignmentEditingId != null) onDeleteAssignment else null,
                onSave = onSaveAssignment,
                isSaving = uiState.isSavingAssignment
            )

            if (uiState.assignmentSaveMessage != null) {
                Text(text = uiState.assignmentSaveMessage, color = Color(0xFF667085))
            }
        }
    }
}

private data class SearchOption(
    val id: Long,
    val title: String,
    val subtitle: String
)

private data class ChoiceOption(
    val key: String,
    val title: String,
    val subtitle: String
)

@Composable
private fun SelectionSummaryCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    value: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F8FA)),
        shape = RoundedCornerShape(18.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .background(Color(0xFFEAFBF1), RoundedCornerShape(14.dp))
                    .padding(10.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color(0xFF1DAA64)
                )
            }
            Column {
                Text(
                    text = title,
                    color = Color(0xFF667085),
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = value,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF0F172A)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchableSelectionField(
    label: String,
    placeholder: String,
    selectedId: String,
    options: List<SearchOption>,
    emptyMessage: String,
    onOptionSelected: (SearchOption) -> Unit
) {
    var expanded by rememberSaveable(label) { mutableStateOf(false) }
    var query by rememberSaveable(label) { mutableStateOf("") }

    val selectedOption = options.firstOrNull { it.id.toString() == selectedId }
    LaunchedEffect(selectedId) {
        query = selectedOption?.title.orEmpty()
    }
    val filteredOptions = options.filter { option ->
        query.isBlank() ||
            option.title.contains(query, ignoreCase = true) ||
            option.subtitle.contains(query, ignoreCase = true) ||
            option.id.toString().contains(query)
    }.take(8)

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = if (expanded) query else selectedOption?.title.orEmpty(),
            onValueChange = {
                query = it
                expanded = true
            },
            label = { Text(label) },
            placeholder = { Text(placeholder) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            supportingText = {
                Text(
                    text = selectedOption?.let { "ID seleccionado: ${it.id}" } ?: "Sin seleccion actual",
                    color = Color(0xFF667085)
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            if (options.isEmpty()) {
                DropdownMenuItem(
                    text = { Text(emptyMessage) },
                    onClick = { expanded = false }
                )
            } else if (filteredOptions.isEmpty()) {
                DropdownMenuItem(
                    text = { Text("No hay resultados para \"$query\"") },
                    onClick = { expanded = false }
                )
            } else {
                filteredOptions.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Text(
                                    text = option.title,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "${option.subtitle} · ID ${option.id}",
                                    color = Color(0xFF667085),
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        },
                        onClick = {
                            onOptionSelected(option)
                            query = option.title
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchableChoiceField(
    label: String,
    placeholder: String,
    selectedKey: String,
    options: List<ChoiceOption>,
    onOptionSelected: (ChoiceOption) -> Unit
) {
    var expanded by rememberSaveable(label) { mutableStateOf(false) }
    var query by rememberSaveable(label) { mutableStateOf("") }

    val selectedOption = options.firstOrNull { it.key == selectedKey }
    LaunchedEffect(selectedKey) {
        query = selectedOption?.title.orEmpty()
    }
    val filteredOptions = options.filter { option ->
        query.isBlank() ||
            option.title.contains(query, ignoreCase = true) ||
            option.subtitle.contains(query, ignoreCase = true) ||
            option.key.contains(query, ignoreCase = true)
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = if (expanded) query else selectedOption?.title.orEmpty(),
            onValueChange = {
                query = it
                expanded = true
            },
            label = { Text(label) },
            placeholder = { Text(placeholder) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            supportingText = {
                Text(
                    text = selectedOption?.subtitle ?: "Sin seleccion actual",
                    color = Color(0xFF667085)
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            filteredOptions.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(
                                text = option.title,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = option.subtitle,
                                color = Color(0xFF667085),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    },
                    onClick = {
                        onOptionSelected(option)
                        query = option.title
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun FormSectionLabel(text: String) {
    Text(
        text = text,
        color = Color(0xFF0F172A),
        fontWeight = FontWeight.SemiBold,
        style = MaterialTheme.typography.bodyMedium
    )
}

@Composable
private fun FormActionsRow(
    onClear: () -> Unit,
    onDelete: (() -> Unit)?,
    onSave: () -> Unit,
    isSaving: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        TextButton(onClick = onClear) {
            Text("Limpiar")
        }
        if (onDelete != null) {
            TextButton(onClick = onDelete, enabled = !isSaving) {
                Text("Eliminar")
            }
        }
        TextButton(onClick = onSave, enabled = !isSaving) {
            Text(if (isSaving) "Guardando..." else "Guardar")
        }
    }
}

@Composable
private fun AttendanceDetailCard(attendance: AttendanceResponse) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(22.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(text = attendance.usuarioNombre, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = "Tipo QR: ${attendance.qrType}", color = Color(0xFF667085))
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "QR #${attendance.qrCodeId}", color = Color(0xFF667085))
            if (attendance.fechaHora != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Fecha: ${attendance.fechaHora}", color = Color(0xFF667085))
            }
        }
    }
}

@Composable
private fun UserDetailCard(
    user: UserSummaryResponse,
    isSelected: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp)
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFFEAFBF1) else Color.White
        ),
        shape = RoundedCornerShape(22.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(text = user.nombre, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = user.email, color = Color(0xFF667085))
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "Rol: ${user.role}", color = Color(0xFF0F172A), fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Activo: ${if (user.activo) "Si" else "No"}", color = Color(0xFF667085))
            if (onClick != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = if (isSelected) "Editando este usuario" else "Toca para editar",
                    color = if (isSelected) Color(0xFF1DAA64) else Color(0xFF667085),
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .background(
                            if (isSelected) Color(0xFFDDF8E6) else Color(0xFFF2F4F7),
                            RoundedCornerShape(14.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }
    }
}

@Composable
private fun AdminUserFormCard(
    uiState: AdminHomeUiState,
    onUserNameChange: (String) -> Unit,
    onUserEmailChange: (String) -> Unit,
    onUserPasswordChange: (String) -> Unit,
    onUserRoleChange: (String) -> Unit,
    onUserActiveChange: (Boolean) -> Unit,
    onSaveUser: () -> Unit,
    onDeleteUser: () -> Unit,
    onStartNewUser: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(22.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = if (uiState.userEditingId == null) "Nuevo usuario" else "Editar usuario #${uiState.userEditingId}",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Gestiona altas, cambios de rol y acceso desde una sola vista.",
                color = Color(0xFF667085)
            )

            FormSectionLabel("Datos basicos")

            OutlinedTextField(
                value = uiState.userNameInput,
                onValueChange = onUserNameChange,
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = uiState.userEmailInput,
                onValueChange = onUserEmailChange,
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = uiState.userPasswordInput,
                onValueChange = onUserPasswordChange,
                label = { Text(if (uiState.userEditingId == null) "Contrasena" else "Nueva contrasena opcional") },
                modifier = Modifier.fillMaxWidth()
            )

            FormSectionLabel("Permisos y acceso")

            SearchableChoiceField(
                label = "Rol del usuario",
                placeholder = "Selecciona el rol",
                selectedKey = uiState.userRoleInput,
                options = listOf(
                    ChoiceOption("CLIENT", "Cliente", "Usuario final del gimnasio"),
                    ChoiceOption("TRAINER", "Entrenador", "Gestiona clases y rutinas"),
                    ChoiceOption("ADMIN", "Administrador", "Control total del sistema")
                ),
                onOptionSelected = { option -> onUserRoleChange(option.key) }
            )

            SearchableChoiceField(
                label = "Estado del usuario",
                placeholder = "Selecciona el estado",
                selectedKey = uiState.userActiveInput.toString(),
                options = listOf(
                    ChoiceOption("true", "Activo", "Puede iniciar sesion y usar la app"),
                    ChoiceOption("false", "Inactivo", "No puede acceder temporalmente")
                ),
                onOptionSelected = { option -> onUserActiveChange(option.key.toBoolean()) }
            )

            Text(
                text = "Rol: ${uiState.userRoleInput} | Estado: ${if (uiState.userActiveInput) "Activo" else "Inactivo"}",
                color = Color(0xFF667085)
            )

            FormActionsRow(
                onClear = onStartNewUser,
                onDelete = if (uiState.userEditingId != null) onDeleteUser else null,
                onSave = onSaveUser,
                isSaving = uiState.isSavingUser
            )

            if (uiState.userSaveMessage != null) {
                Text(text = uiState.userSaveMessage, color = Color(0xFF667085))
            }
        }
    }
}

@Composable
private fun QrCodeDetailCard(qrCode: QrCodeResponse) {
    val qrPayload = remember(qrCode) { buildQrPayload(qrCode) }
    val qrBitmap = remember(qrPayload) { buildQrBitmap(qrPayload) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(22.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(text = "QR #${qrCode.id}", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = "Tipo: ${qrCode.tipo}", color = Color(0xFF667085))
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = describeQrDestination(qrCode),
                color = Color(0xFF0F172A),
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(14.dp))
            qrBitmap?.let { bitmap ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF7F8FA), RoundedCornerShape(18.dp))
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "Codigo QR ${qrCode.id}",
                        modifier = Modifier.size(180.dp)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
            Text(
                text = "Contenido del QR",
                color = Color(0xFF667085),
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = qrPayload,
                color = Color(0xFF0F172A),
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Prueba rapida en emulador: tambien puedes usar el ID ${qrCode.id} manualmente en el lector.",
                color = Color(0xFF667085)
            )
        }
    }
}

private fun buildQrBitmap(value: String, size: Int = 768): Bitmap? {
    return runCatching {
        val matrix = QRCodeWriter().encode(value, BarcodeFormat.QR_CODE, size, size)
        Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888).apply {
            for (x in 0 until size) {
                for (y in 0 until size) {
                    setPixel(
                        x,
                        y,
                        if (matrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE
                    )
                }
            }
        }
    }.getOrNull()
}

@Composable
private fun RolePill(label: String, color: Color) {
    Box(
        modifier = Modifier
            .background(color, RoundedCornerShape(20.dp))
            .padding(horizontal = 14.dp, vertical = 6.dp)
    ) {
        Text(
            text = label,
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp
        )
    }
}

