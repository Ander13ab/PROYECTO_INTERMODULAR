package com.hazelgym.mobile.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.TaskAlt
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
import com.hazelgym.mobile.data.model.GymClassResponse
import com.hazelgym.mobile.data.model.RoutineResponse
import com.hazelgym.mobile.ui.viewmodel.ClientHomeUiState

private enum class ClientTab(val label: String, val icon: ImageVector) {
    HOME("Inicio", Icons.Default.FitnessCenter),
    QR("QR", Icons.Default.QrCode2),
    PROFILE("Perfil", Icons.Default.Person)
}

@Composable
fun ClientHomeScreen(
    uiState: ClientHomeUiState,
    heroLabel: String,
    sectionTitle: String,
    primaryMetricLabel: String,
    secondaryMetricLabel: String,
    secondaryMetricValue: String,
    onRefresh: () -> Unit,
    onLogout: () -> Unit,
    onQrCodeChange: (String) -> Unit,
    onRegisterAttendance: () -> Unit
) {
    var selectedTab by rememberSaveable { mutableStateOf(ClientTab.HOME) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF3F4F1)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.weight(1f)) {
                when (selectedTab) {
                    ClientTab.HOME -> ClientHomeTab(
                        uiState = uiState,
                        heroLabel = heroLabel,
                        sectionTitle = sectionTitle,
                        primaryMetricLabel = primaryMetricLabel,
                        secondaryMetricLabel = secondaryMetricLabel,
                        secondaryMetricValue = secondaryMetricValue,
                        onRefresh = onRefresh,
                        onLogout = onLogout
                    )

                    ClientTab.QR -> ClientQrTab(
                        uiState = uiState,
                        onQrCodeChange = onQrCodeChange,
                        onRegisterAttendance = onRegisterAttendance,
                        onLogout = onLogout
                    )

                    ClientTab.PROFILE -> ClientProfileTab(
                        uiState = uiState,
                        onLogout = onLogout
                    )
                }
            }

            NavigationBar(containerColor = Color.White) {
                ClientTab.entries.forEach { tab ->
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
private fun ClientHomeTab(
    uiState: ClientHomeUiState,
    heroLabel: String,
    sectionTitle: String,
    primaryMetricLabel: String,
    secondaryMetricLabel: String,
    secondaryMetricValue: String,
    onRefresh: () -> Unit,
    onLogout: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            HeaderCard(
                eyebrow = heroLabel,
                name = uiState.userName,
                pillLabel = "Cliente",
                pillColor = Color(0xFFFF6B50),
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
                    MetricCard(primaryMetricLabel, uiState.routines.size.toString(), Modifier.weight(1f))
                    MetricCard(secondaryMetricLabel, uiState.classes.size.toString(), Modifier.weight(1f))
                }

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    MetricCard("Maquinas", uiState.machines.size.toString(), Modifier.weight(1f))
                    MetricCard("Perfil", secondaryMetricValue, Modifier.weight(1f))
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
                QuickActionCard(
                    title = "Mis rutinas",
                    subtitle = "${uiState.routines.size} planes disponibles para entrenar",
                    icon = Icons.Default.TaskAlt,
                    accent = Color(0xFFFFE1DA)
                )
                QuickActionCard(
                    title = "Clases activas",
                    subtitle = "${uiState.classes.count { it.activa }} clases activas para reservar",
                    icon = Icons.Default.CalendarMonth,
                    accent = Color(0xFFDCE8FF)
                )
                QuickActionCard(
                    title = sectionTitle,
                    subtitle = "${uiState.machines.size} maquinas preparadas para tu rutina",
                    icon = Icons.Default.FitnessCenter,
                    accent = Color(0xFFDDF8E6)
                )
            }
        }

        item {
            SectionHeader(
                title = "Resumen real del cliente",
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
                text = "Rutinas disponibles",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 18.dp)
            )
        }

        items(uiState.routines.take(3)) { routine ->
            RoutineCard(routine = routine, modifier = Modifier.padding(horizontal = 18.dp))
        }

        item {
            Text(
                text = "Clases activas",
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
private fun ClientQrTab(
    uiState: ClientHomeUiState,
    onQrCodeChange: (String) -> Unit,
    onRegisterAttendance: () -> Unit,
    onLogout: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            HeaderCard(
                eyebrow = "Acceso rapido por QR",
                name = uiState.userName,
                pillLabel = "Cliente",
                pillColor = Color(0xFFFF6B50),
                onLogout = onLogout
            )
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                QuickActionCard(
                    title = "Registrar asistencia",
                    subtitle = "Introduce el identificador del QR para registrar tu entrada o uso.",
                    icon = Icons.Default.QrCode2,
                    accent = Color(0xFFDCE8FF)
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(22.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = "ID del QR",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedTextField(
                            value = uiState.qrCodeInput,
                            onValueChange = onQrCodeChange,
                            label = { Text("Ejemplo: 1") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp)
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Button(
                            onClick = onRegisterAttendance,
                            enabled = !uiState.isSubmittingAttendance,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(if (uiState.isSubmittingAttendance) "Registrando..." else "Registrar asistencia")
                        }
                        uiState.attendanceMessage?.let { message ->
                            Spacer(modifier = Modifier.height(12.dp))
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
        }
    }
}

@Composable
private fun ClientProfileTab(
    uiState: ClientHomeUiState,
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
                pillLabel = "Cliente",
                pillColor = Color(0xFFFF6B50),
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
private fun QuickActionCard(title: String, subtitle: String, icon: ImageVector, accent: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
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

            Text(text = "›", color = Color(0xFF98A2B3), fontSize = 24.sp)
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
private fun RoutineCard(routine: RoutineResponse, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(22.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(text = routine.nombre, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            if (!routine.descripcion.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = routine.descripcion, color = Color(0xFF667085))
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "Entrenador: ${routine.entrenadorNombre}", color = Color(0xFF0F172A), fontWeight = FontWeight.SemiBold)
        }
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
private fun ProfileLine(label: String, value: String) {
    Column {
        Text(text = label, color = Color(0xFF98A2B3))
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = value, color = Color(0xFF0F172A), fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(12.dp))
    }
}
