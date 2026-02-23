package com.sakhidev.fer.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sakhidev.fer.ui.camera.CameraScreen
import com.sakhidev.fer.ui.landing.LandingScreen
import com.sakhidev.fer.ui.splash.SplashScreen

/**
 * Top-level NavHost wiring Splash → Landing → Camera routes.
 */
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigateToLanding = {
                    navController.navigate(Screen.Landing.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Landing.route) {
            LandingScreen(
                onStartDetection = {
                    navController.navigate(Screen.Camera.route)
                }
            )
        }

        composable(Screen.Camera.route) {
            CameraScreen(
                onBack = {
                    navController.navigateUp()
                }
            )
        }
    }
}
