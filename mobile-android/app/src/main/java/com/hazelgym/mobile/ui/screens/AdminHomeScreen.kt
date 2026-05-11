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
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.SportsGymnastics
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hazelgym.mobile.data.model.MachineResponse
import com.hazelgym.mobile.data.model.UserSummaryResponse
import com.hazelgym.mobile.ui.viewmodel.AdminHomeUiState

@Composable
fun AdminHomeScreen(
    uiState: AdminHomeUiState,
    onRefresh: () -> Unit,
    onLogout: () -> Unit
) {
    val activeMembers = uiState.users.count { it.activo && it.role.isClientRole() }
    val trainers = uiState.users.count { it.role.isTrainerRole() }
    val machines = uiState.machines.size
    val activeClasses = uiState.classes.count { it.activa }

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
                        text = "Panel de administración",
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
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Column {
                                    Text(
                                        text = "GymApp",
                                        color = Color.White,
                                        fontSize = 30.sp,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                    RolePill("Administrador")
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
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        MetricCard(
                            label = "Socios activos",
                            value = activeMembers.toString(),
                            modifier = Modifier.weight(1f)
                        )
                        MetricCard(
                            label = "Entrenadores",
                            value = trainers.toString(),
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        MetricCard(
                            label = "Máquinas",
                            value = machines.toString(),
                            modifier = Modifier.weight(1f)
                        )
                        MetricCard(
                            label = "Clases activas",
                            value = activeClasses.toString(),
                            modifier = Modifier.weight(1f)
                        )
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
                        title = "Gestión de usuarios",
                        subtitle = "$activeMembers socios activos y ${uiState.users.size} usuarios totales",
                        icon = Icons.Default.Groups,
                        accent = Color(0xFFDCE8FF)
                    )
                    QuickActionCard(
                        title = "Generar QR",
                        subtitle = "Entrada, máquinas y clases",
                        icon = Icons.Default.QrCode2,
                        accent = Color(0xFFDDF8E6)
                    )
                    QuickActionCard(
                        title = "Máquinas",
                        subtitle = "$machines máquinas registradas y ${uiState.classes.size} clases creadas",
                        icon = Icons.Default.SportsGymnastics,
                        accent = Color(0xFFFFE1DA)
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Resumen real del sistema",
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
                UserCard(
                    user = user,
                    modifier = Modifier.padding(horizontal = 18.dp)
                )
            }

            item {
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
private fun RolePill(label: String) {
    Box(
        modifier = Modifier
            .background(Color(0xFF1DAA64), RoundedCornerShape(20.dp))
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
private fun MetricCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = value,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 28.sp,
                color = Color(0xFF0F172A)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                color = Color(0xFF98A2B3)
            )
        }
    }
}

@Composable
private fun QuickActionCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    accent: Color
) {
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

            Text(
                text = "›",
                color = Color(0xFF98A2B3),
                fontSize = 24.sp
            )
        }
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

private fun String.isClientRole(): Boolean {
    return equals("CLIENT", ignoreCase = true) || equals("CLIENTE", ignoreCase = true)
}

private fun String.isTrainerRole(): Boolean {
    return equals("TRAINER", ignoreCase = true) || equals("ENTRENADOR", ignoreCase = true)
}
