package com.despaircorp.feature_link_generation.screen.components.alert

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.despaircorp.design_system.theme.TrackShiftTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UrlGenerationSuccessAlert(
    onDismissRequest: () -> Unit,
    trackShiftUrl: String,
    onCopyUrl: () -> Unit,
    modifier: Modifier = Modifier
) {

    BasicAlertDialog(
        onDismissRequest,
        modifier,
        DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = true
        )
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.background,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)),
            modifier = Modifier
        ) {
            Column(
                modifier = Modifier.background(MaterialTheme.colorScheme.background),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Row {
                    Text(
                        "Lien généré !",
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.headlineLarge,
                        modifier = Modifier.padding(20.dp)
                    )

                    Spacer(Modifier.weight(1f))
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .padding(vertical = 8.dp)
                        .padding(horizontal = 20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        trackShiftUrl,
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth().padding(20.dp),
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Button(
                        onClick = {
                            onDismissRequest()
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                        ),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                    ) {
                        Text("Annuler", color = MaterialTheme.colorScheme.onBackground, style = MaterialTheme.typography.titleMedium)
                    }

                    Button(
                        onClick = {
                            onCopyUrl()
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                    ) {
                        Text("Copier le lien", color = MaterialTheme.colorScheme.onBackground, style = MaterialTheme.typography.titleMedium)
                    }
                }

            }
        }

    }
}

@Composable
@Preview
private fun UrlGenerationSuccessAlertPreview() {
    TrackShiftTheme {
        UrlGenerationSuccessAlert(
            onDismissRequest = {

            },
            onCopyUrl = {

            },
            trackShiftUrl = "https://www.trackshift.fr/code/123456"
        )
    }
}