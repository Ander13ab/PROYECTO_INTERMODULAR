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
import com.hazelgym.mobile.ui.screens.ClientHomeScreen
import com.hazelgym.mobile.ui.screens.LoginScreen
import com.hazelgym.mobile.ui.screens.TrainerHomeScreen
import com.hazelgym.mobile.ui.viewmodel.AdminHomeViewModel
import com.hazelgym.mobile.ui.viewmodel.ClientHomeViewModel
import com.hazelgym.mobile.ui.viewmodel.LoginRole
import com.hazelgym.mobile.ui.viewmodel.LoginViewModel
import com.hazelgym.mobile.ui.viewmodel.TrainerHomeViewModel
import kotlinx.coroutines.launch

private object Routes {
    const val Login = "login"
    const val ClientHome = "client_home"
    const val TrainerHome = "trainer_home"
    const val AdminHome = "admin_home"
}

@Composable
fun HazelGymMobileApp() {
    val navController = rememberNavController()
    val rootViewModel: RootViewModel = viewModel(
        factory = RootViewModel.factory()
    )
    val sessionState by rootViewModel.session.collectAsState(initial = null)

    LaunchedEffect(sessionState) {
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
                sectionTitle = "Maquinas para tu rutina",
                primaryMetricLabel = "Rutinas",
                secondaryMetricLabel = "Clases",
                secondaryMetricValue = "Cliente",
                onRefresh = clientHomeViewModel::refresh,
                onLogout = rootViewModel::logout
            )
        }
        composable(Routes.TrainerHome) {
            val trainerHomeViewModel: TrainerHomeViewModel = viewModel(
                factory = TrainerHomeViewModel.factory()
            )
            TrainerHomeScreen(
                uiState = trainerHomeViewModel.uiState.collectAsState().value,
                onRefresh = trainerHomeViewModel::refresh,
                onLogout = rootViewModel::logout
            )
        }
        composable(Routes.AdminHome) {
            val adminHomeViewModel: AdminHomeViewModel = viewModel(
                factory = AdminHomeViewModel.factory()
            )
            AdminHomeScreen(
                uiState = adminHomeViewModel.uiState.collectAsState().value,
                onRefresh = adminHomeViewModel::refresh,
                onLogout = rootViewModel::logout
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
