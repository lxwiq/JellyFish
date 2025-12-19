package com.lowiq.jellyfish.di

import com.lowiq.jellyfish.data.local.DownloadSettingsStorage
import com.lowiq.jellyfish.data.local.DownloadStorage
import com.lowiq.jellyfish.data.local.MediaCache
import com.lowiq.jellyfish.data.local.PlaybackSyncStorage
import com.lowiq.jellyfish.data.local.SearchHistoryStorage
import com.lowiq.jellyfish.data.local.ServerStorage
import com.lowiq.jellyfish.data.local.UserPreferencesStorage
import com.lowiq.jellyfish.data.remote.DownloadClient
import com.lowiq.jellyfish.data.repository.AuthRepositoryImpl
import com.lowiq.jellyfish.data.repository.DownloadRepositoryImpl
import com.lowiq.jellyfish.data.repository.MediaRepositoryImpl
import com.lowiq.jellyfish.data.repository.ServerRepositoryImpl
import com.lowiq.jellyfish.domain.download.DownloadManager
import com.lowiq.jellyfish.domain.model.Library
import com.lowiq.jellyfish.domain.model.Server
import com.lowiq.jellyfish.domain.sync.PlaybackSyncService
import com.lowiq.jellyfish.domain.repository.AuthRepository
import com.lowiq.jellyfish.domain.repository.DownloadRepository
import com.lowiq.jellyfish.domain.repository.MediaRepository
import com.lowiq.jellyfish.domain.repository.ServerRepository
import com.lowiq.jellyfish.domain.usecase.AddServerUseCase
import com.lowiq.jellyfish.domain.usecase.CheckSessionUseCase
import com.lowiq.jellyfish.domain.usecase.GetServersUseCase
import com.lowiq.jellyfish.domain.usecase.LoginUseCase
import com.lowiq.jellyfish.domain.usecase.QuickConnectUseCase
import com.lowiq.jellyfish.presentation.screens.addserver.AddServerScreenModel
import com.lowiq.jellyfish.presentation.screens.detail.EpisodeDetailScreenModel
import com.lowiq.jellyfish.presentation.screens.detail.MovieDetailScreenModel
import com.lowiq.jellyfish.presentation.screens.detail.SeriesDetailScreenModel
import com.lowiq.jellyfish.presentation.screens.downloads.DownloadsScreenModel
import com.lowiq.jellyfish.presentation.screens.home.HomeScreenModel
import com.lowiq.jellyfish.presentation.screens.library.LibraryScreenModel
import com.lowiq.jellyfish.presentation.screens.login.LoginScreenModel
import com.lowiq.jellyfish.presentation.screens.player.VideoPlayerScreenModel
import com.lowiq.jellyfish.presentation.screens.quickconnect.QuickConnectScreenModel
import com.lowiq.jellyfish.presentation.screens.search.SearchScreenModel
import com.lowiq.jellyfish.presentation.screens.serverlist.ServerListScreenModel
import io.ktor.client.*
import org.koin.core.module.Module
import org.koin.dsl.module

expect val platformModule: Module

val appModule = module {
    single { ServerStorage(get(), get()) }
    single { MediaCache(get()) }
    single { UserPreferencesStorage(get()) }
    single { DownloadStorage(get()) }
    single { DownloadSettingsStorage(get()) }
    single { PlaybackSyncStorage(get()) }
    single { SearchHistoryStorage(get()) }
}

val dataModule = module {
    single<AuthRepository> { AuthRepositoryImpl(get(), get(), get()) }
    single<ServerRepository> { ServerRepositoryImpl(get(), get(), get()) }
    single<MediaRepository> { MediaRepositoryImpl(get(), get(), get(), get()) }
    single { DownloadClient(get()) }
    single<DownloadRepository> { DownloadRepositoryImpl(get(), get(), get(), get(), get(), get(), get()) }
    single { DownloadManager(get(), get(), get(), get(), get(), get(), get()) }
    single { PlaybackSyncService(get(), get(), get()) }
}

val domainModule = module {
    factory { LoginUseCase(get(), get()) }
    factory { QuickConnectUseCase(get(), get()) }
    factory { GetServersUseCase(get()) }
    factory { AddServerUseCase(get()) }
    factory { CheckSessionUseCase(get(), get()) }
}

val presentationModule = module {
    factory { ServerListScreenModel(get(), get()) }
    factory { AddServerScreenModel(get()) }
    factory { (server: Server) -> LoginScreenModel(server, get()) }
    factory { (server: Server) -> QuickConnectScreenModel(server, get()) }
    factory { HomeScreenModel(get(), get(), get()) }
    factory { (library: Library) -> LibraryScreenModel(library, get(), get(), get()) }
    factory { (itemId: String) -> MovieDetailScreenModel(itemId, get(), get(), get(), get()) }
    factory { (itemId: String) -> SeriesDetailScreenModel(itemId, get(), get()) }
    factory { (itemId: String) -> EpisodeDetailScreenModel(itemId, get(), get()) }
    factory { params ->
        VideoPlayerScreenModel(
            itemId = params[0],
            itemTitle = params[1],
            itemSubtitle = params[2],
            startPositionMs = params[3],
            offlineFilePath = params[4],
            downloadId = params[5],
            videoPlayer = get(),
            serverRepository = get(),
            mediaRepository = get(),
            downloadRepository = get(),
            playbackSyncService = get()
        )
    }
    factory { DownloadsScreenModel(get(), get()) }
    factory { SearchScreenModel(get(), get(), get()) }
}
