package com.lowiq.jellyfish.domain.cast

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

actual class CastManager {
    private val _availableDevices = MutableStateFlow<List<CastDevice>>(emptyList())
    actual val availableDevices: StateFlow<List<CastDevice>> = _availableDevices

    private val _castState = MutableStateFlow<CastState>(CastState.Disconnected)
    actual val castState: StateFlow<CastState> = _castState

    actual val controller: CastController? = null

    actual fun startDiscovery() {}
    actual fun stopDiscovery() {}

    actual suspend fun connect(device: CastDevice): Result<Unit> =
        Result.failure(NotImplementedError("Cast not supported on iOS yet"))

    actual suspend fun disconnect() {}

    actual suspend fun loadMedia(mediaInfo: CastMediaInfo, startPositionMs: Long): Result<Unit> =
        Result.failure(NotImplementedError("Cast not supported on iOS yet"))

    actual fun addToQueue(mediaInfo: CastMediaInfo) {}
    actual fun getQueue(): List<CastMediaInfo> = emptyList()
    actual fun clearQueue() {}
    actual fun playNext() {}
    actual fun playPrevious() {}
}
