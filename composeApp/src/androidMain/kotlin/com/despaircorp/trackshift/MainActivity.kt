package com.despaircorp.trackshift

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.despaircorp.design_system.theme.TrackShiftTheme
import com.despaircorp.trackshift.main.TrackShiftApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            TrackShiftTheme {
                TrackShiftApp()
            }
        }
    }
}
