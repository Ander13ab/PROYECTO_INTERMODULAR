package com.hazelgym.mobile.ui.screens

import android.Manifest
import android.content.pm.PackageManager
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
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.hazelgym.mobile.data.model.GymClassResponse
import com.hazelgym.mobile.data.model.RoutineResponse
import com.hazelgym.mobile.ui.viewmodel.ClientHomeUiState
import java.util.concurrent.Executors

private enum class ClientTab(val label: String, val icon: ImageVector) {
    HOME("Inicio", Icons.Default.FitnessCenter),
    QR("QR", Icons.Default.QrCode2),
    MACHINES("Maquinas", Icons.Default.TaskAlt),
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
    onQrScanned: (String) -> Unit,
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
                        onLogout = onLogout,
                        onNavigateToQr = { selectedTab = ClientTab.QR },
                        onNavigateToMachines = { selectedTab = ClientTab.MACHINES },
                        onNavigateToProfile = { selectedTab = ClientTab.PROFILE }
                    )

                    ClientTab.QR -> ClientQrTab(
                        uiState = uiState,
                        onQrCodeChange = onQrCodeChange,
                        onQrScanned = onQrScanned,
                        onRegisterAttendance = onRegisterAttendance,
                        onLogout = onLogout
                    )

                    ClientTab.MACHINES -> ClientMachinesTab(
                        uiState = uiState,
                        onRefresh = onRefresh,
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
    onLogout: () -> Unit,
    onNavigateToQr: () -> Unit,
    onNavigateToMachines: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            HeaderCard(
                eyebrow = "Hola de nuevo",
                name = uiState.userName,
                pillLabel = heroLabel,
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
                Text(
                    text = "Acciones rapidas",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                QuickActionCard(
                    title = "Escanear entrada",
                    subtitle = "Registra tu visita de hoy desde el area QR.",
                    icon = Icons.Default.QrCode2,
                    accent = Color(0xFFDCE8FF),
                    onClick = onNavigateToQr
                )
                QuickActionCard(
                    title = "Mis rutinas",
                    subtitle = "${uiState.routines.size} planes disponibles para entrenar",
                    icon = Icons.Default.TaskAlt,
                    accent = Color(0xFFFFE1DA),
                    onClick = onNavigateToProfile
                )
                QuickActionCard(
                    title = sectionTitle,
                    subtitle = "${uiState.machines.size} maquinas preparadas para tu rutina",
                    icon = Icons.Default.FitnessCenter,
                    accent = Color(0xFFDDF8E6),
                    onClick = onNavigateToMachines
                )
                QuickActionCard(
                    title = "Clases activas",
                    subtitle = "${uiState.classes.count { it.activa }} clases activas para reservar",
                    icon = Icons.Default.CalendarMonth,
                    accent = Color(0xFFDCE8FF),
                    onClick = onNavigateToProfile
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
    onQrScanned: (String) -> Unit,
    onRegisterAttendance: () -> Unit,
    onLogout: () -> Unit
) {
    var scannerOpen by rememberSaveable { mutableStateOf(false) }

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
                    subtitle = "Escanea el codigo para registrar la asistencia al momento, o usa el ID manual.",
                    icon = Icons.Default.QrCode2,
                    accent = Color(0xFFDCE8FF),
                    onClick = { scannerOpen = true }
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
                        Button(
                            onClick = { scannerOpen = true },
                            enabled = !uiState.isSubmittingAttendance,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(imageVector = Icons.Default.QrCode2, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(if (uiState.isSubmittingAttendance) "Registrando..." else "Escanear y registrar QR")
                        }
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
                                color = if (message.contains("registrada", ignoreCase = true)) {
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

    if (scannerOpen) {
        QrScannerDialog(
            onDismiss = { scannerOpen = false },
            onQrScanned = { value ->
                scannerOpen = false
                onQrScanned(value)
            }
        )
    }
}

@Composable
private fun ClientMachinesTab(
    uiState: ClientHomeUiState,
    onRefresh: () -> Unit,
    onLogout: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            HeaderCard(
                eyebrow = "Catalogo de maquinas",
                name = uiState.userName,
                pillLabel = "Cliente",
                pillColor = Color(0xFFFF6B50),
                onLogout = onLogout
            )
        }

        item {
            SectionHeader(
                title = "Maquinas del gimnasio",
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

        if (uiState.machines.isEmpty()) {
            item {
                Text(
                    text = "Todavia no hay maquinas cargadas.",
                    color = Color(0xFF667085),
                    modifier = Modifier.padding(horizontal = 18.dp)
                )
            }
        } else {
            items(uiState.machines) { machine ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(22.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = machine.nombre,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )
                        if (!machine.descripcion.isNullOrBlank()) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = machine.descripcion,
                                color = Color(0xFF667085)
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Estado: ${machine.estado}",
                            color = Color(0xFFFF4D2E),
                            fontWeight = FontWeight.SemiBold
                        )
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
private fun QrScannerDialog(
    onDismiss: () -> Unit,
    onQrScanned: (String) -> Unit
) {
    val context = LocalContext.current
    var testQrId by rememberSaveable { mutableStateOf("1") }
    var hasCameraPermission by rememberSaveable {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasCameraPermission = granted
    }

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            color = Color.White,
            shape = RoundedCornerShape(26.dp)
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Escanear QR",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )
                Text(
                    text = "En el emulador puedes probar con un ID si la camara no tiene un QR delante.",
                    color = Color(0xFF667085),
                    style = MaterialTheme.typography.bodyMedium
                )
                if (hasCameraPermission) {
                    QrCameraPreview(onQrScanned = onQrScanned)
                } else {
                    Text(
                        text = "Activa el permiso de camara para escanear codigos QR.",
                        color = Color(0xFFD92D20)
                    )
                }

                OutlinedTextField(
                    value = testQrId,
                    onValueChange = { testQrId = it },
                    label = { Text("ID de QR para probar") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                )

                Button(
                    onClick = { onQrScanned(testQrId) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Usar este ID como QR")
                }

                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Cerrar")
                }
            }
        }
    }
}

@Composable
private fun QrCameraPreview(
    onQrScanned: (String) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val analysisExecutor = remember { Executors.newSingleThreadExecutor() }

    DisposableEffect(Unit) {
        onDispose {
            runCatching { cameraProviderFuture.get().unbindAll() }
            analysisExecutor.shutdown()
        }
    }

    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp),
        factory = { viewContext ->
            val previewView = PreviewView(viewContext)
            val executor = ContextCompat.getMainExecutor(viewContext)

            cameraProviderFuture.addListener(
                {
                    val cameraProvider = cameraProviderFuture.get()
                    val preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }
                    val analyzer = ImageAnalysis.Builder()
                            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                            .build()
                            .also {
                                it.setAnalyzer(analysisExecutor, QrCodeAnalyzer(onQrScanned))
                            }

                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        CameraSelector.DEFAULT_BACK_CAMERA,
                        preview,
                        analyzer
                    )
                },
                executor
            )

            previewView
        }
    )
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
                .background(Color.White, RoundedCornerShape(20.dp))
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

private class QrCodeAnalyzer(
    private val onQrScanned: (String) -> Unit
) : ImageAnalysis.Analyzer {
    private val scanner = BarcodeScanning.getClient()
    private var hasScanned = false

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage == null || hasScanned) {
            imageProxy.close()
            return
        }

        val image = InputImage.fromMediaImage(
            mediaImage,
            imageProxy.imageInfo.rotationDegrees
        )

        scanner.process(image)
            .addOnSuccessListener { barcodes ->
                val rawValue = barcodes
                    .firstOrNull { barcode ->
                        barcode.format == Barcode.FORMAT_QR_CODE && !barcode.rawValue.isNullOrBlank()
                    }
                    ?.rawValue

                if (rawValue != null && !hasScanned) {
                    hasScanned = true
                    onQrScanned(rawValue)
                }
            }
            .addOnCompleteListener {
                imageProxy.close()
            }
    }
}
