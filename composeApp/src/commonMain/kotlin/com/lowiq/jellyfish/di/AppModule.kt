package com.lowiq.jellyfish.di

import com.lowiq.jellyfish.data.local.ServerStorage
import com.lowiq.jellyfish.data.repository.AuthRepositoryImpl
import com.lowiq.jellyfish.data.repository.ServerRepositoryImpl
import com.lowiq.jellyfish.domain.repository.AuthRepository
import com.lowiq.jellyfish.domain.repository.ServerRepository
import com.lowiq.jellyfish.domain.usecase.AddServerUseCase
import com.lowiq.jellyfish.domain.usecase.CheckSessionUseCase
import com.lowiq.jellyfish.domain.usecase.GetServersUseCase
import com.lowiq.jellyfish.domain.usecase.LoginUseCase
import com.lowiq.jellyfish.domain.usecase.QuickConnectUseCase
import org.koin.core.module.Module
import org.koin.dsl.module

expect val platformModule: Module

val appModule = module {
    single { ServerStorage(get()) }
}

val dataModule = module {
    single<AuthRepository> { AuthRepositoryImpl(get(), get(), get()) }
    single<ServerRepository> { ServerRepositoryImpl(get(), get(), get()) }
}

val domainModule = module {
    factory { LoginUseCase(get(), get()) }
    factory { QuickConnectUseCase(get(), get()) }
    factory { GetServersUseCase(get()) }
    factory { AddServerUseCase(get()) }
    factory { CheckSessionUseCase(get(), get()) }
}
