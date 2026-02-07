package com.despaircorp.feature_onboarding.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
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
import com.despaircorp.design_system.theme.TrackShiftTheme
import com.despaircorp.feature_onboarding.screen.components.StreamingPlatform

@Composable
fun ConversionTutoScreen(
    onClick: () -> Unit ,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(Modifier.weight(1f))

        Text(
            "Le lien te permet de convertir les musiques qu'il contient vers un autre service de streaming",
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(20.dp)
        )

        Spacer(Modifier.height(40.dp))

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "https://trackshift.fr/code/3048f4",
                color = MaterialTheme.colorScheme.onBackground
            )
        }


        Spacer(Modifier.height(40.dp))

        Icon(
            imageVector = Icons.Default.ArrowDownward,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onBackground
        )

        Spacer(Modifier.height(40.dp))

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {


            Column(
                modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                StreamingPlatform(
                    name = "Spotify"
                )
                Spacer(Modifier.height(20.dp))
                StreamingPlatform(
                    name = "Soundcloud"
                )

                Spacer(Modifier.height(20.dp))
                StreamingPlatform(
                    name = "Youtube Music"
                )
            }

            Column(
                modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                StreamingPlatform(
                    name = "Apple Music"
                )
                Spacer(Modifier.height(20.dp))
                StreamingPlatform(
                    name = "Deezer"
                )

                Spacer(Modifier.height(20.dp))
                StreamingPlatform(
                    name = "Amazon Music"
                )
            }
        }

        Spacer(Modifier.weight(1f))
        Button(
            onClick = {
                onClick()
            }
        ) {
            Text(
                "Jure ?",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )
        }
        Spacer(Modifier.height(40.dp))


    }
}

@Preview
@Composable
private fun ConversionTutoScreenPreview(
) {
    TrackShiftTheme {
        ConversionTutoScreen(
            onClick = {

            }
        )
    }
}