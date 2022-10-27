package com.kampungpedia.data.network.transaction

import com.kampungpedia.data.network.ApiNetwork
import io.ktor.client.request.*

class TransactionApi(
    private val apiNetwork: ApiNetwork
) {
    companion object{
        const val ENDPOINT_TRANSACTION = "api/v1/transaction"
    }

    suspend fun history() =
        apiNetwork.client.get(urlString = ENDPOINT_TRANSACTION)

}