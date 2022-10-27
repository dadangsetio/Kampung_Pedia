package com.kampungpedia.data.network.voucher.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ListGamesResponse(
    val status: String?,
    val message: String?,
    @SerialName("list_dtu")
    val listDtu: List<VoucherData>
)
