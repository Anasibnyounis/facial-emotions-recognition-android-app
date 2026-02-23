package com.sakhidev.fer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.sakhidev.fer.navigation.AppNavigation
import com.sakhidev.fer.ui.theme.FerTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Entry point of the app.
 * - Installs the Android 12+ splash screen
 * - Sets edge-to-edge display
 * - Hosts AppNavigation inside FerTheme
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            FerTheme {
                AppNavigation()
            }
        }
    }
}
