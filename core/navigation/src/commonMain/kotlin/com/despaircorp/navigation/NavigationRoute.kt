package com.despaircorp.navigation

sealed class NavigationRoute(val route: String) {
    object Splash : NavigationRoute("splash")

    object Home : NavigationRoute("home")

    object Auth : NavigationRoute("auth")

    object Onboard : NavigationRoute("onboard")

    object Paywall: NavigationRoute("paywall")
}


