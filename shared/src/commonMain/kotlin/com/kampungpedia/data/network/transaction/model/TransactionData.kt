package com.kampungpedia.data.network.transaction.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TransactionData(
    val uuid: String,
    val title: String,
    @SerialName("total_amount")
    val totalAmount: Long,
    @SerialName("created_at")
    val createdAt: String,
    val status: String
)