package com.despaircorp.feature_link_generation.screen.components.screenshot

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.despaircorp.design_system.theme.TrackShiftTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.ui.tooling.preview.Preview

private val SCREENSHOT_HEIGHT = 220.dp
private val SCREENSHOT_ASPECT_RATIO = 9f / 19.5f

@Composable
fun ScreenshotRow(
    images: ImmutableList<ByteArray>,
    maxImages: Int,
    onAddClick: () -> Unit,
    onReplaceClick: (Int) -> Unit,
    onRemoveClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 24.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        itemsIndexed(images.toList()) { index, imageBytes ->
            Box(modifier = Modifier.padding(horizontal = 6.dp)) {
                ScreenshotFrame(
                    imageBytes = imageBytes,
                    onClick = { onReplaceClick(index) },
                    onRemove = { onRemoveClick(index) }
                )
            }
        }

        if (images.size < maxImages) {
            item {
                Box(modifier = Modifier.padding(horizontal = 6.dp)) {
                    AddScreenshotFrame(onClick = onAddClick)
                }
            }
        }
    }
}

@Composable
private fun ScreenshotFrame(
    imageBytes: ByteArray,
    onClick: () -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(SCREENSHOT_HEIGHT)
            .aspectRatio(SCREENSHOT_ASPECT_RATIO)
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
    ) {
        AsyncImage(
            model = imageBytes,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
                .size(28.dp)
                .clip(CircleShape)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.error.copy(alpha = 0.5f),
                    shape = CircleShape
                )
                .clickable { onRemove() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = "Remove",
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(14.dp)
            )
        }
    }
}

@Composable
private fun AddScreenshotFrame(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(SCREENSHOT_HEIGHT)
            .aspectRatio(SCREENSHOT_ASPECT_RATIO)
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f),
                shape = RoundedCornerShape(16.dp)
            )
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = "Add screenshot",
            tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
            modifier = Modifier.size(32.dp)
        )
    }
}

@Composable
@Preview
private fun ScreenshotRowEmptyPreview() {
    TrackShiftTheme {
        ScreenshotRow(
            images = persistentListOf(),
            maxImages = 3,
            onAddClick = {},
            onReplaceClick = {},
            onRemoveClick = {}
        )
    }
}

@Composable
@Preview
private fun AddScreenshotFramePreview() {
    TrackShiftTheme {
        AddScreenshotFrame(onClick = {})
    }
}
