package com.despaircorp.feature_screenshot_conversion.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.GetApp
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.SaveAlt
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.OnPlacedModifier
import androidx.compose.ui.unit.dp
import com.despaircorp.design_system.theme.TrackShiftTheme
import com.despaircorp.feature_screenshot_conversion.view_model.ScreenShotViewModel
import io.ktor.util.Platform
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ScreenshotConversionScreen(
    onShowPaywall: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ScreenShotViewModel = koinViewModel()
) {
    ScreenshotConversionScreenContent(
        onShowPaywall = onShowPaywall,
        onUrlSubmit = { url ->
            viewModel.onUrlSubmit(url)
        },
        modifier = modifier
    )
}

@Composable
private fun ScreenshotConversionScreenContent(
    onShowPaywall: () -> Unit,
    onUrlSubmit: (String) -> Unit,
    modifier: Modifier = Modifier,
) {

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
fun UrlInput(
    onSubmit: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var url by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    fun detectPlatform(url: String): Boolean {
        if (url.isBlank()) return false
        return when {
            url.contains("spotify.com") || url.contains("spotify.link") -> true
            url.contains("music.apple.com") || url.contains("itunes.apple.com") -> true
            url.contains("deezer.com") || url.contains("deezer.page.link") -> true
            url.contains("soundcloud.com") -> true
            else -> false
        }
    }

    fun detectContentType(url: String): Boolean {
        if (url.isBlank()) return false
        return when {
            url.contains("/playlist/") || url.contains("/sets/") -> true
            url.contains("/album/") || url.contains("/album?") -> true
            else -> false
        }
    }

    fun handleSubmit() {
        when {
            !detectPlatform(url) -> error = "Plateforme non supportée"
            !detectContentType(url) -> error = "Seuls les playlists et albums sont supportés"
            else -> {
                error = null
                onSubmit(url)
            }
        }
    }

    OutlinedTextField(
        value = url,
        onValueChange = {
            url = it
            error = null
        },
        placeholder = { Text("https://example.com") },
        singleLine = true,
        isError = error != null,
        supportingText = error?.let { { Text(it) } },
        shape = RoundedCornerShape(16.dp),
        trailingIcon = {
            FilledIconButton(
                onClick = { handleSubmit() },
                shape = RoundedCornerShape(12.dp),
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    imageVector = Icons.Filled.Link,
                    contentDescription = "Go",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            cursorColor = MaterialTheme.colorScheme.onBackground
        ),
        modifier = modifier.fillMaxWidth().padding(horizontal = 24.dp)
    )
}

@Composable
@Preview
private fun ScreenshotConversionScreenPreview() {
    TrackShiftTheme {
        ScreenshotConversionScreenContent(
            onShowPaywall = {

            },
            onUrlSubmit = {

            }
        )
    }
}