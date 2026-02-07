package com.despaircorp.feature_onboarding.screen

import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.despaircorp.design_system.theme.TrackShiftTheme
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(
    modifier: Modifier = Modifier
) {
   OnboardingContent()
}

@Composable
fun OnboardingContent(
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(pageCount = { 3 })
    val scope = rememberCoroutineScope()

    Box(modifier = modifier.fillMaxSize()) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            when (page) {
                0 -> ScreenshotTutoScreen(
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(
                                page = 1,
                                animationSpec = tween(
                                    durationMillis = 800,
                                    easing = EaseInOutCubic
                                )
                            )
                        }
                    }
                )
                1 -> UniversalLinkTutoScreen(
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(
                                page = 2,
                                animationSpec = tween(
                                    durationMillis = 800,
                                    easing = EaseInOutCubic
                                )
                            )
                        }
                    }
                )
                2 -> ConversionTutoScreen()
            }
        }

    }
}

@Composable
@Preview
private fun OnboardingContentPreview() {
    TrackShiftTheme {
        OnboardingContent()
    }
}