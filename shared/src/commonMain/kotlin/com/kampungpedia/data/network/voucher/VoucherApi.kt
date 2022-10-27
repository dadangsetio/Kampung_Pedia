package com.kampungpedia.data.network.voucher

import com.kampungpedia.data.network.ApiNetwork
import io.ktor.client.*
import io.ktor.client.request.*

class VoucherApi(
    private val apiNetwork: ApiNetwork
) {
    companion object{
        const val ENDPOINT_GAMES = "api/v1/games"
        const val ENDPOINT_VOUCHER = "api/v1/vouchers"
    }

    suspend fun games() =
        apiNetwork.client.get(urlString = ENDPOINT_GAMES){
            parameter("client_id", 2)
        }

    suspend fun voucher() =
        apiNetwork.client.get(urlString = ENDPOINT_VOUCHER){
            parameter("client_id", 2)
        }
}