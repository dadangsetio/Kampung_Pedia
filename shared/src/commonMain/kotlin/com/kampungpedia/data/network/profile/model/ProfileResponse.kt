package com.kampungpedia.data.network.profile.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileResponse(
    @SerialName("name")
    val name: String? = null,
    @SerialName("email")
    val email: String? = null,
    @SerialName("phone")
    val phone: String? = null,
    @SerialName("foto")
    val foto: String? = null
)
