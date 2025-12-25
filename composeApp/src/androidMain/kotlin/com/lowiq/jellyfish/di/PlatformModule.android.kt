package com.lowiq.jellyfish.di

import com.lowiq.jellyfish.data.local.DATA_STORE_FILE_NAME
import com.lowiq.jellyfish.data.local.FileManager
import com.lowiq.jellyfish.data.local.SecureStorage
import com.lowiq.jellyfish.data.local.createDataStore
import com.lowiq.jellyfish.data.remote.JellyfinDataSource
import com.lowiq.jellyfish.data.remote.JellyfinDataSourceImpl
import com.lowiq.jellyfish.domain.download.DownloadServiceController
import com.lowiq.jellyfish.domain.download.NotificationPermissionHandler
import com.lowiq.jellyfish.domain.player.VideoPlayer
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    single { SecureStorage(androidContext()) }
    single {
        val context = androidContext()
        createDataStore(context.filesDir.resolve(DATA_STORE_FILE_NAME).absolutePath)
    }
    single<JellyfinDataSource> { JellyfinDataSourceImpl(androidContext()) }
    factory { VideoPlayer(androidContext()) }
    single { HttpClient(OkHttp) }
    single { FileManager(androidContext()) }
    single { NotificationPermissionHandler(androidContext()) }
    single { DownloadServiceController(androidContext()) }
}
