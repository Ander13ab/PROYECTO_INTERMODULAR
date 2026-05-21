package com.hazelgym.mobile.ui

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hazelgym.mobile.data.remote.ApiClient
import com.hazelgym.mobile.data.repository.AuthRepository
import com.hazelgym.mobile.data.repository.MachineRepository
import com.hazelgym.mobile.data.session.SessionStorage
import com.hazelgym.mobile.ui.screens.AdminHomeScreen
import com.hazelgym.mobile.ui.screens.AdminActivityDetailScreen
import com.hazelgym.mobile.ui.screens.AdminMachinesDetailScreen
import com.hazelgym.mobile.ui.screens.AdminQrDetailScreen
import com.hazelgym.mobile.ui.screens.AdminUsersDetailScreen
import com.hazelgym.mobile.ui.screens.ClientAttendancesDetailScreen
import com.hazelgym.mobile.ui.screens.ClientHomeScreen
import com.hazelgym.mobile.ui.screens.ClientClassesDetailScreen
import com.hazelgym.mobile.ui.screens.ClientMachinesDetailScreen
import com.hazelgym.mobile.ui.screens.ClientRoutinesDetailScreen
import com.hazelgym.mobile.ui.screens.LoginScreen
import com.hazelgym.mobile.ui.screens.TrainerAssignmentsDetailScreen
import com.hazelgym.mobile.ui.screens.TrainerAttendancesDetailScreen
import com.hazelgym.mobile.ui.screens.TrainerClassesDetailScreen
import com.hazelgym.mobile.ui.screens.TrainerHomeScreen
import com.hazelgym.mobile.ui.screens.TrainerRoutinesDetailScreen
import com.hazelgym.mobile.ui.viewmodel.AdminHomeViewModel
import com.hazelgym.mobile.ui.viewmodel.ClientHomeViewModel
import com.hazelgym.mobile.ui.viewmodel.LoginRole
import com.hazelgym.mobile.ui.viewmodel.LoginViewModel
import com.hazelgym.mobile.ui.viewmodel.TrainerHomeViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private object Routes {
    const val Login = "login"
    const val ClientHome = "client_home"
    const val ClientRoutines = "client_routines"
    const val ClientClasses = "client_classes"
    const val ClientMachines = "client_machines"
    const val ClientAttendances = "client_attendances"
    const val TrainerHome = "trainer_home"
    const val TrainerClasses = "trainer_classes"
    const val TrainerRoutines = "trainer_routines"
    const val TrainerAssignments = "trainer_assignments"
    const val TrainerAttendances = "trainer_attendances"
    const val AdminHome = "admin_home"
    const val AdminMachines = "admin_machines"
    const val AdminUsers = "admin_users"
    const val AdminQr = "admin_qr"
    const val AdminActivity = "admin_activity"
}

@Composable
fun HazelGymMobileApp() {
    val navController = rememberNavController()
    val rootViewModel: RootViewModel = viewModel(
        factory = RootViewModel.factory()
    )
    val sessionState by rootViewModel.session.collectAsState(initial = null)
    val isReady by rootViewModel.isReady.collectAsState()

    LaunchedEffect(isReady, sessionState) {
        if (!isReady) return@LaunchedEffect
        val currentSession = sessionState
        val targetRoute = when {
            currentSession == null -> Routes.Login
            LoginRole.ADMIN.matchesBackendRole(currentSession.role) -> Routes.AdminHome
            LoginRole.ENTRENADOR.matchesBackendRole(currentSession.role) -> Routes.TrainerHome
            else -> Routes.ClientHome
        }
        navController.navigateToRoot(targetRoute)
    }

    NavHost(
        navController = navController,
        startDestination = Routes.Login
    ) {
        composable(Routes.Login) {
            val loginViewModel: LoginViewModel = viewModel(
                factory = LoginViewModel.factory()
            )
            LoginScreen(
                uiState = loginViewModel.uiState.collectAsState().value,
                onEmailChange = loginViewModel::updateEmail,
                onPasswordChange = loginViewModel::updatePassword,
                onRoleSelected = loginViewModel::selectRole,
                onLoginClick = loginViewModel::login
            )
        }
        composable(Routes.ClientHome) {
            val clientHomeViewModel: ClientHomeViewModel = viewModel(
                factory = ClientHomeViewModel.factory()
            )
            ClientHomeScreen(
                uiState = clientHomeViewModel.uiState.collectAsState().value,
                heroLabel = "Tu zona de entrenamiento",
                sectionTitle = "Máquinas para tu rutina",
                primaryMetricLabel = "Rutinas",
                secondaryMetricLabel = "Clases",
                onRefresh = clientHomeViewModel::refresh,
                onLogout = rootViewModel::logout,
                onQrCodeChange = clientHomeViewModel::updateQrCodeInput,
                onQrScanned = clientHomeViewModel::registerScannedAttendance,
                onMachineQrScanned = clientHomeViewModel::registerScannedMachine,
                onRegisterAttendance = clientHomeViewModel::registerAttendance,
                onNavigateToRoutines = { navController.navigate(Routes.ClientRoutines) },
                onNavigateToClasses = { navController.navigate(Routes.ClientClasses) },
                onNavigateToMachines = { navController.navigate(Routes.ClientMachines) },
                onNavigateToAttendances = { navController.navigate(Routes.ClientAttendances) },
                onClearScannedMachine = clientHomeViewModel::clearScannedMachine
            )
        }
        composable(Routes.ClientRoutines) {
            val clientHomeViewModel: ClientHomeViewModel = viewModel(factory = ClientHomeViewModel.factory())
            ClientRoutinesDetailScreen(
                uiState = clientHomeViewModel.uiState.collectAsState().value,
                onBack = { navController.popBackStack() }
            )
        }
        composable(Routes.ClientClasses) {
            val clientHomeViewModel: ClientHomeViewModel = viewModel(factory = ClientHomeViewModel.factory())
            ClientClassesDetailScreen(
                uiState = clientHomeViewModel.uiState.collectAsState().value,
                onBack = { navController.popBackStack() }
            )
        }
        composable(Routes.ClientMachines) {
            val clientHomeViewModel: ClientHomeViewModel = viewModel(factory = ClientHomeViewModel.factory())
            ClientMachinesDetailScreen(
                uiState = clientHomeViewModel.uiState.collectAsState().value,
                onBack = { navController.popBackStack() }
            )
        }
        composable(Routes.ClientAttendances) {
            val clientHomeViewModel: ClientHomeViewModel = viewModel(factory = ClientHomeViewModel.factory())
            ClientAttendancesDetailScreen(
                uiState = clientHomeViewModel.uiState.collectAsState().value,
                onBack = { navController.popBackStack() }
            )
        }
        composable(Routes.TrainerHome) {
            val trainerHomeViewModel: TrainerHomeViewModel = viewModel(
                factory = TrainerHomeViewModel.factory()
            )
            TrainerHomeScreen(
                uiState = trainerHomeViewModel.uiState.collectAsState().value,
                onRefresh = trainerHomeViewModel::refresh,
                onLogout = rootViewModel::logout,
                onNavigateToClasses = { navController.navigate(Routes.TrainerClasses) },
                onNavigateToRoutines = { navController.navigate(Routes.TrainerRoutines) },
                onNavigateToAssignments = { navController.navigate(Routes.TrainerAssignments) },
                onNavigateToAttendances = { navController.navigate(Routes.TrainerAttendances) }
            )
        }
        composable(Routes.TrainerClasses) {
            val trainerHomeViewModel: TrainerHomeViewModel = viewModel(factory = TrainerHomeViewModel.factory())
            TrainerClassesDetailScreen(
                uiState = trainerHomeViewModel.uiState.collectAsState().value,
                onClassNameChange = trainerHomeViewModel::updateClassNameInput,
                onClassDescriptionChange = trainerHomeViewModel::updateClassDescriptionInput,
                onClassDurationChange = trainerHomeViewModel::updateClassDurationInput,
                onClassActiveChange = trainerHomeViewModel::updateClassActiveInput,
                onSaveClass = trainerHomeViewModel::saveClass,
                onDeleteClass = trainerHomeViewModel::deleteEditingClass,
                onStartNewClass = trainerHomeViewModel::startNewClass,
                onEditClass = trainerHomeViewModel::editClass,
                onBack = { navController.popBackStack() }
            )
        }
        composable(Routes.TrainerRoutines) {
            val trainerHomeViewModel: TrainerHomeViewModel = viewModel(factory = TrainerHomeViewModel.factory())
            TrainerRoutinesDetailScreen(
                uiState = trainerHomeViewModel.uiState.collectAsState().value,
                onRoutineNameChange = trainerHomeViewModel::updateRoutineNameInput,
                onRoutineDescriptionChange = trainerHomeViewModel::updateRoutineDescriptionInput,
                onSaveRoutine = trainerHomeViewModel::saveRoutine,
                onDeleteRoutine = trainerHomeViewModel::deleteEditingRoutine,
                onStartNewRoutine = trainerHomeViewModel::startNewRoutine,
                onEditRoutine = trainerHomeViewModel::editRoutine,
                onBack = { navController.popBackStack() }
            )
        }
        composable(Routes.TrainerAssignments) {
            val trainerHomeViewModel: TrainerHomeViewModel = viewModel(factory = TrainerHomeViewModel.factory())
            TrainerAssignmentsDetailScreen(
                uiState = trainerHomeViewModel.uiState.collectAsState().value,
                onAssignmentRoutineIdChange = trainerHomeViewModel::updateAssignmentRoutineIdInput,
                onAssignmentClientIdChange = trainerHomeViewModel::updateAssignmentClientIdInput,
                onSelectRoutine = trainerHomeViewModel::selectAssignmentRoutine,
                onSelectClient = trainerHomeViewModel::selectAssignmentClient,
                onSaveAssignment = trainerHomeViewModel::saveAssignment,
                onDeleteAssignment = trainerHomeViewModel::deleteEditingAssignment,
                onStartNewAssignment = trainerHomeViewModel::startNewAssignment,
                onEditAssignment = trainerHomeViewModel::editAssignment,
                onBack = { navController.popBackStack() }
            )
        }
        composable(Routes.TrainerAttendances) {
            val trainerHomeViewModel: TrainerHomeViewModel = viewModel(factory = TrainerHomeViewModel.factory())
            TrainerAttendancesDetailScreen(
                uiState = trainerHomeViewModel.uiState.collectAsState().value,
                onBack = { navController.popBackStack() }
            )
        }
        composable(Routes.AdminHome) {
            val adminHomeViewModel: AdminHomeViewModel = viewModel(
                factory = AdminHomeViewModel.factory()
            )
            AdminHomeScreen(
                uiState = adminHomeViewModel.uiState.collectAsState().value,
                onRefresh = adminHomeViewModel::refresh,
                onQrClassSessionIdChange = adminHomeViewModel::updateQrClassSessionIdInput,
                onQrMachineIdChange = adminHomeViewModel::updateQrMachineIdInput,
                onCreateEntryQr = adminHomeViewModel::createEntryQrCode,
                onCreateClassSessionQr = adminHomeViewModel::createClassSessionQrCode,
                onCreateMachineQr = adminHomeViewModel::createMachineQrCode,
                onLogout = rootViewModel::logout,
                onNavigateToUsers = { navController.navigate(Routes.AdminUsers) },
                onNavigateToQr = { navController.navigate(Routes.AdminQr) },
                onNavigateToMachines = { navController.navigate(Routes.AdminMachines) },
                onNavigateToActivity = { navController.navigate(Routes.AdminActivity) }
            )
        }
        composable(Routes.AdminMachines) {
            val adminHomeViewModel: AdminHomeViewModel = viewModel(factory = AdminHomeViewModel.factory())
            AdminMachinesDetailScreen(
                uiState = adminHomeViewModel.uiState.collectAsState().value,
                onMachineNameChange = adminHomeViewModel::updateMachineNameInput,
                onMachineDescriptionChange = adminHomeViewModel::updateMachineDescriptionInput,
                onMachineMuscleGroupChange = adminHomeViewModel::updateMachineMuscleGroupInput,
                onMachineInstructionsChange = adminHomeViewModel::updateMachineInstructionsInput,
                onMachineLevelChange = adminHomeViewModel::updateMachineLevelInput,
                onMachineSafetyWarningChange = adminHomeViewModel::updateMachineSafetyWarningInput,
                onMachineMediaUrlChange = adminHomeViewModel::updateMachineMediaUrlInput,
                onMachineStatusChange = adminHomeViewModel::updateMachineStatusInput,
                onSaveMachine = adminHomeViewModel::saveMachine,
                onDeleteMachine = adminHomeViewModel::deleteEditingMachine,
                onStartNewMachine = adminHomeViewModel::startNewMachine,
                onEditMachine = adminHomeViewModel::editMachine,
                onBack = { navController.popBackStack() }
            )
        }
        composable(Routes.AdminUsers) {
            val adminHomeViewModel: AdminHomeViewModel = viewModel(factory = AdminHomeViewModel.factory())
            AdminUsersDetailScreen(
                uiState = adminHomeViewModel.uiState.collectAsState().value,
                onUserNameChange = adminHomeViewModel::updateUserNameInput,
                onUserEmailChange = adminHomeViewModel::updateUserEmailInput,
                onUserPasswordChange = adminHomeViewModel::updateUserPasswordInput,
                onUserRoleChange = adminHomeViewModel::updateUserRoleInput,
                onUserActiveChange = adminHomeViewModel::updateUserActiveInput,
                onSaveUser = adminHomeViewModel::saveUser,
                onDeleteUser = adminHomeViewModel::deleteEditingUser,
                onStartNewUser = adminHomeViewModel::startNewUser,
                onEditUser = adminHomeViewModel::editUser,
                onBack = { navController.popBackStack() }
            )
        }
        composable(Routes.AdminQr) {
            val adminHomeViewModel: AdminHomeViewModel = viewModel(factory = AdminHomeViewModel.factory())
            AdminQrDetailScreen(
                uiState = adminHomeViewModel.uiState.collectAsState().value,
                onBack = { navController.popBackStack() }
            )
        }
        composable(Routes.AdminActivity) {
            val adminHomeViewModel: AdminHomeViewModel = viewModel(factory = AdminHomeViewModel.factory())
            AdminActivityDetailScreen(
                uiState = adminHomeViewModel.uiState.collectAsState().value,
                onBack = { navController.popBackStack() }
            )
        }
    }
}

private fun NavHostController.navigateToRoot(route: String) {
    navigate(route) {
        popUpTo(graph.startDestinationId) {
            inclusive = true
        }
        launchSingleTop = true
    }
}

class AppContainer(application: Application) {
    val sessionStorage = SessionStorage(application.applicationContext)
    val authRepository = AuthRepository(ApiClient.authApi)
    val machineRepository = MachineRepository(ApiClient.machineApi)
}

abstract class ContainerAndroidViewModel(application: Application) : AndroidViewModel(application) {
    protected val container = AppContainer(application)
}

class RootViewModel(application: Application) : ContainerAndroidViewModel(application) {
    val session = container.sessionStorage.session
    private val _isReady = MutableStateFlow(false)
    val isReady: StateFlow<Boolean> = _isReady.asStateFlow()

    init {
        viewModelScope.launch {
            container.sessionStorage.clear()
            _isReady.value = true
        }
    }

    fun logout() {
        viewModelScope.launch {
            container.sessionStorage.clear()
        }
    }

    companion object {
        fun factory(): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: androidx.lifecycle.viewmodel.CreationExtras): T {
                val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                return RootViewModel(application) as T
            }
        }
    }
}
