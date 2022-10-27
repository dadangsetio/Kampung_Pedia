package com.kampungpedia.data

import com.kampungpedia.data.local.TransactionStorage
import com.kampungpedia.data.network.transaction.TransactionApi
import com.kampungpedia.data.network.transaction.model.ListTransactionResponse
import io.ktor.client.call.*

class TransactionRepository(
    private val transactionApi: TransactionApi,
    private val transactionStorage: TransactionStorage
) {

    suspend fun getListTransaction(
        forceUpdate: Boolean = false
    ): ListTransactionResponse {
        val db = transactionStorage.getAll()
        return if (forceUpdate || db.isEmpty()){
            val response = transactionApi.history()
            response.apply {
                if (this.status.value == 200){
                    transactionStorage.deleteAll()
                    transactionStorage.insert(transactions = this.body<ListTransactionResponse>().data)
                }
            }.body()
        }else{
            ListTransactionResponse(data = db)
        }
    }

    fun delete(){
        transactionStorage.deleteAll()
    }

    companion object
}