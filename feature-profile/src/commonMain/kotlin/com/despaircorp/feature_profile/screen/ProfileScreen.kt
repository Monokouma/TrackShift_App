package com.despaircorp.feature_profile.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.despaircorp.design_system.theme.TrackShiftTheme
import com.despaircorp.feature_profile.model.UiProfileModel
import com.despaircorp.feature_profile.screen.components.EditNameDialog
import com.despaircorp.feature_profile.screen.components.MonthlyUsageCard
import com.despaircorp.feature_profile.screen.components.ProfileNameAndImage
import com.despaircorp.feature_profile.screen.components.StatsCard
import com.despaircorp.feature_profile.ui_state.ProfileUiState
import com.despaircorp.feature_profile.view_model.ProfileViewModel
import com.preat.peekaboo.image.picker.ResizeOptions
import com.preat.peekaboo.image.picker.SelectionMode
import com.preat.peekaboo.image.picker.rememberImagePickerLauncher
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Suppress("ParamsComparedByRef")
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    showSettings: () -> Unit,
    showPaywall: () -> Unit,
    viewModel: ProfileViewModel = koinViewModel()
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val pullToRefreshState = rememberPullToRefreshState()
    val isRefreshing = viewModel.isRefreshing.collectAsStateWithLifecycle()

    when (val state = uiState.value) {
        is ProfileUiState.Error -> {
            ProfileScreenErrorContent(
                message = state.message,
                onRetry = {
                    viewModel.refresh()
                }
            )
        }

        is ProfileUiState.Content -> {
            PullToRefreshBox(
                isRefreshing = isRefreshing.value,
                onRefresh = { viewModel.refresh() },
                state = pullToRefreshState,
                modifier = modifier.fillMaxSize()
            ) {
                ProfileScreenContent(
                    userUiProfileModel = state.uiProfileModel,
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    onNameChange = {
                        viewModel.onNameEdit(newName = it)
                    },
                    onImagePick = {
                        viewModel.updateUserImage(imageBytes = it)
                    },
                    showPaywall = {
                        showPaywall()
                    },
                    showSettings = showSettings
                )
            }

        }

        is ProfileUiState.Loading -> {
            ProfileScreenLoadingContent()
        }
    }
}

@Composable
private fun ProfileScreenContent(
    userUiProfileModel: UiProfileModel,
    onNameChange: (String) -> Unit,
    showSettings: () -> Unit,
    onImagePick: (ByteArray) -> Unit,
    showPaywall: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()

    val imagePicker = rememberImagePickerLauncher(
        selectionMode = SelectionMode.Single,
        scope = scope,
        resizeOptions = ResizeOptions(
            width = 512,
            height = 512,
            resizeThresholdBytes = 512 * 1024L,
            compressionQuality = 0.8
        ),
        onResult = { images: List<ByteArray> ->
            images.firstOrNull()?.let { imageBytes ->
                onImagePick(imageBytes)
            }
        }
    )


    var showNameEdit by rememberSaveable {
        mutableStateOf(false)
    }

    var nameInput by rememberSaveable { mutableStateOf(userUiProfileModel.username) }

    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background).systemBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(Modifier.weight(1f))

            Icon(
                imageVector = Icons.Outlined.MoreVert,
                contentDescription = "Settings",
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(60.dp).clickable(true, onClick = showSettings)
                    .padding(8.dp)
            )

        }

        ProfileNameAndImage(
            onProfilePictureClick = {
                imagePicker.launch()
            },
            onUsernameEditClick = {
                showNameEdit = true
            },
            userName = userUiProfileModel.username,
            imageUrl = userUiProfileModel.imageUrl,
            isPro = userUiProfileModel.isPro
        )

        Spacer(Modifier.height(20.dp))

        StatsCard(
            totalLinksCreated = userUiProfileModel.totalLinksCreated,
            totalLinksConverted = userUiProfileModel.totalLinksConverted,
            totalPlaylistsCreated = userUiProfileModel.totalPlaylistsCreated,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        MonthlyUsageCard(
            linksCreatedMonth = userUiProfileModel.linksCreatedMonth,
            linksConvertedMonth = userUiProfileModel.linksConvertedMonth,
            onUpgradeClick = {
                showPaywall()
            },
            isPro = userUiProfileModel.isPro,
            modifier = Modifier.padding(8.dp)
        )

        Spacer(Modifier.weight(1f))
    }

    if (showNameEdit) {
        EditNameDialog(
            currentName = nameInput,
            onNameChange = { nameInput = it },
            onConfirm = {
                onNameChange(nameInput)
                showNameEdit = false
            },
            onDismiss = {
                nameInput = userUiProfileModel.username
                showNameEdit = false
            }
        )
    }
}

@Composable
private fun ProfileScreenLoadingContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()

    }
}

@Composable
private fun ProfileScreenErrorContent(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = "Erreur",
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(64.dp)
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = onRetry,
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            )
        ) {
            Text("Réessayer")
        }

    }
}

@Composable
@Preview
private fun ProfileScreenProContentPreview() {
    TrackShiftTheme {
        ProfileScreenContent(
            userUiProfileModel = UiProfileModel(
                id = "usr_123456",
                username = "Monokouma",
                imageUrl = "https://example.com/avatar.jpg",
                isPro = true,
                totalLinksCreated = 42,
                totalPlaylistsCreated = 5,
                totalLinksConverted = 28,
                proExpiresAt = "2026-12-31",
                linksConvertedMonth = 7,
                linksCreatedMonth = 3
            ),
            onNameChange = {

            },
            onImagePick = {

            },
            showPaywall = {

            },
            showSettings = {

            }
        )
    }
}

@Composable
@Preview
private fun ProfileScreenFreeContentPreview() {
    TrackShiftTheme {
        ProfileScreenContent(
            userUiProfileModel = UiProfileModel(
                id = "usr_123456",
                username = "Monokouma",
                imageUrl = "https://example.com/avatar.jpg",
                isPro = false,
                totalLinksCreated = 42,
                totalPlaylistsCreated = 5,
                totalLinksConverted = 28,
                proExpiresAt = "2026-12-31",
                linksConvertedMonth = 7,
                linksCreatedMonth = 3
            ),
            onNameChange = {

            },
            onImagePick = {

            },
            showPaywall = {

            },
            showSettings = {

            }
        )
    }
}

@Composable
@Preview
private fun ProfileScreenLoadingPreview() {
    TrackShiftTheme {
        ProfileScreenLoadingContent()
    }
}

@Composable
@Preview
private fun ProfileScreenErrorPreview() {
    TrackShiftTheme {
        ProfileScreenErrorContent(
            "Une erreur est survennue",
            onRetry = {}
        )
    }
}
