package com.kampungpedia.data.local

import com.kampungpedia.DriverFactory
import com.kampungpedia.data.HistoryTransaction
import com.kampungpedia.data.KampungDB
import com.kampungpedia.data.network.transaction.model.TransactionData

class TransactionStorage(databaseFactory: DriverFactory) {
    private val database = KampungDB(databaseFactory.createDriver())
    private val queries = database.historyTransactionQueries

    fun insert(transaction: TransactionData? = null, transactions: List<TransactionData>? = null){
        transaction?.let {
            with(it){
                queries.insertTransaction(uuid, title, totalAmount, createdAt, status)
            }
        }
        transactions?.let { list ->
            list.map {
                with(it){
                    queries.insertTransaction(uuid, title, totalAmount, createdAt, status)
                }
            }
        }
    }

    fun getAll() = queries.selectTransactionAll().executeAsList().map {
        it.toTransactionData()
    }

    fun deleteAll() {
        queries.deleteTransaction()
    }
}

fun HistoryTransaction.toTransactionData() = TransactionData(
    uuid,
    titile,
    total_amount,
    created_at,
    status
)