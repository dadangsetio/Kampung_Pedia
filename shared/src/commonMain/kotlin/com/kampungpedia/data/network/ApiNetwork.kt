package com.kampungpedia.data.network

import com.kampungpedia.data.AuthRepository
import com.kampungpedia.data.local.AuthLocal
import com.kampungpedia.data.network.auth.model.AuthResponse
import com.russhwolf.settings.Settings
import io.github.aakira.napier.Napier
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.observer.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.singleOrNull
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ApiNetwork(
    private val settings: Settings
): KoinComponent {

//    suspend fun client(): HttpClient {
//        var token = ""
//        coroutineScope {
//            token = authRepository.auth.singleOrNull()?.accessToken ?: ""
//        }
//        return HttpClient(CIO){
//            install(DefaultRequest){
//                url("http://api.kampungpedia.com/")
//                header("Accept", "application/json")
//                contentType(ContentType.Application.Json)
//
//                bearerAuth(token)
//            }
//
//            install(ContentNegotiation){
//                json(Json {
//                    prettyPrint = true
//                    isLenient = true
//                    ignoreUnknownKeys = true
//                })
//            }
//
//            install(HttpTimeout){
//                requestTimeoutMillis = 95000L
//                connectTimeoutMillis = 95000L
//                socketTimeoutMillis = 95000L
//            }
//
//            install(Logging) {
//                logger = Logger.DEFAULT
//                level = LogLevel.ALL
//            }
//
//            install(ResponseObserver) {
//                onResponse { response ->
//                    Napier.d("ApiService: "+"HTTP status: ${response.status.value}")
//                }
//            }
//        }
//    }


    val client: HttpClient
    get() {
        return HttpClient(CIO){
            install(DefaultRequest){
                url("http://api.kampungpedia.com/")
                header("Accept", "application/json")
                contentType(ContentType.Application.Json)
                settings.getStringOrNull(AuthLocal.KEY_AUTH)?.let {
                    bearerAuth(Json.decodeFromString(AuthResponse.serializer(), it).accessToken)
                }
            }



            install(ContentNegotiation){
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }

            install(HttpTimeout){
                requestTimeoutMillis = 95000L
                connectTimeoutMillis = 95000L
                socketTimeoutMillis = 95000L
            }

            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL
            }

            install(ResponseObserver) {
                onResponse { response ->
                    Napier.d("ApiService: "+"HTTP status: ${response.status.value}")
                }
            }
        }
    }

    companion object
}