package com.despaircorp.feature_shift.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
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
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults.contentPadding
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode.Companion.Screen
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.despaircorp.design_system.theme.TrackShiftTheme
import com.despaircorp.feature_link_generation.screen.LinkGenerationScreen
import com.despaircorp.feature_shift.model.ShiftTab
import com.despaircorp.feature_shift.view_model.ShiftViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ShiftScreen(
    modifier: Modifier = Modifier,
    onShowPaywall: () -> Unit,
    viewModel: ShiftViewModel = koinViewModel()
) {
    ShiftScreenContent(
        onShowPaywall = onShowPaywall
    )
}

@Composable
private fun ShiftScreenContent(
    onShowPaywall: () -> Unit,
    modifier: Modifier = Modifier
) {

    val startDestination = ShiftTab.SCREENSHOT
    var selectedDestination by rememberSaveable { mutableIntStateOf(startDestination.ordinal) }

    Column(
        modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Spacer(modifier = Modifier.height(60.dp))

        PrimaryTabRow(
            selectedTabIndex = selectedDestination,
            containerColor = Color.Transparent,
            divider = {},
        ) {
            ShiftTab.entries.forEachIndexed { index, destination ->
                Tab(
                    selected = selectedDestination == index,
                    selectedContentColor = MaterialTheme.colorScheme.primary,
                    unselectedContentColor = MaterialTheme.colorScheme.onBackground,
                    onClick = {
                        selectedDestination = index
                    },
                    text = {
                        Text(
                            text = destination.name,
                            style = MaterialTheme.typography.headlineSmall,
                        )
                    }
                )
            }
        }

        AnimatedContent(
            targetState = selectedDestination,
            transitionSpec = {
                if (targetState > initialState) {
                    slideInHorizontally { it } togetherWith slideOutHorizontally { -it }
                } else {
                    slideInHorizontally { -it } togetherWith slideOutHorizontally { it }
                }
            }
        ) { index ->
            when (ShiftTab.entries[index]) {
                ShiftTab.SCREENSHOT -> {
                    LinkGenerationScreen(onShowPaywall = onShowPaywall)
                }
                ShiftTab.LINK -> {
                    Text("Link", color = MaterialTheme.colorScheme.onBackground)
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}


@Composable
@Preview
private fun ShiftScreenContentPreview() {
    TrackShiftTheme {
        ShiftScreenContent(
            onShowPaywall = {

            }
        )
    }
}