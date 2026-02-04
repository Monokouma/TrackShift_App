package com.despaircorp.trackshift.screen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.despaircorp.feature_splash_screen.screen.SplashScreen
import com.despaircorp.navigation.NavigationRoute
import com.despaircorp.trackshift.view_model.TrackShiftAppViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TrackShiftApp(
    viewModel: TrackShiftAppViewModel = koinViewModel(),
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()

    val isAuthenticated = viewModel.isAuthenticatedStateFlow.collectAsStateWithLifecycle()

    LaunchedEffect(isAuthenticated.value) {
        when (isAuthenticated.value) {
            true -> navController.navigate(NavigationRoute.Home.route) {
                popUpTo(NavigationRoute.Splash.route) { inclusive = true }
            }
            false -> navController.navigate(NavigationRoute.Auth.route) {
                popUpTo(NavigationRoute.Splash.route) { inclusive = true }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = NavigationRoute.Splash.route
    ) {

        composable(NavigationRoute.Splash.route) {
            SplashScreen()
        }

        composable(NavigationRoute.Home.route) {
            Text("Home")
        }

        composable(NavigationRoute.Auth.route) {
            Text("Auth")
        }
    }
}