package com.despaircorp.trackshift

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.despaircorp.design_system.theme.TrackShiftTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            TrackShiftTheme {

            }
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    TrackShiftTheme {

    }
}