package com.kampungpedia.data.network.voucher.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VoucherDenom(
    val id: String?,
    @SerialName("package")
    val packages: String?,
    val price: Long?
)
