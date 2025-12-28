package com.lowiq.jellyfish.presentation.screens.player

import com.lowiq.jellyfish.domain.player.AudioTrack
import com.lowiq.jellyfish.domain.player.PlaybackState
import com.lowiq.jellyfish.domain.player.QualityOption
import com.lowiq.jellyfish.domain.player.SubtitleTrack
import com.lowiq.jellyfish.domain.player.VideoScaleMode

data class VideoPlayerState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val title: String = "",
    val subtitle: String? = null, // e.g., "S01E03 - Episode Title"
    val playbackState: PlaybackState = PlaybackState.Idle,
    val audioTracks: List<AudioTrack> = emptyList(),
    val subtitleTracks: List<SubtitleTrack> = emptyList(),
    val qualityOptions: List<QualityOption> = emptyList(),
    val playbackSpeed: Float = 1f,
    val scaleMode: VideoScaleMode = VideoScaleMode.FIT,
    val controlsVisible: Boolean = true,
    val showTrackSelector: Boolean = false,
    val showSettingsSheet: Boolean = false,
    val showResumeDialog: Boolean = false,
    val resumePositionMs: Long = 0,
    val nextEpisode: NextEpisodeInfo? = null,
    val showNextEpisodeCard: Boolean = false,
    val isOfflineMode: Boolean = false
)

data class NextEpisodeInfo(
    val id: String,
    val title: String,
    val seasonNumber: Int,
    val episodeNumber: Int,
    val thumbnailUrl: String?
)
