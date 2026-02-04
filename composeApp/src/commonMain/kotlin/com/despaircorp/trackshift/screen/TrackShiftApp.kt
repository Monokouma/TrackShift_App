package com.despaircorp.trackshift.screen

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.despaircorp.feature_auth.screen.AuthScreen
import com.despaircorp.feature_splash_screen.screen.SplashScreen
import com.despaircorp.navigation.NavigationRoute
import com.despaircorp.trackshift.view_model.TrackShiftAppViewModel
import kotlinx.coroutines.delay
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Duration.Companion.seconds

@Composable
fun TrackShiftApp(
    modifier: Modifier = Modifier,
    viewModel: TrackShiftAppViewModel = koinViewModel()
) {
    val navController = rememberNavController()

    val isAuthenticated = viewModel.isAuthenticatedStateFlow.collectAsStateWithLifecycle()

    LaunchedEffect(isAuthenticated.value) {
        delay(2.seconds)
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
        startDestination = NavigationRoute.Splash.route,
        enterTransition = { fadeIn(animationSpec = tween(250)) },
        exitTransition = { fadeOut(animationSpec = tween(250)) },
        popEnterTransition = { fadeIn(animationSpec = tween(250)) },
        popExitTransition = { fadeOut(animationSpec = tween(250)) },
    ) {

        composable(NavigationRoute.Splash.route) {
            SplashScreen()
        }

        composable(NavigationRoute.Home.route) {
            Text("Home")
        }

        composable(NavigationRoute.Auth.route) {
            AuthScreen()
        }
    }
}