package com.lowiq.jellyfish.domain.cast

enum class CastDeviceType {
    CHROMECAST,
    AIRPLAY,
    DLNA
}

data class CastDevice(
    val id: String,
    val name: String,
    val type: CastDeviceType,
    val isConnected: Boolean = false
)
