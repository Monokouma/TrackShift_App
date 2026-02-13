package com.despaircorp.feature_profile.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.despaircorp.design_system.theme.TrackShiftTheme
import com.despaircorp.feature_profile.model.UiProfileModel
import com.despaircorp.feature_profile.screen.components.MonthlyUsageCard
import com.despaircorp.feature_profile.screen.components.ProfileNameAndImage
import com.despaircorp.feature_profile.screen.components.StatsCard
import com.despaircorp.feature_profile.ui_state.ProfileUiState
import com.despaircorp.feature_profile.view_model.ProfileViewModel
import com.moriafly.salt.ui.rememberScrollState
import com.moriafly.salt.ui.verticalScroll
import org.koin.compose.viewmodel.koinViewModel

@Suppress("ParamsComparedByRef")
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = koinViewModel()
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val pullToRefreshState = rememberPullToRefreshState()
    val isRefreshing = viewModel.isRefreshing.collectAsStateWithLifecycle()

    when (val state = uiState.value) {
        is ProfileUiState.Error -> {

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
                    modifier = Modifier.verticalScroll(rememberScrollState())
                )
            }

        }

        is ProfileUiState.Loading -> {

        }
    }
}

@Composable
private fun ProfileScreenContent(
    userUiProfileModel: UiProfileModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(80.dp))

        ProfileNameAndImage(
            onProfilePictureClick = {},
            onUsernameEditClick = {},
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
            onUpgradeClick = {},
            isPro = userUiProfileModel.isPro,
            modifier = Modifier.padding(8.dp)
        )

        Spacer(Modifier.weight(1f))
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
            )
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
            )
        )
    }
}
