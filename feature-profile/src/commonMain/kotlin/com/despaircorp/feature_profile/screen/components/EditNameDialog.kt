package com.despaircorp.feature_profile.screen.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.despaircorp.design_system.theme.TrackShiftTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun EditNameDialog(
    currentName: String,
    onNameChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.border(
            width = 1.dp,
            color = MaterialTheme.colorScheme.primaryContainer,
            shape = RoundedCornerShape(16.dp)
        ),
        title = {
            Text(
                text = "Modifier le nom",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        text = {
            OutlinedTextField(
                value = currentName,
                onValueChange = onNameChange,
                singleLine = true,
                placeholder = {
                    Text("Nouveau nom")
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primaryContainer,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                    cursorColor = MaterialTheme.colorScheme.onBackground
                ),
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                enabled = currentName.isNotBlank(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Confirmer", color = MaterialTheme.colorScheme.onBackground)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Annuler", color = MaterialTheme.colorScheme.onBackground)
            }
        }
    )
}

@Composable
@Preview
private fun EditNameDialogPreview() {
    TrackShiftTheme {
        var name by remember { mutableStateOf("Monokouma") }
        EditNameDialog(
            currentName = name,
            onNameChange = { name = it },
            onConfirm = {},
            onDismiss = {}
        )
    }
}

@Composable
@Preview
private fun EditNameDialogEmptyPreview() {
    TrackShiftTheme {
        var name by remember { mutableStateOf("") }
        EditNameDialog(
            currentName = name,
            onNameChange = { name = it },
            onConfirm = {},
            onDismiss = {}
        )
    }
}
