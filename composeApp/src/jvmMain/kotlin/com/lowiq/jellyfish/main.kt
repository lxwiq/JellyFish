package com.lowiq.jellyfish

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.lowiq.jellyfish.di.appModule
import com.lowiq.jellyfish.di.dataModule
import com.lowiq.jellyfish.di.domainModule
import com.lowiq.jellyfish.di.platformModule
import org.koin.core.context.startKoin

fun main() {
    startKoin {
        modules(platformModule, appModule, dataModule, domainModule)
    }

    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "JellyFish",
        ) {
            App()
        }
    }
}