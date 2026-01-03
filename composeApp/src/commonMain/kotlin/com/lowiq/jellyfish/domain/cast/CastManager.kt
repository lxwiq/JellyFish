package com.lowiq.jellyfish.domain.cast

import kotlinx.coroutines.flow.StateFlow

expect class CastManager {
    val availableDevices: StateFlow<List<CastDevice>>
    val castState: StateFlow<CastState>
    val controller: CastController?

    fun startDiscovery()
    fun stopDiscovery()

    suspend fun connect(device: CastDevice): Result<Unit>
    suspend fun disconnect()

    suspend fun loadMedia(
        mediaInfo: CastMediaInfo,
        startPositionMs: Long = 0
    ): Result<Unit>

    fun addToQueue(mediaInfo: CastMediaInfo)
    fun getQueue(): List<CastMediaInfo>
    fun clearQueue()
    fun playNext()
    fun playPrevious()
}
