package com.despaircorp.feature_profile.screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.despaircorp.design_system.theme.TrackShiftTheme

@Composable
fun MonthlyUsageCard(
    linksCreatedMonth: Int,
    linksConvertedMonth: Int,
    isPro: Boolean,
    onUpgradeClick: () -> Unit,
    modifier: Modifier = Modifier,
    maxLinksCreated: Int = 10,
    maxLinksConverted: Int = 10
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp)
    ) {
        Text(
            text = "Utilisation mensuelle",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(Modifier.height(16.dp))

        UsageProgressBar(
            label = "Liens créés",
            current = linksCreatedMonth,
            max = maxLinksCreated,
            isPro = isPro
        )

        Spacer(Modifier.height(12.dp))

        UsageProgressBar(
            label = "Liens convertis",
            current = linksConvertedMonth,
            max = maxLinksConverted,
            isPro = isPro
        )

        Spacer(Modifier.height(16.dp))

        if (!isPro) {
            Button(
                onClick = onUpgradeClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = "Passer à Pro pour convertir plus",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun UsageProgressBar(
    label: String,
    current: Int,
    max: Int,
    isPro: Boolean,
    modifier: Modifier = Modifier
) {
    val progress = (current.toFloat() / max).coerceIn(0f, 1f)

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = if (isPro) "∞" else "$current / $max",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Spacer(Modifier.height(6.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .background(
                    color = MaterialTheme.colorScheme.outlineVariant,
                    shape = RoundedCornerShape(4.dp)
                )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(if (isPro) 100f else progress)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.tertiary,
                                MaterialTheme.colorScheme.primary
                            )
                        ),
                        shape = RoundedCornerShape(4.dp)
                    )
            )
        }
    }
}

@Composable
@Preview
private fun MonthlyUsageCardPreview() {
    TrackShiftTheme {
        MonthlyUsageCard(
            linksCreatedMonth = 7,
            linksConvertedMonth = 3,
            onUpgradeClick = {},
            modifier = Modifier.padding(16.dp),
            isPro = false
        )
    }
}

@Composable
@Preview
private fun UsageProgressBarPreview() {
    TrackShiftTheme {
        UsageProgressBar(
            label = "Liens créés",
            current = 7,
            max = 10,
            modifier = Modifier.padding(16.dp),
            isPro = true
        )
    }
}

@Composable
@Preview
private fun UsageProgressBarFullPreview() {
    TrackShiftTheme {
        UsageProgressBar(
            label = "Liens convertis",
            current = 10,
            max = 10,
            modifier = Modifier.padding(16.dp),
            isPro = false
        )
    }
}
