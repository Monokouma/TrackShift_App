package com.despaircorp.feature_link_generation.screen.components.screenshot

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.despaircorp.design_system.theme.TrackShiftTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ScreenshotCountBadge(
    count: Int,
    max: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerHigh),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "$count / $max",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

@Composable
@Preview
private fun ScreenshotCountBadgeEmptyPreview() {
    TrackShiftTheme {
        ScreenshotCountBadge(count = 0, max = 3)
    }
}

@Composable
@Preview
private fun ScreenshotCountBadgeFilledPreview() {
    TrackShiftTheme {
        ScreenshotCountBadge(count = 2, max = 5)
    }
}
