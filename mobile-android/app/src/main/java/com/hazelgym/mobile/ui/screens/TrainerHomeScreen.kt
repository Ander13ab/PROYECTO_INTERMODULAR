package com.hazelgym.mobile.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hazelgym.mobile.data.model.AttendanceResponse
import com.hazelgym.mobile.data.model.GymClassResponse
import com.hazelgym.mobile.data.model.RoutineAssignmentResponse
import com.hazelgym.mobile.ui.viewmodel.TrainerHomeUiState

@Composable
fun TrainerHomeScreen(
    uiState: TrainerHomeUiState,
    onRefresh: () -> Unit,
    onLogout: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF2F5F2)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color(0xFF0D0D14),
                            shape = RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp)
                        )
                        .padding(24.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Panel de seguimiento",
                                color = Color.White.copy(alpha = 0.8f)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = uiState.userName,
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 28.sp
                            )
                        }
                        IconButton(onClick = onLogout) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Volver al login",
                                tint = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Rol: ${uiState.role}",
                        color = Color(0xFFFFB09B)
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    DashboardMetric("Clases", uiState.classes.size.toString())
                    DashboardMetric("Asistencias", uiState.attendances.size.toString())
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Actividad del entrenador",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Button(onClick = onRefresh) {
                        Text("Recargar")
                    }
                }
            }

            if (uiState.errorMessage != null) {
                item {
                    Text(
                        text = uiState.errorMessage,
                        color = Color(0xFFD92D20),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }

            item {
                Text(
                    text = "Clases disponibles",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            items(uiState.classes) { gymClass ->
                GymClassCard(
                    gymClass = gymClass,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            item {
                Text(
                    text = "Rutinas asignadas",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            items(uiState.routineAssignments) { assignment ->
                RoutineAssignmentCard(
                    assignment = assignment,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            item {
                Text(
                    text = "Asistencias recientes",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            items(uiState.attendances) { attendance ->
                AttendanceCard(
                    attendance = attendance,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }
}

@Composable
private fun DashboardMetric(label: String, value: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = value, fontWeight = FontWeight.ExtraBold, fontSize = 24.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = label, color = Color(0xFF667085))
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
            Text(
                text = gymClass.nombre,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
            if (!gymClass.descripcion.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = gymClass.descripcion,
                    color = Color(0xFF667085)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Entrenador: ${gymClass.entrenadorNombre}",
                color = Color(0xFF0F172A),
                fontWeight = FontWeight.SemiBold
            )
            gymClass.duracion?.let { duration ->
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Duracion: $duration min",
                    color = Color(0xFF667085)
                )
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
            Text(
                text = assignment.routineName,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Cliente: ${assignment.clientName}",
                color = Color(0xFF667085)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Asignacion #${assignment.id}",
                color = Color(0xFF0F172A),
                fontWeight = FontWeight.SemiBold
            )
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
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Registro #${attendance.id}",
                color = Color(0xFF0F172A),
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
