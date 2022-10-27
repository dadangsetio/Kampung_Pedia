package com.kampungpedia.data.network.banner

import com.kampungpedia.data.network.ApiNetwork
import com.kampungpedia.data.network.ApiRepositories
import com.kampungpedia.data.network.NetworkResponse
import com.kampungpedia.data.network.banner.model.BannerResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class BannerApi(
    private val apiNetwork: ApiNetwork
) {
    companion object {
        const val ENDPOINT_BANNERS = "api/v1/banners"
    }

    suspend fun banners() =
        apiNetwork.client.get(urlString = ENDPOINT_BANNERS)
}