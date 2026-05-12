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
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.Groups
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
import com.hazelgym.mobile.data.model.GymClassResponse
import com.hazelgym.mobile.data.model.RoutineAssignmentResponse
import com.hazelgym.mobile.ui.viewmodel.TrainerHomeUiState

private enum class TrainerTab(val label: String, val icon: ImageVector) {
    HOME("Inicio", Icons.Default.Home),
    ACTIVITY("Actividad", Icons.Default.EventAvailable),
    PROFILE("Perfil", Icons.Default.Person)
}

@Composable
fun TrainerHomeScreen(
    uiState: TrainerHomeUiState,
    onRefresh: () -> Unit,
    onLogout: () -> Unit,
    onNavigateToClasses: () -> Unit,
    onNavigateToAssignments: () -> Unit,
    onNavigateToAttendances: () -> Unit
) {
    var selectedTab by rememberSaveable { mutableStateOf(TrainerTab.HOME) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF3F4F1)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.weight(1f)) {
                when (selectedTab) {
                    TrainerTab.HOME -> TrainerHomeTab(
                        uiState = uiState,
                        onRefresh = onRefresh,
                        onLogout = onLogout,
                        onNavigateToClasses = onNavigateToClasses,
                        onNavigateToAssignments = onNavigateToAssignments,
                        onNavigateToAttendances = onNavigateToAttendances
                    )
                    TrainerTab.ACTIVITY -> TrainerActivityTab(
                        uiState = uiState,
                        onRefresh = onRefresh,
                        onLogout = onLogout
                    )
                    TrainerTab.PROFILE -> TrainerProfileTab(
                        uiState = uiState,
                        onLogout = onLogout
                    )
                }
            }

            NavigationBar(containerColor = Color.White) {
                TrainerTab.entries.forEach { tab ->
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
private fun TrainerHomeTab(
    uiState: TrainerHomeUiState,
    onRefresh: () -> Unit,
    onLogout: () -> Unit,
    onNavigateToClasses: () -> Unit,
    onNavigateToAssignments: () -> Unit,
    onNavigateToAttendances: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            HeaderCard(
                eyebrow = "Panel de entrenador",
                name = uiState.userName,
                pillLabel = "Entrenadora certificada",
                pillColor = Color(0xFF6B8DFF),
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
                    MetricCard("Clases", uiState.classes.size.toString(), Modifier.weight(1f))
                    MetricCard("Asistencias", uiState.attendances.size.toString(), Modifier.weight(1f))
                }

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    MetricCard("Asignaciones", uiState.routineAssignments.size.toString(), Modifier.weight(1f))
                    MetricCard("Perfil", "Entrenador", Modifier.weight(1f))
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
                    title = "Clases de hoy",
                    subtitle = "${uiState.classes.count { it.activa }} clases activas para dirigir",
                    icon = Icons.Default.EventAvailable,
                    accent = Color(0xFFDCE8FF),
                    onClick = onNavigateToClasses
                )
                QuickActionCard(
                    title = "Mis clientes",
                    subtitle = "${uiState.routineAssignments.size} asignaciones con clientes",
                    icon = Icons.Default.Groups,
                    accent = Color(0xFFDDF8E6),
                    onClick = onNavigateToAssignments
                )
                QuickActionCard(
                    title = "Clases este mes",
                    subtitle = "${uiState.attendances.size} registros asociados a tu seguimiento",
                    icon = Icons.Default.Badge,
                    accent = Color(0xFFFFE1DA),
                    onClick = onNavigateToAttendances
                )
            }
        }

        item {
            SectionHeader(
                title = "Resumen real del entrenador",
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
                text = "Clases disponibles",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 18.dp)
            )
        }

        items(uiState.classes.take(3)) { gymClass ->
            GymClassCard(gymClass = gymClass, modifier = Modifier.padding(horizontal = 18.dp))
        }
    }
}

@Composable
private fun TrainerActivityTab(
    uiState: TrainerHomeUiState,
    onRefresh: () -> Unit,
    onLogout: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            HeaderCard(
                eyebrow = "Actividad y seguimiento",
                name = uiState.userName,
                pillLabel = "Entrenador",
                pillColor = Color(0xFF6B8DFF),
                onLogout = onLogout
            )
        }

        item {
            SectionHeader(
                title = "Detalle operativo",
                action = "Recargar",
                onAction = onRefresh
            )
        }

        item {
            Text(
                text = "Rutinas asignadas",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 18.dp)
            )
        }

        items(uiState.routineAssignments.take(4)) { assignment ->
            RoutineAssignmentCard(assignment = assignment, modifier = Modifier.padding(horizontal = 18.dp))
        }

        item {
            Text(
                text = "Asistencias recientes",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 18.dp)
            )
        }

        items(uiState.attendances.take(4)) { attendance ->
            AttendanceCard(attendance = attendance, modifier = Modifier.padding(horizontal = 18.dp))
        }
    }
}

@Composable
private fun TrainerProfileTab(
    uiState: TrainerHomeUiState,
    onLogout: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            HeaderCard(
                eyebrow = "Tu perfil",
                name = uiState.userName,
                pillLabel = "Entrenador",
                pillColor = Color(0xFF6B8DFF),
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
                    ProfileLine("Email", uiState.email)
                    ProfileLine("Rol", uiState.role)
                }
            }
        }
    }
}

@Composable
private fun HeaderCard(
    eyebrow: String,
    name: String,
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
                    color = Color(0xFF0D0D14),
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
                        text = name,
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
        Text(text = label, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
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
                    Icon(imageVector = icon, contentDescription = null, tint = Color(0xFF344054))
                }

                Column {
                    Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = subtitle, color = Color(0xFF98A2B3))
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
private fun GymClassCard(gymClass: GymClassResponse, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(22.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(text = gymClass.nombre, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            if (!gymClass.descripcion.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = gymClass.descripcion, color = Color(0xFF667085))
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "Entrenador: ${gymClass.entrenadorNombre}", color = Color(0xFF0F172A), fontWeight = FontWeight.SemiBold)
            gymClass.duracion?.let { duration ->
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Duracion: $duration min", color = Color(0xFF667085))
            }
        }
    }
}

@Composable
private fun RoutineAssignmentCard(assignment: RoutineAssignmentResponse, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
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
private fun AttendanceCard(attendance: AttendanceResponse, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(22.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(text = attendance.usuarioNombre, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = "Tipo QR: ${attendance.qrType}", color = Color(0xFF667085))
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "Registro #${attendance.id}", color = Color(0xFF0F172A), fontWeight = FontWeight.SemiBold)
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
