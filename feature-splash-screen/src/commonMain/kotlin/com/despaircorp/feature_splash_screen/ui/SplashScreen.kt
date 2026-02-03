package com.despaircorp.feature_splash_screen.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.despaircorp.design_system.resources.Res
import com.despaircorp.design_system.resources.app_name
import com.despaircorp.design_system.theme.TrackShiftTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Text(stringResource(Res.string.app_name))
    }
}

@Preview(showBackground = true)
@Composable
private fun SplashScreenDarkPreview() {
    TrackShiftTheme(darkTheme = true) {
        SplashScreen()
    }
}

@Preview(showBackground = true)
@Composable
private fun SplashScreenLightPreview() {
    TrackShiftTheme(darkTheme = false) {
        SplashScreen()
    }
}