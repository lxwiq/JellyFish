package com.lowiq.jellyfish.di

import com.lowiq.jellyfish.data.local.DATA_STORE_FILE_NAME
import com.lowiq.jellyfish.data.local.FileManager
import com.lowiq.jellyfish.data.local.SecureStorage
import com.lowiq.jellyfish.data.local.createDataStore
import com.lowiq.jellyfish.data.remote.DownloadClient
import com.lowiq.jellyfish.data.remote.JellyfinDataSource
import com.lowiq.jellyfish.data.remote.JellyfinDataSourceImpl
import com.lowiq.jellyfish.domain.download.DownloadNotifier
import com.lowiq.jellyfish.domain.download.DownloadServiceController
import com.lowiq.jellyfish.domain.download.NotificationPermissionHandler
import com.lowiq.jellyfish.domain.player.VideoPlayer
import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin
import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.core.module.Module
import org.koin.dsl.module
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

@OptIn(ExperimentalForeignApi::class)
actual val platformModule: Module = module {
    single { SecureStorage() }
    single {
        val documentDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null,
        )
        requireNotNull(documentDirectory) { "Failed to get document directory" }
        val dataStoreUrl = documentDirectory.URLByAppendingPathComponent(DATA_STORE_FILE_NAME)
        requireNotNull(dataStoreUrl) { "Failed to create data store URL" }
        createDataStore(dataStoreUrl.path!!)
    }
    single<JellyfinDataSource> { JellyfinDataSourceImpl() }
    single { HttpClient(Darwin) }
    single { DownloadClient(get()) }
    single { FileManager() }
    factory { VideoPlayer() }
    single { DownloadNotifier() }
    single { NotificationPermissionHandler() }
    single { DownloadServiceController() }
}
