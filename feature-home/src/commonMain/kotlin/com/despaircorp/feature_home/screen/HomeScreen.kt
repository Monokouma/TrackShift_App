package com.despaircorp.feature_home.screen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.despaircorp.design_system.theme.TrackShiftTheme
import com.despaircorp.feature_home.model.HomeTab
import com.despaircorp.feature_home.ui_state.HomeUiState
import com.despaircorp.feature_home.view_model.HomeViewModel
import com.despaircorp.feature_profile.screen.ProfileScreen
import com.despaircorp.feature_shift.screen.ShiftScreen
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    showPaywall: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeContent(
        uiState = uiState,
        onTabSelect = viewModel::onTabSelected,
        modifier = modifier,
        showPaywall = {
            showPaywall()
        }
    )
}

@Composable
private fun HomeContent(
    uiState: HomeUiState,
    onTabSelect: (HomeTab) -> Unit,
    showPaywall: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 88.dp)
        ) {
            when (uiState.currentTab) {
                HomeTab.PROFILE -> ProfileScreen(
                    showPaywall = {
                        showPaywall()
                    }
                )
                HomeTab.SHIFT -> ShiftScreen(
                    onShowPaywall = {

                    }
                )
                HomeTab.HISTORY -> HistoryPlaceholder()
            }
        }

        HomeBottomBar(
            currentTab = uiState.currentTab,
            onTabSelected = onTabSelect,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun HomeBottomBar(
    currentTab: HomeTab,
    onTabSelected: (HomeTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .padding(horizontal = 32.dp, vertical = 16.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.95f))
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.15f),
                        Color.White.copy(alpha = 0.05f)
                    )
                ),
                shape = RoundedCornerShape(24.dp)
            )
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BottomBarTab(
            icon = Icons.Default.Person,
            selected = currentTab == HomeTab.PROFILE,
            onClick = { onTabSelected(HomeTab.PROFILE) }
        )

        BottomBarTab(
            icon = Icons.Default.SwapHoriz,
            selected = currentTab == HomeTab.SHIFT,
            onClick = { onTabSelected(HomeTab.SHIFT) }
        )

        BottomBarTab(
            icon = Icons.Default.History,
            selected = currentTab == HomeTab.HISTORY,
            onClick = { onTabSelected(HomeTab.HISTORY) }
        )
    }
}

@Composable
private fun BottomBarTab(
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (selected) 1.1f else 1f,
        animationSpec = tween(200)
    )

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .then(
                if (selected) {
                    Modifier.background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
                } else {
                    Modifier
                }
            )
            .clickable { onClick() }
            .padding(horizontal = 24.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (selected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            },
            modifier = Modifier
                .size(24.dp)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                }
        )
    }
}

@Composable
private fun ShiftPlaceholder() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Shift",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
private fun HistoryPlaceholder() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "History",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    TrackShiftTheme {
        HomeContent(
            HomeUiState(
                HomeTab.SHIFT
            ),
            onTabSelect = {

            },
            showPaywall = {

            }
        )
    }
}