package com.kampungpedia.utils

import io.github.aakira.napier.Napier
import io.ktor.client.plugins.*
import io.ktor.util.network.*

class NetworkResponseCode {

    /**
     * responses status code.
     */
    fun checkError(e: Throwable): Int {
        println(e)
        // Handle Error
        return when (e) {

            //For 3xx responses
            is RedirectResponseException -> {
                (e.response.status.value)
            }
            //For 4xx responses
            is ClientRequestException -> {
                (e.response.status.value)
            }

            //For 5xx responses
            is ServerResponseException -> {
                (e.response.status.value)
            }

            // Network Error
            is UnresolvedAddressException -> {
                // Internet Error
                -1
            }
            else -> {
                // Unhandled error
                -2
            }
        }
    }
}