package com.lowiq.jellyfish.domain.download

expect class DownloadServiceController {
    fun startService()
    fun stopService()
    fun isServiceRunning(): Boolean
}
