package com.kampungpedia.android

import android.app.Application
import com.kampungpedia.DriverFactory
import com.kampungpedia.android.BuildConfig
import com.kampungpedia.data.*
import com.kampungpedia.data.local.AuthLocal
import com.kampungpedia.data.local.ProfileLocal
import com.kampungpedia.data.network.ApiNetwork
import com.kampungpedia.di.initKoin
import com.kampungpedia.logic.*
import com.kampungpedia.repository.create
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    private val appModule = module {
        single { DriverFactory(androidContext()) }
        factory { ApiNetwork.create(androidApplication()) }
        single { ProfileRepository.create(androidApplication(), get()) }
        single { BannerRepository.create(get(), get()) }
        single { VoucherRepository.create(get()) }
        single { AuthRepository.create(androidApplication(), get()) }
        single { TransactionRepository.create(get(), get()) }
    }

    private fun initKoin() {
        initKoin {
            if (BuildConfig.DEBUG) androidLogger(Level.ERROR)

            androidContext(this@App)
            modules(appModule)
        }
    }
}