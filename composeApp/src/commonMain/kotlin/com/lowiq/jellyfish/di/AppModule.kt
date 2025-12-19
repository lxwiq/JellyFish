package com.lowiq.jellyfish.di

import com.lowiq.jellyfish.data.local.MediaCache
import com.lowiq.jellyfish.data.local.ServerStorage
import com.lowiq.jellyfish.data.local.UserPreferencesStorage
import com.lowiq.jellyfish.data.repository.AuthRepositoryImpl
import com.lowiq.jellyfish.data.repository.MediaRepositoryImpl
import com.lowiq.jellyfish.data.repository.ServerRepositoryImpl
import com.lowiq.jellyfish.domain.model.Library
import com.lowiq.jellyfish.domain.model.Server
import com.lowiq.jellyfish.domain.repository.AuthRepository
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
import com.lowiq.jellyfish.presentation.screens.home.HomeScreenModel
import com.lowiq.jellyfish.presentation.screens.library.LibraryScreenModel
import com.lowiq.jellyfish.presentation.screens.login.LoginScreenModel
import com.lowiq.jellyfish.presentation.screens.player.VideoPlayerScreenModel
import com.lowiq.jellyfish.presentation.screens.quickconnect.QuickConnectScreenModel
import com.lowiq.jellyfish.presentation.screens.serverlist.ServerListScreenModel
import org.koin.core.module.Module
import org.koin.dsl.module

expect val platformModule: Module

val appModule = module {
    single { ServerStorage(get()) }
    single { MediaCache(get()) }
    single { UserPreferencesStorage(get()) }
}

val dataModule = module {
    single<AuthRepository> { AuthRepositoryImpl(get(), get(), get()) }
    single<ServerRepository> { ServerRepositoryImpl(get(), get(), get()) }
    single<MediaRepository> { MediaRepositoryImpl(get(), get(), get(), get()) }
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
    factory { (itemId: String) -> MovieDetailScreenModel(itemId, get(), get()) }
    factory { (itemId: String) -> SeriesDetailScreenModel(itemId, get(), get()) }
    factory { (itemId: String) -> EpisodeDetailScreenModel(itemId, get(), get()) }
    factory { (itemId: String, title: String, subtitle: String?, startPositionMs: Long) ->
        VideoPlayerScreenModel(itemId, title, subtitle, startPositionMs, get(), get(), get())
    }
}
