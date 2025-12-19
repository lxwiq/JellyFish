package com.lowiq.jellyfish.di

import com.lowiq.jellyfish.data.local.DATA_STORE_FILE_NAME
import com.lowiq.jellyfish.data.local.SecureStorage
import com.lowiq.jellyfish.data.local.createDataStore
import com.lowiq.jellyfish.data.remote.JellyfinDataSource
import com.lowiq.jellyfish.data.remote.JellyfinDataSourceImpl
import com.lowiq.jellyfish.domain.player.VideoPlayer
import org.koin.core.module.Module
import org.koin.dsl.module
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

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
    factory { VideoPlayer() }
}
