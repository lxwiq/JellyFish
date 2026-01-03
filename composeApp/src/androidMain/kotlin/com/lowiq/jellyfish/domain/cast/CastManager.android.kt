package com.lowiq.jellyfish.domain.cast

import android.content.Context
import android.net.Uri
import com.google.android.gms.cast.MediaInfo
import com.google.android.gms.cast.MediaLoadRequestData
import com.google.android.gms.cast.MediaMetadata
import com.google.android.gms.cast.MediaTrack
import com.google.android.gms.cast.framework.CastContext
import com.google.android.gms.cast.framework.CastSession
import com.google.android.gms.cast.framework.SessionManager
import com.google.android.gms.cast.framework.SessionManagerListener
import com.google.android.gms.cast.framework.media.RemoteMediaClient
import com.google.android.gms.common.images.WebImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

actual class CastManager(
    private val context: Context
) {
    private val scope = CoroutineScope(Dispatchers.Main)
    private var statusUpdateJob: Job? = null

    private val castContext: CastContext by lazy {
        CastContext.getSharedInstance(context)
    }

    private val sessionManager: SessionManager
        get() = castContext.sessionManager

    private val _availableDevices = MutableStateFlow<List<CastDevice>>(emptyList())
    actual val availableDevices: StateFlow<List<CastDevice>> = _availableDevices

    private val _castState = MutableStateFlow<CastState>(CastState.Disconnected)
    actual val castState: StateFlow<CastState> = _castState

    private var chromecastController: ChromecastController? = null
    actual val controller: CastController?
        get() = chromecastController

    private val queue = mutableListOf<CastMediaInfo>()
    private var currentQueueIndex = -1

    private val sessionManagerListener = object : SessionManagerListener<CastSession> {
        override fun onSessionStarting(session: CastSession) {
            _castState.value = CastState.Connecting
        }

        override fun onSessionStarted(session: CastSession, sessionId: String) {
            session.remoteMediaClient?.let { client ->
                chromecastController = ChromecastController(client)
                startStatusUpdates(client)
            }
            updateConnectedState(session)
        }

        override fun onSessionStartFailed(session: CastSession, error: Int) {
            chromecastController = null
            _castState.value = CastState.Error("Failed to connect: error $error")
        }

        override fun onSessionEnding(session: CastSession) {}

        override fun onSessionEnded(session: CastSession, error: Int) {
            stopStatusUpdates()
            chromecastController = null
            _castState.value = CastState.Disconnected
        }

        override fun onSessionResuming(session: CastSession, sessionId: String) {
            _castState.value = CastState.Connecting
        }

        override fun onSessionResumed(session: CastSession, wasSuspended: Boolean) {
            session.remoteMediaClient?.let { client ->
                chromecastController = ChromecastController(client)
                startStatusUpdates(client)
            }
            updateConnectedState(session)
        }

        override fun onSessionResumeFailed(session: CastSession, error: Int) {
            chromecastController = null
            _castState.value = CastState.Error("Failed to resume: error $error")
        }

        override fun onSessionSuspended(session: CastSession, reason: Int) {
            stopStatusUpdates()
        }
    }

    private fun updateConnectedState(session: CastSession) {
        val device = CastDevice(
            id = session.castDevice?.deviceId ?: "",
            name = session.castDevice?.friendlyName ?: "Chromecast",
            type = CastDeviceType.CHROMECAST,
            isConnected = true
        )
        _castState.value = CastState.Connected(device = device)
    }

    private fun startStatusUpdates(client: RemoteMediaClient) {
        statusUpdateJob?.cancel()
        statusUpdateJob = scope.launch {
            while (isActive) {
                updatePlaybackState(client)
                delay(1000)
            }
        }
    }

    private fun stopStatusUpdates() {
        statusUpdateJob?.cancel()
        statusUpdateJob = null
    }

    private fun updatePlaybackState(client: RemoteMediaClient) {
        val status = client.mediaStatus ?: return
        val currentState = _castState.value
        if (currentState !is CastState.Connected) return

        val volume = sessionManager.currentCastSession?.volume?.toFloat() ?: 1f

        val playbackState = CastPlaybackState(
            isPlaying = status.playerState == com.google.android.gms.cast.MediaStatus.PLAYER_STATE_PLAYING,
            positionMs = client.approximateStreamPosition,
            durationMs = status.mediaInfo?.streamDuration ?: 0,
            volume = volume,
            playbackSpeed = status.playbackRate.toFloat(),
            isBuffering = status.playerState == com.google.android.gms.cast.MediaStatus.PLAYER_STATE_BUFFERING
        )

        _castState.value = currentState.copy(playbackState = playbackState)
    }

    actual fun startDiscovery() {
        sessionManager.addSessionManagerListener(sessionManagerListener, CastSession::class.java)
    }

    actual fun stopDiscovery() {
        sessionManager.removeSessionManagerListener(sessionManagerListener, CastSession::class.java)
    }

    actual suspend fun connect(device: CastDevice): Result<Unit> {
        return Result.success(Unit)
    }

    actual suspend fun disconnect() {
        sessionManager.endCurrentSession(true)
    }

    actual suspend fun loadMedia(mediaInfo: CastMediaInfo, startPositionMs: Long): Result<Unit> {
        val session = sessionManager.currentCastSession
            ?: return Result.failure(Exception("No active cast session"))

        val client = session.remoteMediaClient
            ?: return Result.failure(Exception("No remote media client"))

        return suspendCancellableCoroutine { continuation ->
            val metadata = MediaMetadata(
                if (mediaInfo.mediaType == CastMediaType.VIDEO)
                    MediaMetadata.MEDIA_TYPE_MOVIE
                else
                    MediaMetadata.MEDIA_TYPE_MUSIC_TRACK
            ).apply {
                putString(MediaMetadata.KEY_TITLE, mediaInfo.title)
                mediaInfo.subtitle?.let { putString(MediaMetadata.KEY_SUBTITLE, it) }
                mediaInfo.imageUrl?.let { addImage(WebImage(Uri.parse(it))) }
            }

            val tracks = mutableListOf<MediaTrack>()

            mediaInfo.subtitleTracks.forEachIndexed { index, track ->
                tracks.add(
                    MediaTrack.Builder(index.toLong(), MediaTrack.TYPE_TEXT)
                        .setName(track.name)
                        .setSubtype(MediaTrack.SUBTYPE_SUBTITLES)
                        .setContentId(track.url)
                        .setLanguage(track.language ?: "und")
                        .build()
                )
            }

            val castMediaInfo = MediaInfo.Builder(mediaInfo.streamUrl)
                .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
                .setContentType("video/mp4")
                .setMetadata(metadata)
                .setMediaTracks(tracks)
                .build()

            val loadRequest = MediaLoadRequestData.Builder()
                .setMediaInfo(castMediaInfo)
                .setAutoplay(true)
                .setCurrentTime(startPositionMs)
                .build()

            client.load(loadRequest).addStatusListener {
                if (continuation.isActive) {
                    continuation.resume(Result.success(Unit))
                }
            }

            val currentState = _castState.value
            if (currentState is CastState.Connected) {
                _castState.value = currentState.copy(mediaInfo = mediaInfo)
            }
        }
    }

    actual fun addToQueue(mediaInfo: CastMediaInfo) {
        queue.add(mediaInfo)
    }

    actual fun getQueue(): List<CastMediaInfo> = queue.toList()

    actual fun clearQueue() {
        queue.clear()
        currentQueueIndex = -1
    }

    actual fun playNext() {
        if (currentQueueIndex < queue.size - 1) {
            currentQueueIndex++
            scope.launch {
                loadMedia(queue[currentQueueIndex])
            }
        }
    }

    actual fun playPrevious() {
        if (currentQueueIndex > 0) {
            currentQueueIndex--
            scope.launch {
                loadMedia(queue[currentQueueIndex])
            }
        }
    }
}
