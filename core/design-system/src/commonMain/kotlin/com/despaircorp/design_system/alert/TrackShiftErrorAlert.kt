package com.despaircorp.design_system.alert

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.despaircorp.design_system.theme.TrackShiftTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun TrackShiftErrorAlert(
    onDismissRequest: () -> Unit,
    errorMessage: String,
    mdofifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Erreur") },
        text = { Text(errorMessage) },
        confirmButton = {
            TextButton(onClick = onDismissRequest) {
                Text("OK")
            }
        },
        modifier = mdofifier
    )
}

@Composable
@Preview
private fun UrlGenerationErrorAlertPreview() {
    TrackShiftTheme {
        TrackShiftErrorAlert(
            onDismissRequest = {

            },
            errorMessage = "Une erreur est survenue"
        )
    }
}