package com.lowiq.jellyfish.data.remote

import io.ktor.client.*
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
        onWrite: suspend (ByteArray) -> Unit
    ): Flow<DownloadResult> = flow {
        try {
            httpClient.prepareGet(url).execute { response ->
                if (!response.status.isSuccess()) {
                    emit(DownloadResult.Error("HTTP ${response.status.value}: ${response.status.description}"))
                    return@execute
                }

                val contentLength = response.contentLength() ?: 0L
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
    }
}
