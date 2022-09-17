package com.example.musify.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.musify.R
import com.example.musify.domain.SearchResult
import com.example.musify.ui.components.AsyncImageWithPlaceholder
import com.example.musify.ui.theme.dynamictheme.DynamicBackgroundType
import com.example.musify.ui.theme.dynamictheme.DynamicThemeResource
import com.example.musify.ui.theme.dynamictheme.DynamicallyThemedSurface
import com.google.accompanist.insets.statusBarsPadding

// TODO make artist and album name scrollable if they overflow
@Composable
fun NowPlayingScreen(
    currentlyPlayingTrack: SearchResult.TrackSearchResult,
    playbackDurationRange: ClosedFloatingPointRange<Float>,
    playbackProgressProvider: () -> Float,
    onCloseButtonClicked: () -> Unit,
    onShuffleButtonClicked: () -> Unit,
    onSkipPreviousButtonClicked: () -> Unit,
    onPlayButtonClicked: () -> Unit,
    onPauseButtonClicked: () -> Unit,
    onSkipNextButtonClicked: () -> Unit,
    onRepeatButtonClicked: () -> Unit
) {

    var isImageLoadingPlaceholderVisible by remember { mutableStateOf(true) }
    val dynamicThemeResource = remember(currentlyPlayingTrack) {
        DynamicThemeResource.FromImageUrl(currentlyPlayingTrack.imageUrlString)
    }
    var isPlayingIconVisible by remember { mutableStateOf(true) }
    DynamicallyThemedSurface(
        dynamicThemeResource = dynamicThemeResource,
        dynamicBackgroundType = DynamicBackgroundType.Filled()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(16.dp)
        ) {
            Header(
                modifier = Modifier.fillMaxWidth(),
                onCloseButtonClicked = onCloseButtonClicked,
                onTrailingButtonClick = {/*TODO*/ }
            )
            AsyncImageWithPlaceholder(
                modifier = Modifier
                    .size(500.dp)
                    .aspectRatio(1f),
                model = currentlyPlayingTrack.imageUrlString,
                contentDescription = null,
                onImageLoadingFinished = { isImageLoadingPlaceholderVisible = false },
                isLoadingPlaceholderVisible = isImageLoadingPlaceholderVisible,
                onImageLoading = { isImageLoadingPlaceholderVisible = true }
            )
            Text(
                text = currentlyPlayingTrack.name,
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = MaterialTheme.typography.h5
            )
            Text(
                text = currentlyPlayingTrack.artistsString,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography
                    .subtitle1
                    .copy(
                        color = MaterialTheme.colors
                            .onBackground
                            .copy(alpha = ContentAlpha.medium)
                    ),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            Spacer(modifier = Modifier.size(16.dp))
            Box(modifier = Modifier.fillMaxWidth()) {
                // The box acts as a recomposition scope.
                // Instead of recomposing the entire screen when the
                // playback progress changes, only the box recomposes
                // because the progress state is only read within the
                // scope of the box.
                Slider(
                    modifier = Modifier.fillMaxWidth(),
                    value = playbackProgressProvider(),
                    valueRange = playbackDurationRange,
                    colors = SliderDefaults.colors(
                        thumbColor = Color.White,
                        activeTrackColor = Color.White
                    ),
                    onValueChange = {}
                )
            }

            PlaybackControls(
                modifier = Modifier.fillMaxWidth(),
                isPlayIconVisible = isPlayingIconVisible,
                onSkipPreviousButtonClicked = onSkipPreviousButtonClicked,
                onPlayButtonClicked = {
                    onPlayButtonClicked()
                    // display pause button after play button is clicked
                    isPlayingIconVisible = false
                },
                onPauseButtonClicked = {
                    onPauseButtonClicked()
                    // display play button after pause button is clicked
                    isPlayingIconVisible = true
                },
                onSkipNextButtonClicked = onSkipNextButtonClicked,
                onRepeatButtonClicked = onRepeatButtonClicked,
                onShuffleButtonClicked = onShuffleButtonClicked
            )
            Footer(
                modifier = Modifier.fillMaxWidth(),
                onAvailableDevicesButtonClicked = { /*TODO*/ },
                onShareButtonClicked = { /*TODO*/ }
            )
        }
    }
}

@Composable
private fun Header(
    modifier: Modifier = Modifier,
    onCloseButtonClicked: () -> Unit,
    onTrailingButtonClick: () -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.clickable { onCloseButtonClicked() },
            painter = painterResource(R.drawable.ic_expand_more_24),
            contentDescription = null
        )
        Text(
            text = "Now playing",
            style = MaterialTheme.typography.subtitle1,
            fontWeight = FontWeight.SemiBold
        )
        Icon(
            modifier = Modifier.clickable { onTrailingButtonClick() },
            painter = painterResource(id = R.drawable.ic_more_horiz_24),
            contentDescription = null
        )

    }
}

@Composable
private fun Footer(
    modifier: Modifier = Modifier,
    onShareButtonClicked: () -> Unit,
    onAvailableDevicesButtonClicked: () -> Unit
) {

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onAvailableDevicesButtonClicked) {
            Icon(
                painter = painterResource(id = R.drawable.ic_available_devices),
                contentDescription = null
            )
        }
        IconButton(onClick = onShareButtonClicked) {
            Icon(
                imageVector = Icons.Filled.Share,
                contentDescription = null
            )
        }
    }
}

@Composable
private fun PlaybackControls(
    modifier: Modifier = Modifier,
    isPlayIconVisible: Boolean,
    onSkipPreviousButtonClicked: () -> Unit,
    onShuffleButtonClicked: () -> Unit,
    onPlayButtonClicked: () -> Unit,
    onPauseButtonClicked: () -> Unit,
    onSkipNextButtonClicked: () -> Unit,
    onRepeatButtonClicked: () -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onShuffleButtonClicked) {
            Icon(
                painter = painterResource(R.drawable.ic_round_shuffle_24),
                contentDescription = null
            )
        }
        IconButton(onClick = onSkipPreviousButtonClicked) {
            Icon(
                modifier = Modifier.size(40.dp),
                painter = painterResource(R.drawable.ic_skip_previous_24),
                contentDescription = null
            )
        }
        IconButton(onClick = if (isPlayIconVisible) onPlayButtonClicked else onPauseButtonClicked) {
            Icon(
                modifier = Modifier.size(72.dp),
                painter = if (isPlayIconVisible) painterResource(R.drawable.ic_play_circle_filled_24)
                else painterResource(R.drawable.ic_pause_circle_filled_24),
                contentDescription = null
            )
        }
        IconButton(onClick = onSkipNextButtonClicked) {
            Icon(
                modifier = Modifier.size(40.dp),
                painter = painterResource(R.drawable.ic_skip_next_24),
                contentDescription = null
            )
        }
        IconButton(onClick = onRepeatButtonClicked) {
            Icon(
                painter = painterResource(R.drawable.ic_round_repeat_24),
                contentDescription = null
            )
        }
    }
}