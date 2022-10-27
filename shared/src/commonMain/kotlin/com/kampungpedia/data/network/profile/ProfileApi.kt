package com.kampungpedia.data.network.profile

import com.kampungpedia.data.network.ApiNetwork
import com.kampungpedia.data.network.ApiRepositories
import com.kampungpedia.data.network.NetworkResponse
import com.kampungpedia.data.network.profile.model.ProfileResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*

internal class ProfileApi(
    private val apiNetwork: ApiNetwork
) {

    companion object{
        const val ENDPOINT_ME = "api/v1/me"
    }

    suspend fun profle() = apiNetwork.client
        .get(urlString = ENDPOINT_ME)

    suspend fun profle(profileEditRequest: ProfileEditRequest) =
        apiNetwork.client
            .submitFormWithBinaryData(
                formData = formData {
                    profileEditRequest.photo?.let {
                        append("photo", it)
                    }
                    profileEditRequest.name?.let {
                        append("name", it)
                    }
            }){
                url(ENDPOINT_ME)
            }
}