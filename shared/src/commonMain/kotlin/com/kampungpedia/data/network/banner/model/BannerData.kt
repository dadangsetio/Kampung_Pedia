package com.kampungpedia.data.network.banner.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BannerData(
    val id: Int,
    val banner: String
)
