package com.despaircorp.feature_settings.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.despaircorp.design_system.theme.TrackShiftTheme
import com.despaircorp.feature_settings.view_model.SettingsViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = koinViewModel()
) {
    SettingsScreenContent(
        showPaywall = {

        },
        modifier = modifier
    )
}

@Composable
private fun SettingsScreenContent(
    showPaywall: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.background(MaterialTheme.colorScheme.background).fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

    }
}

@Composable
@Preview
private fun SettingsScreenContentPreview() {
    TrackShiftTheme {
        SettingsScreenContent(
            showPaywall = {}
        )
    }
}