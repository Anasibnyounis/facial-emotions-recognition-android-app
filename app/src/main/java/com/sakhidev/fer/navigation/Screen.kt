package com.sakhidev.fer.navigation

/**
 * Sealed class for all navigation routes in the app.
 */
sealed class Screen(val route: String) {
    object Splash  : Screen("splash")
    object Landing : Screen("landing")
    object Camera  : Screen("camera")
}
