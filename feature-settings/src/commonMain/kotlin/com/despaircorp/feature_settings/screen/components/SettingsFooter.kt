package com.despaircorp.feature_settings.screen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.despaircorp.design_system.theme.TrackShiftTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SettingsFooter(
    versionName: String,
    onOpenCgu: () -> Unit,
    onOpenPrivacy: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Version : $versionName",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.labelLarge
        )

        Spacer(Modifier.height(8.dp))

        Box(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
        ) {
            Text(
                "Conditions Générales\nd'Utilisation",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.CenterStart)
                    .clickable(true, onClick = onOpenCgu),
                textDecoration = TextDecoration.Underline,
                textAlign = TextAlign.Center
            )

            Text(
                "Politique de\nConfidentialité",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.CenterEnd)
                    .clickable(true, onClick = onOpenPrivacy),
                textDecoration = TextDecoration.Underline,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
@Preview
private fun SettingsFooterPreview() {
    TrackShiftTheme {
        SettingsFooter(
            versionName = "1.0.0",
            onOpenCgu = {},
            onOpenPrivacy = {}
        )
    }
}
