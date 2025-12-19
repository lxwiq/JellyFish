package com.lowiq.jellyfish.presentation.screens.downloads

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.AsyncImage
import com.lowiq.jellyfish.domain.model.Download
import com.lowiq.jellyfish.domain.model.DownloadStatus
import com.lowiq.jellyfish.presentation.screens.player.VideoPlayerScreen
import com.lowiq.jellyfish.presentation.theme.LocalJellyFishColors

class DownloadsScreen : Screen {

    @Composable
    override fun Content() {
        val colors = LocalJellyFishColors.current
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = koinScreenModel<DownloadsScreenModel>()
        val state by screenModel.state.collectAsState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.background)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { navigator.pop() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                    Text(
                        "Téléchargements",
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
                state.storageInfo?.let { info ->
                    Text(
                        "Espace: ${formatBytes(info.usedBytes)}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.mutedForeground
                    )
                }
            }

            if (state.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (state.activeDownloads.isNotEmpty()) {
                        item {
                            Text(
                                "EN COURS (${state.activeDownloads.size})",
                                style = MaterialTheme.typography.titleSmall,
                                color = colors.mutedForeground
                            )
                        }
                        items(state.activeDownloads, key = { it.id }) { download ->
                            ActiveDownloadItem(
                                download = download,
                                onPause = { screenModel.pauseDownload(download.id) },
                                onResume = { screenModel.resumeDownload(download.id) },
                                onCancel = { screenModel.cancelDownload(download.id) }
                            )
                        }
                    }

                    if (state.completedDownloads.isNotEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "TÉLÉCHARGÉS (${state.completedDownloads.size})",
                                style = MaterialTheme.typography.titleSmall,
                                color = colors.mutedForeground
                            )
                        }
                        items(state.completedDownloads, key = { it.id }) { download ->
                            CompletedDownloadItem(
                                download = download,
                                onPlay = {
                                    download.filePath?.let { path ->
                                        navigator.push(
                                            VideoPlayerScreen(
                                                itemId = download.itemId,
                                                title = download.title,
                                                subtitle = download.subtitle,
                                                startPositionMs = download.lastPlayedPositionMs,
                                                offlineFilePath = path,
                                                downloadId = download.id
                                            )
                                        )
                                    }
                                },
                                onDelete = { screenModel.deleteDownload(download.id) }
                            )
                        }
                    }

                    if (state.activeDownloads.isEmpty() && state.completedDownloads.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        Icons.Default.Download,
                                        contentDescription = null,
                                        modifier = Modifier.size(64.dp),
                                        tint = colors.mutedForeground
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        "Aucun téléchargement",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = colors.mutedForeground
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ActiveDownloadItem(
    download: Download,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onCancel: () -> Unit
) {
    val colors = LocalJellyFishColors.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = colors.card)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = download.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp, 90.dp)
                    .clip(RoundedCornerShape(4.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    download.title,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                download.subtitle?.let {
                    Text(
                        it,
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.mutedForeground
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "${download.quality} • ${formatBytes(download.totalBytes)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.mutedForeground
                )
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { download.progress },
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    "${(download.progress * 100).toInt()}%",
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.mutedForeground
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column {
                IconButton(
                    onClick = if (download.status == DownloadStatus.PAUSED) onResume else onPause
                ) {
                    Icon(
                        if (download.status == DownloadStatus.PAUSED) Icons.Default.PlayArrow else Icons.Default.Pause,
                        contentDescription = null
                    )
                }
                IconButton(onClick = onCancel) {
                    Icon(Icons.Default.Close, contentDescription = "Cancel")
                }
            }
        }
    }
}

@Composable
private fun CompletedDownloadItem(
    download: Download,
    onPlay: () -> Unit,
    onDelete: () -> Unit
) {
    val colors = LocalJellyFishColors.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = colors.card)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = download.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp, 90.dp)
                    .clip(RoundedCornerShape(4.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    download.title,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                download.subtitle?.let {
                    Text(
                        it,
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.mutedForeground
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "${download.quality} • ${formatBytes(download.downloadedBytes)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.mutedForeground
                )
            }

            IconButton(onClick = onPlay) {
                Icon(Icons.Default.PlayArrow, contentDescription = "Play")
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = colors.destructive)
            }
        }
    }
}

private fun formatBytes(bytes: Long): String {
    return when {
        bytes >= 1_000_000_000 -> {
            val gb = bytes / 1_000_000_000.0
            "${(gb * 10).toLong() / 10.0} Go"
        }
        bytes >= 1_000_000 -> "${bytes / 1_000_000} Mo"
        else -> "${bytes / 1_000} Ko"
    }
}
