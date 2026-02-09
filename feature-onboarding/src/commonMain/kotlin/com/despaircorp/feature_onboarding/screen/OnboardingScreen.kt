package com.despaircorp.feature_onboarding.screen

import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.despaircorp.design_system.theme.TrackShiftTheme
import com.despaircorp.feature_onboarding.screen.sub_screens.ConversionTutoScreen
import com.despaircorp.feature_onboarding.screen.sub_screens.ScreenshotTutoScreen
import com.despaircorp.feature_onboarding.screen.sub_screens.TrollScreen
import com.despaircorp.feature_onboarding.screen.sub_screens.UniversalLinkTutoScreen
import com.despaircorp.feature_onboarding.view_model.OnboardingViewModel
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun OnboardingScreen(
    onOnboardFinish: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: OnboardingViewModel = koinViewModel()
) {

    val isOnboardCompleted = viewModel.isOnboardCompleted.collectAsStateWithLifecycle()
    val currentOnOnboardFinish by rememberUpdatedState(onOnboardFinish)

    LaunchedEffect(isOnboardCompleted.value) {
        if (isOnboardCompleted.value) {
            currentOnOnboardFinish()
        }
    }

    OnboardingContent(
        onOnboardFinish = {
            viewModel.setOnboardCompleted()
        }
    )
}

@Composable
fun OnboardingContent(
    onOnboardFinish: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val pagerState = rememberPagerState(pageCount = { 4 })
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
                2 -> ConversionTutoScreen(
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(
                                page = 3,
                                animationSpec = tween(
                                    durationMillis = 800,
                                    easing = EaseInOutCubic
                                )
                            )
                        }
                    }
                )
                3 -> {
                    TrollScreen(
                        onClick = { onOnboardFinish() }
                    )
                }
            }
        }

    }
}

@Composable
@Preview
private fun OnboardingContentPreview() {
    TrackShiftTheme {
        OnboardingContent(
            onOnboardFinish = {

            }
        )
    }
}