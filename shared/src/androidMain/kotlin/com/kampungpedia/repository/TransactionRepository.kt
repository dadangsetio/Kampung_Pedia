package com.kampungpedia.repository

import android.app.Application
import com.kampungpedia.DriverFactory
import com.kampungpedia.data.TransactionRepository
import com.kampungpedia.data.local.TransactionStorage
import com.kampungpedia.data.network.ApiNetwork
import com.kampungpedia.data.network.transaction.TransactionApi

fun TransactionRepository.Companion.create(apiNetwork: ApiNetwork, driverFactory: DriverFactory) = TransactionRepository(
    TransactionApi(
        apiNetwork
    ),
    TransactionStorage(
        driverFactory
    )
)