package com.kampungpedia.di

import com.kampungpedia.logic.*
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

fun initKoin(koinAppDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        koinAppDeclaration()
        modules(
            logicModule
        )
    }

// iOS
fun initKoin() = initKoin {  }


val logicModule = module {
    single { Profile(get()) }
    single { Banners(get()) }
    single { ListVoucher(get()) }
    single { ListGames(get()) }
    single { Auth(get()) }
    single { ListTransaction(get()) }
}