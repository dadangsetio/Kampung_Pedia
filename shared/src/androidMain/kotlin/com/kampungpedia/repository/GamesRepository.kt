package com.kampungpedia.repository

import com.kampungpedia.data.VoucherRepository
import com.kampungpedia.data.network.ApiNetwork
import com.kampungpedia.data.network.ApiWorker
import com.kampungpedia.data.network.voucher.VoucherApi

fun VoucherRepository.Companion.create(apiNetwork: ApiNetwork) = VoucherRepository(
    VoucherApi(
        apiNetwork
    )
)