package com.lowiq.jellyfish.data.remote

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

data class DownloadProgress(
    val bytesDownloaded: Long,
    val totalBytes: Long,
    val progress: Float
)

sealed class DownloadResult {
    data class Progress(val progress: DownloadProgress) : DownloadResult()
    data class Success(val filePath: String) : DownloadResult()
    data class Error(val message: String) : DownloadResult()
}

class DownloadClient(private val httpClient: HttpClient) {

    fun downloadFile(
        url: String,
        destinationPath: String,
        token: String? = null,
        onWrite: suspend (ByteArray) -> Unit
    ): Flow<DownloadResult> = flow {
        try {
            println("[DownloadClient] Starting download: $url")
            httpClient.prepareGet(url) {
                // Long timeout for transcoding - server may take time to prepare stream
                timeout {
                    requestTimeoutMillis = DOWNLOAD_TIMEOUT_MS
                    connectTimeoutMillis = CONNECT_TIMEOUT_MS
                    socketTimeoutMillis = SOCKET_TIMEOUT_MS
                }
                // Add authentication header if token provided
                token?.let {
                    header("X-MediaBrowser-Token", it)
                }
            }.execute { response ->
                println("[DownloadClient] Response status: ${response.status}")
                println("[DownloadClient] Content-Length: ${response.contentLength()}")
                println("[DownloadClient] Content-Type: ${response.contentType()}")

                if (!response.status.isSuccess()) {
                    emit(DownloadResult.Error("HTTP ${response.status.value}: ${response.status.description}"))
                    return@execute
                }

                val contentLength = response.contentLength() ?: 0L
                if (contentLength < 1_000_000) {
                    // Less than 1MB - probably an error page, not a video
                    println("[DownloadClient] WARNING: Content-Length is suspiciously small: $contentLength bytes")
                }
                var bytesDownloaded = 0L
                val channel = response.bodyAsChannel()

                while (!channel.isClosedForRead) {
                    val packet = channel.readRemaining(DEFAULT_BUFFER_SIZE.toLong())
                    while (!packet.isEmpty) {
                        val bytes = packet.readBytes()
                        onWrite(bytes)
                        bytesDownloaded += bytes.size

                        val progress = if (contentLength > 0) {
                            bytesDownloaded.toFloat() / contentLength.toFloat()
                        } else 0f

                        emit(DownloadResult.Progress(
                            DownloadProgress(
                                bytesDownloaded = bytesDownloaded,
                                totalBytes = contentLength,
                                progress = progress
                            )
                        ))
                    }
                }

                emit(DownloadResult.Success(destinationPath))
            }
        } catch (e: Exception) {
            emit(DownloadResult.Error(e.message ?: "Download failed"))
        }
    }.flowOn(Dispatchers.IO)

    companion object {
        private const val DEFAULT_BUFFER_SIZE = 8192
        // Transcoding can take time to start - use generous timeouts
        private const val CONNECT_TIMEOUT_MS = 60_000L      // 60s to connect
        private const val SOCKET_TIMEOUT_MS = 120_000L     // 120s between data chunks
        private const val DOWNLOAD_TIMEOUT_MS = Long.MAX_VALUE  // No overall limit for downloads
    }
}
