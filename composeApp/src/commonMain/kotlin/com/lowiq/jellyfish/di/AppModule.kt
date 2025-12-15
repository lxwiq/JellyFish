package com.lowiq.jellyfish.di

import org.koin.dsl.module

val appModule = module {
    // Will be populated with repositories, use cases, etc.
}

val dataModule = module {
    // DataSources and Repository implementations
}

val domainModule = module {
    // Use cases
}
