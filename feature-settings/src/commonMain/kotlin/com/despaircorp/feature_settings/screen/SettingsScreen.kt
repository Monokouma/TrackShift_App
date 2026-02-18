package com.despaircorp.feature_settings.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.despaircorp.core.secrets.BuildKonfig
import com.despaircorp.design_system.theme.TrackShiftTheme
import com.despaircorp.feature_settings.screen.components.SettingsFooter
import com.despaircorp.feature_settings.screen.components.SettingsTopBar
import com.despaircorp.feature_settings.ui_state.SettingsUiState
import com.despaircorp.feature_settings.view_model.SettingsViewModel
import com.despaircorp.utils.PlatformUtils
import com.despaircorp.utils.SubscriptionsManager
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SettingsScreen(
    showPaywall: () -> Unit,
    onBackPress: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = koinViewModel()
) {

    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val subscriptionsManager: SubscriptionsManager = koinInject()

    SettingsScreenContent(
        showPaywall = {
            showPaywall()
        },
        modifier = modifier,
        onBackPress = {
            onBackPress()
        },
        uiState = uiState,
        onEventConsume = viewModel::onEventConsumed,
        onManageSubscriptionClick = {
            subscriptionsManager.openSubscriptionManagement()
        }
    )
}

@Composable
private fun SettingsScreenContent(
    showPaywall: () -> Unit,
    onBackPress: () -> Unit,
    onManageSubscriptionClick: () -> Unit,
    modifier: Modifier = Modifier,
    uiState: State<SettingsUiState>,
    onEventConsume: () -> Unit
) {

    var showLoading by rememberSaveable {
        mutableStateOf(false)
    }

    var showSuccessPopUp by rememberSaveable {
        mutableStateOf(false)
    }

    var showErrorPopUp by rememberSaveable {
        mutableStateOf(false)
    }

    var errorMessage by rememberSaveable {
        mutableStateOf("")
    }

    var isUserPro by rememberSaveable {
        mutableStateOf(false)
    }

    when (val state = uiState.value) {
        is SettingsUiState.Error -> {
            errorMessage = state.message
            showErrorPopUp = true
            showLoading = false
            onEventConsume()
        }

        SettingsUiState.Idle -> Unit
        SettingsUiState.Loading -> {
            showLoading = true
        }

        is SettingsUiState.Nominal -> {
            isUserPro = state.isUserPro
        }

        SettingsUiState.Success -> {
            showSuccessPopUp = true
            showLoading = false
            onEventConsume()
        }
    }

    Column(
        modifier = modifier.background(MaterialTheme.colorScheme.background).fillMaxSize()
            .systemBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        SettingsTopBar(onBackPress = onBackPress)

        if (isUserPro) {
            Spacer(Modifier.height(40.dp))
            Button(
                onClick = { onManageSubscriptionClick() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 8.dp,
                    pressedElevation = 2.dp
                ),
                contentPadding = PaddingValues(0.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    "Gérer l'abonnement",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        }

        Spacer(Modifier.weight(1f))

        Button(
            onClick = { onManageSubscriptionClick() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 8.dp,
                pressedElevation = 2.dp
            ),
            contentPadding = PaddingValues(0.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Logout,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                "Déconnexion",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleMedium,
            )
        }

        Button(
            onClick = { onManageSubscriptionClick() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onError
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 8.dp,
                pressedElevation = 2.dp
            ),
            contentPadding = PaddingValues(0.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                "Supprimer mon compte",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleMedium,
            )
        }

        SettingsFooter(
            versionName = BuildKonfig.VERSION_NAME,
            onOpenCgu = { PlatformUtils().openUrl("https://trackshift.fr/cgu") },
            onOpenPrivacy = { PlatformUtils().openUrl("https://trackshift.fr/politics") }
        )
    }
}

@Composable
@Preview
private fun SettingsScreenContentPreview() {
    TrackShiftTheme {
        SettingsScreenContent(
            showPaywall = {},
            onBackPress = {},
            uiState = mutableStateOf(SettingsUiState.Nominal(isUserPro = true)),
            onEventConsume = {

            },
            onManageSubscriptionClick = {

            }
        )
    }
}