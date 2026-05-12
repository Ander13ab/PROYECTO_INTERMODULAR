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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.SportsGymnastics
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hazelgym.mobile.data.model.AttendanceResponse
import com.hazelgym.mobile.data.model.QrCodeResponse
import com.hazelgym.mobile.data.model.UserSummaryResponse
import com.hazelgym.mobile.ui.viewmodel.AdminHomeUiState

private enum class AdminTab(val label: String, val icon: ImageVector) {
    HOME("Inicio", Icons.Default.Home),
    USERS("Usuarios", Icons.Default.Groups),
    QR("QR", Icons.Default.QrCode2),
    ACTIVITY("Actividad", Icons.Default.Badge),
    PROFILE("Perfil", Icons.Default.Person)
}

@Composable
fun AdminHomeScreen(
    uiState: AdminHomeUiState,
    onRefresh: () -> Unit,
    onQrClassSessionIdChange: (String) -> Unit,
    onQrMachineIdChange: (String) -> Unit,
    onCreateEntryQr: () -> Unit,
    onCreateClassSessionQr: () -> Unit,
    onCreateMachineQr: () -> Unit,
    onLogout: () -> Unit
) {
    var selectedTab by rememberSaveable { mutableStateOf(AdminTab.HOME) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF3F4F1)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.weight(1f)) {
                when (selectedTab) {
                    AdminTab.HOME -> AdminHomeTab(
                        uiState = uiState,
                        onRefresh = onRefresh,
                        onLogout = onLogout,
                        onNavigateToUsers = { selectedTab = AdminTab.USERS },
                        onNavigateToQr = { selectedTab = AdminTab.QR },
                        onNavigateToActivity = { selectedTab = AdminTab.ACTIVITY }
                    )

                    AdminTab.USERS -> AdminUsersTab(
                        uiState = uiState,
                        onRefresh = onRefresh,
                        onLogout = onLogout
                    )

                    AdminTab.QR -> AdminQrTab(
                        uiState = uiState,
                        onRefresh = onRefresh,
                        onQrClassSessionIdChange = onQrClassSessionIdChange,
                        onQrMachineIdChange = onQrMachineIdChange,
                        onCreateEntryQr = onCreateEntryQr,
                        onCreateClassSessionQr = onCreateClassSessionQr,
                        onCreateMachineQr = onCreateMachineQr,
                        onLogout = onLogout
                    )

                    AdminTab.ACTIVITY -> AdminActivityTab(
                        uiState = uiState,
                        onRefresh = onRefresh,
                        onLogout = onLogout
                    )

                    AdminTab.PROFILE -> AdminProfileTab(
                        uiState = uiState,
                        onLogout = onLogout
                    )
                }
            }

            NavigationBar(containerColor = Color.White) {
                AdminTab.entries.forEach { tab ->
                    NavigationBarItem(
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab },
                        icon = { Icon(tab.icon, contentDescription = tab.label) },
                        label = { Text(tab.label) }
                    )
                }
            }
        }
    }
}

@Composable
private fun AdminHomeTab(
    uiState: AdminHomeUiState,
    onRefresh: () -> Unit,
    onLogout: () -> Unit,
    onNavigateToUsers: () -> Unit,
    onNavigateToQr: () -> Unit,
    onNavigateToActivity: () -> Unit
) {
    val activeMembers = uiState.users.count { it.activo && it.role.isClientRole() }
    val trainers = uiState.users.count { it.role.isTrainerRole() }
    val machines = uiState.machines.size
    val attendances = uiState.attendances.size

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            HeroHeader(
                eyebrow = "Panel de administracion",
                title = "GymApp",
                pillLabel = "Administrador",
                pillColor = Color(0xFF1DAA64),
                onLogout = onLogout
            )
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    MetricCard("Socios activos", activeMembers.toString(), Modifier.weight(1f))
                    MetricCard("Entrenadores", trainers.toString(), Modifier.weight(1f))
                }
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    MetricCard("Maquinas", machines.toString(), Modifier.weight(1f))
                    MetricCard("Asistencias", attendances.toString(), Modifier.weight(1f))
                }
            }
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "Acciones rapidas",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                QuickActionCard(
                    title = "Gestion de usuarios",
                    subtitle = "$activeMembers socios activos y ${uiState.users.size} usuarios totales",
                    icon = Icons.Default.Groups,
                    accent = Color(0xFFDCE8FF),
                    onClick = onNavigateToUsers
                )
                QuickActionCard(
                    title = "Generar QR",
                    subtitle = "${uiState.qrCodes.size} codigos disponibles para entrada, maquinas y clases",
                    icon = Icons.Default.QrCode2,
                    accent = Color(0xFFDDF8E6),
                    onClick = onNavigateToQr
                )
                QuickActionCard(
                    title = "Maquinas",
                    subtitle = "$machines maquinas registradas",
                    icon = Icons.Default.SportsGymnastics,
                    accent = Color(0xFFFFE1DA),
                    onClick = onNavigateToActivity
                )
            }
        }

        item {
            SectionHeader(
                title = "Resumen real del sistema",
                action = "Recargar",
                onAction = onRefresh
            )
        }

        if (uiState.errorMessage != null) {
            item {
                Text(
                    text = uiState.errorMessage,
                    color = Color(0xFFD92D20),
                    modifier = Modifier.padding(horizontal = 18.dp)
                )
            }
        }

        item {
            Text(
                text = "Usuarios recientes",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 18.dp)
            )
        }

        items(uiState.users.take(4)) { user ->
            UserCard(user = user, modifier = Modifier.padding(horizontal = 18.dp))
        }
    }
}

@Composable
private fun AdminUsersTab(
    uiState: AdminHomeUiState,
    onRefresh: () -> Unit,
    onLogout: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            HeroHeader(
                eyebrow = "Gestion de usuarios",
                title = "Usuarios y roles",
                pillLabel = "Administrador",
                pillColor = Color(0xFF1DAA64),
                onLogout = onLogout
            )
        }

        item {
            SectionHeader(
                title = "Listado completo",
                action = "Recargar",
                onAction = onRefresh
            )
        }

        items(uiState.users) { user ->
            UserCard(user = user, modifier = Modifier.padding(horizontal = 18.dp))
        }
    }
}

@Composable
private fun AdminQrTab(
    uiState: AdminHomeUiState,
    onRefresh: () -> Unit,
    onQrClassSessionIdChange: (String) -> Unit,
    onQrMachineIdChange: (String) -> Unit,
    onCreateEntryQr: () -> Unit,
    onCreateClassSessionQr: () -> Unit,
    onCreateMachineQr: () -> Unit,
    onLogout: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            HeroHeader(
                eyebrow = "Generacion y consulta",
                title = "Codigos QR",
                pillLabel = "Administrador",
                pillColor = Color(0xFF1DAA64),
                onLogout = onLogout
            )
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                QuickActionCard(
                    title = "Entrada al gimnasio",
                    subtitle = "Los codigos QR de entrada aparecen listados abajo.",
                    icon = Icons.Default.QrCode2,
                    accent = Color(0xFFDCE8FF),
                    onClick = onCreateEntryQr
                )
                QuickActionCard(
                    title = "Maquinas y clases",
                    subtitle = "Los codigos vinculados a maquinas o sesiones se muestran con su referencia.",
                    icon = Icons.Default.SportsGymnastics,
                    accent = Color(0xFFDDF8E6),
                    onClick = onRefresh
                )
            }
        }

        item {
            SectionHeader(
                title = "Codigos QR reales",
                action = "Recargar",
                onAction = onRefresh
            )
        }

        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(22.dp)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Crear nuevo QR",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    if (uiState.classSessions.isNotEmpty()) {
                        Text(
                            text = "Sesiones disponibles: " + uiState.classSessions
                                .take(3)
                                .joinToString { "#${it.id} ${it.gymClassName}" },
                            color = Color(0xFF667085)
                        )
                    }
                    Button(
                        onClick = onCreateEntryQr,
                        enabled = !uiState.isCreatingQr,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (uiState.isCreatingQr) "Creando..." else "Crear QR de entrada")
                    }
                    OutlinedTextField(
                        value = uiState.qrMachineIdInput,
                        onValueChange = onQrMachineIdChange,
                        label = { Text("ID de maquina") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                    )
                    Button(
                        onClick = onCreateMachineQr,
                        enabled = !uiState.isCreatingQr,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (uiState.isCreatingQr) "Creando..." else "Crear QR de maquina")
                    }
                    OutlinedTextField(
                        value = uiState.qrClassSessionIdInput,
                        onValueChange = onQrClassSessionIdChange,
                        label = { Text("ID de sesion de clase") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                    )
                    Button(
                        onClick = onCreateClassSessionQr,
                        enabled = !uiState.isCreatingQr,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (uiState.isCreatingQr) "Creando..." else "Crear QR de sesion")
                    }
                    uiState.qrCreateMessage?.let { message ->
                        Text(
                            text = message,
                            color = if (message.contains("correctamente", ignoreCase = true)) {
                                Color(0xFF16A34A)
                            } else {
                                Color(0xFFD92D20)
                            }
                        )
                    }
                }
            }
        }

        if (uiState.qrCodes.isEmpty()) {
            item {
                Text(
                    text = "Todavia no hay codigos QR cargados.",
                    color = Color(0xFF667085),
                    modifier = Modifier.padding(horizontal = 18.dp)
                )
            }
        } else {
            items(uiState.qrCodes) { qrCode ->
                QrCodeCard(qrCode = qrCode, modifier = Modifier.padding(horizontal = 18.dp))
            }
        }
    }
}

@Composable
private fun AdminActivityTab(
    uiState: AdminHomeUiState,
    onRefresh: () -> Unit,
    onLogout: () -> Unit
) {
    val entryAttendances = uiState.attendances.count { it.qrType.equals("ENTRY", ignoreCase = true) }
    val machineAttendances = uiState.attendances.count { it.qrType.equals("MACHINE", ignoreCase = true) }
    val classAttendances = uiState.attendances.count { it.qrType.equals("CLASS_SESSION", ignoreCase = true) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            HeroHeader(
                eyebrow = "Actividad del gimnasio",
                title = "Asistencias QR",
                pillLabel = "Administrador",
                pillColor = Color(0xFF1DAA64),
                onLogout = onLogout
            )
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    MetricCard("Entradas", entryAttendances.toString(), Modifier.weight(1f))
                    MetricCard("Maquinas", machineAttendances.toString(), Modifier.weight(1f))
                }
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    MetricCard("Clases", classAttendances.toString(), Modifier.weight(1f))
                    MetricCard("Total", uiState.attendances.size.toString(), Modifier.weight(1f))
                }
            }
        }

        item {
            SectionHeader(
                title = "Ultimos registros",
                action = "Recargar",
                onAction = onRefresh
            )
        }

        if (uiState.attendances.isEmpty()) {
            item {
                Text(
                    text = "Todavia no hay asistencias registradas.",
                    color = Color(0xFF667085),
                    modifier = Modifier.padding(horizontal = 18.dp)
                )
            }
        } else {
            items(uiState.attendances.take(10)) { attendance ->
                AttendanceCard(attendance = attendance, modifier = Modifier.padding(horizontal = 18.dp))
            }
        }
    }
}

@Composable
private fun AdminProfileTab(
    uiState: AdminHomeUiState,
    onLogout: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            HeroHeader(
                eyebrow = "Tu perfil",
                title = uiState.userName,
                pillLabel = "Administrador",
                pillColor = Color(0xFF1DAA64),
                onLogout = onLogout
            )
        }

        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(22.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    ProfileLine("Nombre", uiState.userName)
                    ProfileLine("Rol", uiState.role)
                }
            }
        }
    }
}

@Composable
private fun HeroHeader(
    eyebrow: String,
    title: String,
    pillLabel: String,
    pillColor: Color,
    onLogout: () -> Unit
) {
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
                    color = Color(0xFF062B18),
                    shape = RoundedCornerShape(28.dp)
                )
                .padding(22.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        text = title,
                        color = Color.White,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    RolePill(pillLabel, pillColor)
                }

                IconButton(onClick = onLogout) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Volver al login",
                        tint = Color.White
                    )
                }
            }
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

@Composable
private fun MetricCard(label: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(text = value, fontWeight = FontWeight.ExtraBold, fontSize = 28.sp, color = Color(0xFF0F172A))
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = label, color = Color(0xFF98A2B3))
        }
    }
}

@Composable
private fun QuickActionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    accent: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .background(accent, RoundedCornerShape(14.dp))
                        .padding(10.dp)
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color(0xFF344054)
                    )
                }

                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = subtitle,
                        color = Color(0xFF98A2B3)
                    )
                }
            }

            Text(text = ">", color = Color(0xFF98A2B3), fontSize = 24.sp)
        }
    }
}

@Composable
private fun SectionHeader(title: String, action: String, onAction: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Button(onClick = onAction) { Text(action) }
    }
}

@Composable
private fun UserCard(user: UserSummaryResponse, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(22.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = user.nombre,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = user.email,
                color = Color(0xFF667085)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Rol: ${user.role}",
                color = Color(0xFF0F172A),
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = if (user.activo) "Estado: activo" else "Estado: inactivo",
                color = if (user.activo) Color(0xFF16A34A) else Color(0xFFD92D20),
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun QrCodeCard(qrCode: QrCodeResponse, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(22.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = "QR #${qrCode.id}",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Tipo: ${qrCode.tipo}",
                color = Color(0xFF667085)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = if (qrCode.esEntradaGimnasio) "Entrada del gimnasio" else "QR asociado",
                color = Color(0xFF0F172A),
                fontWeight = FontWeight.SemiBold
            )
            qrCode.maquinaNombre?.let { machineName ->
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Maquina: $machineName", color = Color(0xFF667085))
            }
            qrCode.sesionClaseResumen?.let { sessionSummary ->
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Sesion: $sessionSummary", color = Color(0xFF667085))
            }
        }
    }
}

@Composable
private fun AttendanceCard(attendance: AttendanceResponse, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(22.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = attendance.usuarioNombre,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Tipo QR: ${attendance.qrType}",
                color = Color(0xFF667085)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "QR #${attendance.qrCodeId}",
                color = Color(0xFF667085)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Registro #${attendance.id}",
                color = Color(0xFF0F172A),
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun ProfileLine(label: String, value: String) {
    Column {
        Text(text = label, color = Color(0xFF98A2B3))
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = value, color = Color(0xFF0F172A), fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(12.dp))
    }
}

private fun String.isClientRole(): Boolean {
    return equals("CLIENT", ignoreCase = true) || equals("CLIENTE", ignoreCase = true)
}

private fun String.isTrainerRole(): Boolean {
    return equals("TRAINER", ignoreCase = true) || equals("ENTRENADOR", ignoreCase = true)
}
