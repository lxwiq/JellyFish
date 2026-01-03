package com.lowiq.jellyfish.di

import com.lowiq.jellyfish.data.local.DATA_STORE_FILE_NAME
import com.lowiq.jellyfish.data.local.FileManager
import com.lowiq.jellyfish.data.local.SecureStorage
import com.lowiq.jellyfish.data.local.createDataStore
import com.lowiq.jellyfish.data.remote.JellyfinDataSource
import com.lowiq.jellyfish.data.remote.JellyfinDataSourceImpl
import com.lowiq.jellyfish.domain.download.DownloadServiceController
import com.lowiq.jellyfish.domain.download.NotificationPermissionHandler
import com.lowiq.jellyfish.domain.cast.CastManager
import com.lowiq.jellyfish.domain.player.VideoPlayer
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import org.koin.core.module.Module
import org.koin.dsl.module
import java.io.File

actual val platformModule: Module = module {
    single { SecureStorage() }
    single {
        val appDir = File(System.getProperty("user.home"), ".jellyfish")
        appDir.mkdirs()
        createDataStore(File(appDir, DATA_STORE_FILE_NAME).absolutePath)
    }
    single<JellyfinDataSource> { JellyfinDataSourceImpl() }
    factory { VideoPlayer() }
    single { HttpClient(OkHttp) }
    single { FileManager() }
    single { NotificationPermissionHandler() }
    single { DownloadServiceController() }
    single { CastManager() }
}
