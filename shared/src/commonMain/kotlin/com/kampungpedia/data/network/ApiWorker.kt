package com.kampungpedia.data.network

import com.kampungpedia.SharedPref
import com.kampungpedia.data.preferences.Preferences
import io.github.aakira.napier.Napier
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.observer.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json


class ApiWorker {

    val BASE_URL = "http://api.kampungpedia.com/"
    val METERAN_URL = "http://meteran.kampungpedia.com/api"

    private val client = HttpClient(CIO) {
        //Header
        install(DefaultRequest) {
            header("Accept", "application/json")
            header("Content-type", "application/json")
            contentType(ContentType.Application.Json)
            //Pass your token
            header("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOlwvXC9hcGkua2FtcHVuZ3BlZGlhLmNvbVwvYXBpXC92MVwvbG9naW5PclJlZ2lzdGVyIiwiaWF0IjoxNjYwOTAyOTAzLCJleHAiOjE2NjM0OTQ5MDMsIm5iZiI6MTY2MDkwMjkwMywianRpIjoiZHBZOU1JeXlCVVdqUXI5MCIsInN1YiI6MSwicHJ2IjoiMjNiZDVjODk0OWY2MDBhZGIzOWU3MDFjNDAwODcyZGI3YTU5NzZmNyJ9.nJ4lh082HWXxwIuTVaAgZw1hPSC0uFy9-cYwrSYQSMU")
            Napier.d("API: "+url.buildString())
            url(BASE_URL)
        }

        // Json
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }


        // Timeout
        install(HttpTimeout) {
            requestTimeoutMillis = 95000L
            connectTimeoutMillis = 95000L
            socketTimeoutMillis = 95000L
        }

        //Now you see response logs inside terminal
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }

        //Print other logs
        install(ResponseObserver) {
            onResponse { response ->
                Napier.d("ApiService: "+"HTTP status: ${response.status.value}")
            }
        }

    }

    fun getClient(): HttpClient {
        return client
    }
}