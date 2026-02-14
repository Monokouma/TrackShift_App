package com.despaircorp.feature_profile.screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.despaircorp.design_system.theme.TrackShiftTheme

@Composable
fun StatsCard(
    totalLinksCreated: Int,
    totalLinksConverted: Int,
    totalPlaylistsCreated: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        StatItem(
            value = totalLinksCreated,
            label = "Liens crées",
            modifier = Modifier.weight(1f)
        )

        VerticalDivider(
            modifier = Modifier.height(40.dp),
            color = MaterialTheme.colorScheme.outlineVariant
        )

        StatItem(
            value = totalLinksConverted,
            label = "Liens convertis",
            modifier = Modifier.weight(1f)
        )

        VerticalDivider(
            modifier = Modifier.height(40.dp),
            color = MaterialTheme.colorScheme.outlineVariant
        )

        StatItem(
            value = totalPlaylistsCreated,
            label = "Playlists crées",
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun StatItem(
    value: Int,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value.toString(),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
@Preview
private fun StatsCardPreview() {
    TrackShiftTheme {
        StatsCard(
            totalLinksCreated = 42,
            totalLinksConverted = 28,
            totalPlaylistsCreated = 5,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
@Preview
private fun StatItemPreview() {
    TrackShiftTheme {
        StatItem(
            value = 42,
            label = "Liens crées"
        )
    }
}
