package com.despaircorp.feature_link_generation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.despaircorp.design_system.theme.TrackShiftTheme
import com.despaircorp.feature_link_generation.screen.components.alert.UrlGenerationErrorAlert
import com.despaircorp.feature_link_generation.screen.components.alert.UrlGenerationLoadingAlert
import com.despaircorp.feature_link_generation.screen.components.alert.UrlGenerationSuccessAlert
import com.despaircorp.feature_link_generation.screen.components.input.UrlInput
import com.despaircorp.feature_link_generation.ui_state.LinkGenerationUiState
import com.despaircorp.feature_link_generation.view_model.LinkGenerationViewModel
import com.despaircorp.utils.ClipboardManager
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LinkGenerationScreen(
    onShowPaywall: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LinkGenerationViewModel = koinViewModel()
) {
    val uisState = viewModel.uiState.collectAsStateWithLifecycle()
    val clipboardManager: ClipboardManager = koinInject()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Box(modifier = modifier.fillMaxSize()) {

        LinkGenerationScreenContent(
            onShowPaywall = onShowPaywall,
            onEventConsume = viewModel::onEventConsumed,
            uiState = uisState.value,
            onCopyUrl = { url ->
                clipboardManager.copyToClipboard(url)
                scope.launch {
                    snackbarHostState.showSnackbar("Lien copié")
                }
            },
            onUrlSubmit = { url ->
                viewModel.onUrlSubmit(url)
            },
            modifier = Modifier
        )

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun LinkGenerationScreenContent(
    onShowPaywall: () -> Unit,
    onEventConsume: () -> Unit,
    onCopyUrl: (String) -> Unit,
    uiState: LinkGenerationUiState,
    onUrlSubmit: (String) -> Unit,
    modifier: Modifier = Modifier,
) {

    var showLoading by rememberSaveable {
        mutableStateOf(false)
    }

    var showTrackShiftUrlPopUp by rememberSaveable {
        mutableStateOf(false)
    }

    var showErrorPopUp by rememberSaveable {
        mutableStateOf(false)
    }

    var trackShiftUrl by rememberSaveable {
        mutableStateOf("")
    }

    var errorMessage by rememberSaveable {
        mutableStateOf("")
    }

    when (uiState) {
        LinkGenerationUiState.Idle -> Unit

        LinkGenerationUiState.LimitReach -> {
            onShowPaywall()
            showLoading = false
            onEventConsume()
        }

        is LinkGenerationUiState.Error -> {
            errorMessage = uiState.message
            showErrorPopUp = true
            showLoading = false
            onEventConsume()
        }

        is LinkGenerationUiState.Success -> {
            trackShiftUrl = uiState.trackshiftUrl
            showTrackShiftUrlPopUp = true
            showLoading = false
            onEventConsume()
        }

        LinkGenerationUiState.Loading -> {
            showLoading = true
        }
    }


    if (showTrackShiftUrlPopUp) {
        UrlGenerationSuccessAlert(
            onDismissRequest = {
                showTrackShiftUrlPopUp = false
            },
            onCopyUrl = {
                showTrackShiftUrlPopUp = false
                onCopyUrl(trackShiftUrl)
            },
            trackShiftUrl = trackShiftUrl,
        )
    }

    if (showErrorPopUp) {
        UrlGenerationErrorAlert(
            onDismissRequest = {
                showErrorPopUp = false
            },
            errorMessage = errorMessage
        )
    }

    if (showLoading) {
        UrlGenerationLoadingAlert()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            "Ici, tu peux créer un lien universel",
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(40.dp))

        Text(
            "Colle l'url d'une playlist publique",
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(20.dp))

        UrlInput(
            onSubmit = {
                onUrlSubmit(it)
            }
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            " Ou, utilise des screenshots : ",
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
@Preview
private fun LinkGenerationScreenPreview() {
    TrackShiftTheme {
        LinkGenerationScreenContent(
            onShowPaywall = {

            },
            onUrlSubmit = {

            },
            onEventConsume = {},
            uiState = LinkGenerationUiState.Loading,
            onCopyUrl = { },
        )
    }
}