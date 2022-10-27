package com.kampungpedia.data.network.auth

import com.kampungpedia.data.network.ApiNetwork
import io.ktor.client.request.forms.*
import io.ktor.http.*

class AuthApi(
    private val apiNetwork: ApiNetwork
) {

    companion object {
        const val ENDPOINT_LOGIN = "api/v1/login"
        const val ENDPOINT_LOGIN_OR_REGISTER = "api/v1/loginOrRegister"
    }

    suspend fun login(email: String, password: String) =
        apiNetwork.client.submitForm(
            url = ENDPOINT_LOGIN,
            formParameters = Parameters.build {
                append("email", email)
                append("password", password)
            }
        )

    suspend fun loginOrRegister(token: String) =
        apiNetwork.client.submitForm(
            url = ENDPOINT_LOGIN_OR_REGISTER,
            formParameters = Parameters.build {
                append("token", token)
            }
        )
}