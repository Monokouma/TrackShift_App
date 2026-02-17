package com.despaircorp.feature_link_generation.screen.components.input

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Link
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp

@Composable
fun UrlInput(
    onSubmit: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var url by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    val focusManager = LocalFocusManager.current

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
                url = ""
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
                onClick = {
                    handleSubmit()
                    focusManager.clearFocus()
                },
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