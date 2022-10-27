package com.kampungpedia.data.network.voucher.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ListVoucherResponse(
    val status: String?,
    val message: String?,
    @SerialName("list_voucher")
    val listVoucher: List<VoucherData>
)