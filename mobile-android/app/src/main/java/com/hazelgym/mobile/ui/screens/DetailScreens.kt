package com.hazelgym.mobile.ui.screens

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
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hazelgym.mobile.data.model.AttendanceResponse
import com.hazelgym.mobile.data.model.GymClassResponse
import com.hazelgym.mobile.data.model.MachineResponse
import com.hazelgym.mobile.data.model.RoutineAssignmentResponse
import com.hazelgym.mobile.data.model.RoutineResponse
import com.hazelgym.mobile.data.model.UserSummaryResponse
import com.hazelgym.mobile.data.model.QrCodeResponse
import com.hazelgym.mobile.ui.viewmodel.AdminHomeUiState
import com.hazelgym.mobile.ui.viewmodel.ClientHomeUiState
import com.hazelgym.mobile.ui.viewmodel.TrainerHomeUiState

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
            DetailSectionHeader("Clases cargadas", "${uiState.classes.size} clases")
        }
        items(uiState.classes) { gymClass ->
            GymClassDetailCard(gymClass)
        }
    }
}

@Composable
fun TrainerAssignmentsDetailScreen(
    uiState: TrainerHomeUiState,
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
            DetailSectionHeader("Rutinas asignadas", "${uiState.routineAssignments.size} asignaciones")
        }
        items(uiState.routineAssignments) { assignment ->
            RoutineAssignmentDetailCard(assignment)
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
private fun RoutineDetailCard(routine: RoutineResponse) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(22.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(text = routine.nombre, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = routine.descripcion ?: "Rutina sin descripcion", color = Color(0xFF667085))
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "Entrenador: ${routine.entrenadorNombre}", color = Color(0xFF0F172A), fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun GymClassDetailCard(gymClass: GymClassResponse) {
    val statusText = if (gymClass.activa) "Activa" else "Inactiva"
    val statusColor = if (gymClass.activa) Color(0xFF16A34A) else Color(0xFFD92D20)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
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

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                SelectableTextButton(
                    label = "ACTIVA",
                    selected = uiState.machineStatusInput == "ACTIVA",
                    onClick = { onMachineStatusChange("ACTIVA") }
                )
                SelectableTextButton(
                    label = "FUERA SERVICIO",
                    selected = uiState.machineStatusInput == "FUERA_DE_SERVICIO",
                    onClick = { onMachineStatusChange("FUERA_DE_SERVICIO") }
                )
            }

            Text(
                text = "Estado seleccionado: ${uiState.machineStatusInput}",
                color = Color(0xFF667085)
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                TextButton(onClick = onStartNewMachine) {
                    Text("Limpiar")
                }
                if (uiState.machineEditingId != null) {
                    TextButton(
                        onClick = onDeleteMachine,
                        enabled = !uiState.isSavingMachine
                    ) {
                        Text("Eliminar")
                    }
                }
                TextButton(onClick = onSaveMachine, enabled = !uiState.isSavingMachine) {
                    Text(if (uiState.isSavingMachine) "Guardando..." else "Guardar")
                }
            }

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
private fun RoutineAssignmentDetailCard(assignment: RoutineAssignmentResponse) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(22.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(text = assignment.routineName, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = "Cliente: ${assignment.clientName}", color = Color(0xFF667085))
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "Asignacion #${assignment.id}", color = Color(0xFF0F172A), fontWeight = FontWeight.SemiBold)
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

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                SelectableTextButton(
                    label = "CLIENT",
                    selected = uiState.userRoleInput == "CLIENT",
                    onClick = { onUserRoleChange("CLIENT") }
                )
                SelectableTextButton(
                    label = "TRAINER",
                    selected = uiState.userRoleInput == "TRAINER",
                    onClick = { onUserRoleChange("TRAINER") }
                )
                SelectableTextButton(
                    label = "ADMIN",
                    selected = uiState.userRoleInput == "ADMIN",
                    onClick = { onUserRoleChange("ADMIN") }
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                SelectableTextButton(
                    label = "Activo",
                    selected = uiState.userActiveInput,
                    onClick = { onUserActiveChange(true) }
                )
                SelectableTextButton(
                    label = "Inactivo",
                    selected = !uiState.userActiveInput,
                    onClick = { onUserActiveChange(false) }
                )
            }

            Text(
                text = "Rol: ${uiState.userRoleInput} | Estado: ${if (uiState.userActiveInput) "Activo" else "Inactivo"}",
                color = Color(0xFF667085)
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                TextButton(onClick = onStartNewUser) { Text("Limpiar") }
                if (uiState.userEditingId != null) {
                    TextButton(onClick = onDeleteUser, enabled = !uiState.isSavingUser) { Text("Eliminar") }
                }
                TextButton(onClick = onSaveUser, enabled = !uiState.isSavingUser) {
                    Text(if (uiState.isSavingUser) "Guardando..." else "Guardar")
                }
            }

            if (uiState.userSaveMessage != null) {
                Text(text = uiState.userSaveMessage, color = Color(0xFF667085))
            }
        }
    }
}

@Composable
private fun SelectableTextButton(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
        modifier = Modifier.background(
            color = if (selected) Color(0xFFEAFBF1) else Color(0xFFF4F4F5),
            shape = RoundedCornerShape(14.dp)
        )
    ) {
        Text(
            text = label,
            color = if (selected) Color(0xFF1DAA64) else Color(0xFF475467),
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun QrCodeDetailCard(qrCode: QrCodeResponse) {
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
            Text(text = if (qrCode.esEntradaGimnasio) "Entrada del gimnasio" else "QR asociado", color = Color(0xFF0F172A), fontWeight = FontWeight.SemiBold)
        }
    }
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

