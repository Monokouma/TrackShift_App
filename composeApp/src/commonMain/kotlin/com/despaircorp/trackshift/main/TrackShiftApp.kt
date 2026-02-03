package com.despaircorp.trackshift.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.despaircorp.feature_splash_screen.ui.SplashScreen
import com.despaircorp.navigation.NavigationRoute

@Composable
fun TrackShiftApp(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavigationRoute.Splash.route
    ) {
        composable(NavigationRoute.Splash.route) {
            SplashScreen()
        }
    }
}