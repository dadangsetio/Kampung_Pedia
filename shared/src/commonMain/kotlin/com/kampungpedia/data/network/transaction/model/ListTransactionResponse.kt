package com.kampungpedia.data.network.transaction.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ListTransactionResponse(
    @SerialName("current_page")
    val currentPage: Int? = null,

    val data: List<TransactionData>,

    @SerialName("first_page_url")
    val firstPageURL: String? = null,

    val from: Long? = null,

    @SerialName("next_page_url")
    val nextPageURL: String? = null,

    val path: String? = null,

    @SerialName("per_page")
    val perPage: Int? = null,

    @SerialName("prev_page_url")
    val prevPageURL: String? = null,

    val to: Long? = null
)