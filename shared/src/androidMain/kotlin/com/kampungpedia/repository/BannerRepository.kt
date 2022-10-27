package com.kampungpedia.repository

import android.app.Activity
import android.app.Application
import com.kampungpedia.DriverFactory
import com.kampungpedia.data.BannerRepository
import com.kampungpedia.data.local.BannerStorage
import com.kampungpedia.data.network.ApiNetwork
import com.kampungpedia.data.network.ApiWorker
import com.kampungpedia.data.network.banner.BannerApi
import io.ktor.client.*

fun BannerRepository.Companion.create(driverFactory: DriverFactory , apiNetwork: ApiNetwork) =
    BannerRepository(
        BannerApi(
            apiNetwork
        ),
        BannerStorage(
            driverFactory
        )
    )