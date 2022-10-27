package com.kampungpedia.data.network.voucher.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VoucherData(
    val id: String?,
    val name: String?,
    val image: String?,
    val publisher: String?,
    @SerialName("publisher_website")
    val publisherWebsite: String?,
    val denom: List<VoucherDenom>
)
