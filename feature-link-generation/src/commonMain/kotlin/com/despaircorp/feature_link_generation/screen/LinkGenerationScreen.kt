package com.despaircorp.feature_link_generation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.BorderStroke
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
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
import com.despaircorp.feature_link_generation.screen.components.divider.OrDivider
import com.despaircorp.feature_link_generation.screen.components.header.SectionIcon
import com.despaircorp.feature_link_generation.screen.components.input.UrlInput
import com.despaircorp.feature_link_generation.screen.components.screenshot.ScreenshotCountBadge
import com.despaircorp.feature_link_generation.screen.components.screenshot.ScreenshotRow
import com.despaircorp.feature_link_generation.ui_state.LinkGenerationUiState
import com.despaircorp.feature_link_generation.view_model.LinkGenerationViewModel
import com.despaircorp.utils.ClipboardManager
import com.preat.peekaboo.image.picker.ResizeOptions
import com.preat.peekaboo.image.picker.SelectionMode
import com.preat.peekaboo.image.picker.rememberImagePickerLauncher
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
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
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val isPro = viewModel.isUserPro.collectAsStateWithLifecycle()
    val images = viewModel.images.collectAsStateWithLifecycle()
    val maxImages = viewModel.maxImages.collectAsStateWithLifecycle()

    val clipboardManager: ClipboardManager = koinInject()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val resizeOptions = remember {
        ResizeOptions(
            width = 1080,
            height = 1920,
            resizeThresholdBytes = 1024 * 1024L,
            compressionQuality = 0.8
        )
    }

    val addPicker = key(maxImages.value) {
        rememberImagePickerLauncher(
            selectionMode = SelectionMode.Multiple(maxSelection = maxImages.value),
            scope = scope,
            resizeOptions = resizeOptions,
            onResult = { result: List<ByteArray> ->
                viewModel.onImagePickerResult(result)
            }
        )
    }

    val replacePicker = rememberImagePickerLauncher(
        selectionMode = SelectionMode.Single,
        scope = scope,
        resizeOptions = resizeOptions,
        onResult = { result: List<ByteArray> ->
            viewModel.onImagePickerResult(result)
        }
    )

    Box(modifier = modifier.fillMaxSize()) {

        LinkGenerationScreenContent(
            onShowPaywall = onShowPaywall,
            onEventConsume = viewModel::onEventConsumed,
            uiState = uiState.value,
            onCopyUrl = { url ->
                clipboardManager.copyToClipboard(url)
                scope.launch {
                    snackbarHostState.showSnackbar("Lien copié")
                }
            },
            isPro = isPro.value,
            onUrlSubmit = { url ->
                viewModel.onUrlSubmit(url)
            },
            images = images.value,
            maxImages = maxImages.value,
            onAddScreenshot = {
                viewModel.onAddScreenshot()
                addPicker.launch()
            },
            onReplaceScreenshot = { index ->
                viewModel.onReplaceScreenshot(index)
                replacePicker.launch()
            },
            onRemoveScreenshot = { index ->
                viewModel.onRemoveScreenshot(index)
            },
            onScreenshotSubmit = {
                viewModel.onScreenshotSubmit()
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
    isPro: Boolean,
    uiState: LinkGenerationUiState,
    onUrlSubmit: (String) -> Unit,
    images: ImmutableList<ByteArray>,
    maxImages: Int,
    onAddScreenshot: () -> Unit,
    onReplaceScreenshot: (Int) -> Unit,
    onRemoveScreenshot: (Int) -> Unit,
    onScreenshotSubmit: () -> Unit,
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
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        // --- Section 1: URL ---
        SectionIcon(icon = Icons.Filled.Link)

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Colle un lien",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            "Playlist ou album public",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))

        UrlInput(
            onSubmit = { onUrlSubmit(it) }
        )

        // --- Divider ---
        Spacer(modifier = Modifier.height(40.dp))

        OrDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        )

        Spacer(modifier = Modifier.height(40.dp))

        // --- Section 2: Screenshots ---
        SectionIcon(icon = Icons.Filled.CameraAlt)

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                "Utilise des screenshots",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground
            )

            ScreenshotCountBadge(
                count = images.size,
                max = maxImages
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            "Capture ton ecran de playlist",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))

        ScreenshotRow(
            images = images,
            maxImages = maxImages,
            onAddClick = onAddScreenshot,
            onReplaceClick = onReplaceScreenshot,
            onRemoveClick = onRemoveScreenshot,
            modifier = Modifier.fillMaxWidth()
        )

        if (images.isNotEmpty()) {
            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onScreenshotSubmit,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onBackground,
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .height(48.dp)
            ) {
                Text(
                    "Générer",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
@Preview
private fun LinkGenerationScreenPreview() {
    TrackShiftTheme {
        LinkGenerationScreenContent(
            onShowPaywall = {},
            onUrlSubmit = {},
            onEventConsume = {},
            uiState = LinkGenerationUiState.Idle,
            onCopyUrl = {},
            isPro = true,
            images = persistentListOf(),
            maxImages = 5,
            onAddScreenshot = {},
            onReplaceScreenshot = {},
            onRemoveScreenshot = {},
            onScreenshotSubmit = {}
        )
    }
}
