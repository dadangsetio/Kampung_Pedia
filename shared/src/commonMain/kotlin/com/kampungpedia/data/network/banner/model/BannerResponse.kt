package com.kampungpedia.data.network.banner.model

import kotlinx.serialization.Serializable

@Serializable
data class BannerResponse(
    val data: List<BannerData>
)