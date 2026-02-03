package com.despaircorp.trackshift

import androidx.compose.ui.window.ComposeUIViewController
import com.despaircorp.design_system.theme.TrackShiftTheme
import com.despaircorp.trackshift.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    TrackShiftTheme { }
}