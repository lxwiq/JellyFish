package com.lowiq.jellyfish

import android.app.Application
import com.lowiq.jellyfish.di.appModule
import com.lowiq.jellyfish.di.dataModule
import com.lowiq.jellyfish.di.domainModule
import com.lowiq.jellyfish.di.platformModule
import com.lowiq.jellyfish.di.presentationModule
import com.lowiq.jellyfish.domain.download.AppLifecycleObserver
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class JellyFishApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@JellyFishApplication)
            modules(platformModule, appModule, dataModule, domainModule, presentationModule)
        }

        val lifecycleObserver: AppLifecycleObserver by inject()
        lifecycleObserver.start()
    }
}
