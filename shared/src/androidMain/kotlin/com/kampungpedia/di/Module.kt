package com.kampungpedia.di

import com.kampungpedia.DriverFactory
import com.kampungpedia.data.*
import com.kampungpedia.data.network.ApiNetwork
import com.kampungpedia.logic.*
import com.kampungpedia.repository.create
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule() = module {
    single {
        single { DriverFactory(get()) }
        factory { ApiNetwork.create(get()) }
        single { ProfileRepository.create(get(), get()) }
        single { BannerRepository.create(get(), get()) }
        single { VoucherRepository.create(get()) }
        single { AuthRepository.create(get(), get()) }
        single { TransactionRepository.create(get(), get()) }
    }
}