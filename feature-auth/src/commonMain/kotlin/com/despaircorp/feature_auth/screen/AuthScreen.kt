package com.despaircorp.feature_auth.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.despaircorp.design_system.theme.TrackShiftTheme
import com.despaircorp.feature_auth.model.AuthProvider
import com.despaircorp.feature_auth.view_model.AuthViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Suppress("ParamsComparedByRef")
@Composable
fun AuthScreen(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = koinViewModel()
) {

    AuthScreenContent(
        onAuthProviderPick = {
            viewModel.onProviderPick(provider = it)
        },
        onMailAuth = { mail, pass, provider ->

        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AuthScreenContent(
    onAuthProviderPick: (AuthProvider) -> Unit,
    onMailAuth: (String, String, AuthProvider) -> Unit,
    modifier: Modifier = Modifier
) {
    var showSheet by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Text("Salut toi !",
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text("On se connait ?",
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(80.dp))


        AuthButton(
            onClick = {
                onAuthProviderPick(it)
            }
        )

        Spacer(modifier = Modifier.height(40.dp))

        MoreAuthOptions(
            onClick = {
                showSheet = true
            }
        )

        Spacer(modifier = Modifier.weight(1f))

        PoliticsAndCgu()

        if (showSheet) {
            MailAuth(
                onDismissRequest = {
                    showSheet = false
                },
                onSubmit = { mail, pass, provider ->
                    onMailAuth(mail, pass, provider)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MailAuth(
    onSubmit: (String, String, AuthProvider) -> Unit,
    onDismissRequest: () -> Unit
) {

    ModalBottomSheet(
        onDismissRequest = { onDismissRequest() },
        sheetState = rememberModalBottomSheetState(),
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        containerColor = MaterialTheme.colorScheme.background,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(top = 12.dp)
                    .width(36.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.4.dp))
                    .background(Color.Gray.copy(alpha = 0.4f))
            )
        },

    ) {
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var passwordVisible by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
        ) {
            Text(
                text = "Se connecter",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next,
                ),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                    focusedBorderColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
                    focusedLabelColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onBackground,
                    cursorColor = MaterialTheme.colorScheme.onBackground,
                ),
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Mot de passe") },
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done,
                ),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onBackground,
                        )
                    }
                },
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                    focusedBorderColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
                    focusedLabelColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onBackground,
                    cursorColor = MaterialTheme.colorScheme.onBackground,
                ),
                modifier = Modifier.fillMaxWidth(),
            )


            Spacer(modifier = Modifier.height(24.dp))

            AuthButton(
                text = "Se connecter",
                onClick = {
                    onSubmit(email, password, AuthProvider.MAIL)
                },
                enabled = email.isNotBlank() && password.isNotBlank(),
                backgroundColor = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun AuthButton(
    onClick: (AuthProvider) -> Unit,
    modifier: Modifier = Modifier
) {
    Column {
        AuthButton(
            text = "Se connecter avec Google",
            onClick = { onClick(AuthProvider.GOOGLE) },
            backgroundColor = Color(0xFFFFFFFF),
            textColor = Color(0xFF1F1F1F),
            modifier = Modifier.padding(horizontal = 40.dp).padding(vertical = 8.dp)
        )

        AuthButton(
            text = "Se connecter avec Apple",
            onClick = { onClick(AuthProvider.APPLE) },
            backgroundColor = Color(0xFF000000),
            textColor = Color(0xFFFFFFFF),
            modifier = Modifier.padding(horizontal = 40.dp).padding(vertical = 8.dp),
            borderColor = Color.White
        )

        AuthButton(
            text = "Se connecter avec Discord",
            onClick = { onClick(AuthProvider.DISCORD) },
            backgroundColor = Color(0xFF5865F2),
            textColor = Color(0xFFFFFFFF),
            modifier = Modifier.padding(horizontal = 40.dp).padding(vertical = 8.dp)
        )
    }
}

@Composable
private fun MoreAuthOptions(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {

        HorizontalDivider(
            modifier = Modifier.weight(1f).padding(start = 20.dp),
            color = Color.White.copy(alpha = 0.4f),
        )

        Text(
            text = "Utiliser une autre méthode de connexion",
            modifier = Modifier.clickable(enabled = true, onClick = onClick).padding(horizontal = 12.dp),
            color = Color.White,
            fontSize = 16.sp,
        )

        HorizontalDivider(
            modifier = Modifier.weight(1f).padding(end = 20.dp),
            color = Color.White.copy(alpha = 0.4f),
        )
    }
}

@Composable
private fun PoliticsAndCgu(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = "Politique de Confidentialité",
            modifier = Modifier.padding(20.dp),
            color = Color.White,
            style = MaterialTheme.typography.titleSmall,
            textDecoration = TextDecoration.Underline
        )

        Text(
            text = "Conditions Générales d'Utilisation",
            modifier = Modifier.padding(20.dp),
            color = Color.White,
            style = MaterialTheme.typography.titleSmall,
            textDecoration = TextDecoration.Underline
        )
    }
}

@Composable
fun AuthButton(
    text: String,
    onClick: () -> Unit,
    backgroundColor: Color,
    modifier: Modifier = Modifier,
    textColor: Color = MaterialTheme.colorScheme.onBackground,
    enabled: Boolean = true,
    borderColor: Color = Color.Transparent
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(26.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            disabledContainerColor = backgroundColor.copy(alpha = 0.4f),
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 20.dp,
            pressedElevation = 20.dp,
        ),
        border = BorderStroke(
            width = 1.dp,
            color = borderColor
        )
    ) {
        Text(
            text = text,
            color = if (enabled) textColor else textColor.copy(alpha = 0.4f),
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
        )
    }
}


@Composable
@Preview
private fun AuthScreenPreview() {
    TrackShiftTheme {
        AuthScreenContent(
            onAuthProviderPick = {

            },
            onMailAuth = { mail, pass, provider ->

            }
        )
    }
}

