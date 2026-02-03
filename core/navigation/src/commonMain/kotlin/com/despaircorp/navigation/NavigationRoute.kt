package com.despaircorp.navigation

sealed class NavigationRoute(val route: String) {
    object Splash : NavigationRoute("splash")
}