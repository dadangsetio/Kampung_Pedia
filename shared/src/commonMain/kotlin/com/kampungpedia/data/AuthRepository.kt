package com.kampungpedia.data

import com.kampungpedia.data.local.AuthLocal
import com.kampungpedia.data.network.NetworkResponse
import com.kampungpedia.data.network.auth.AuthApi
import com.kampungpedia.data.network.auth.model.AuthResponse
import io.ktor.client.call.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

class AuthRepository internal constructor(
    private val authApi: AuthApi,
    private val authLocal: AuthLocal,
) {
    suspend fun auth(email: String, password: String): Flow<AuthResponse> {
        val response = flowOf(authApi.login(email, password).apply {
            if (this.status.value == 200){
                authLocal.auth(this.body())
            }
        }.body<AuthResponse>())
        return authLocal.auth().combine(response){ local, network, ->
            local ?: network
        }
    }


    suspend fun auth(token: String): Flow<AuthResponse> {
        val response = authApi.loginOrRegister(token).apply {
            if (this.status.value == 200){
                authLocal.auth(this.body())
            }
        }.body<AuthResponse>()
        return authLocal.auth().combine(flowOf(response)){ local, network, ->
            local ?: network
        }
    }


    suspend fun logout() = authLocal.delete()

    val auth = authLocal.auth()

    companion object
}