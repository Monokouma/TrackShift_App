package com.despaircorp.feature_onboarding.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.despaircorp.design_system.resources.Res
import com.despaircorp.design_system.resources.tuto_screen
import com.despaircorp.design_system.theme.TrackShiftTheme
import org.jetbrains.compose.resources.painterResource

@Composable
fun ScreenshotTutoScreen(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Text(
            "Comment ça marche ?",
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.displaySmall,
            textAlign = TextAlign.Center
            )

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            "Tu peux utiliser un screenshot : ",
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(20.dp))
        //Screenshot

        Image(
            painter = painterResource(Res.drawable.tuto_screen),
            contentDescription = "Screenshot",
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
        )

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            "Où, l'url publique d'une playlist : ",
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(20.dp))
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "https://une.gentille.app.de.streaming.com/playlist/1234",
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { 
                onClick()
            }
        ) {
            Text(
                "Et ensuite ?",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
@Preview
private fun ScreenshotTutoScreenPreview() {
    TrackShiftTheme {
        ScreenshotTutoScreen(
            onClick = {

            }
        )
    }
}