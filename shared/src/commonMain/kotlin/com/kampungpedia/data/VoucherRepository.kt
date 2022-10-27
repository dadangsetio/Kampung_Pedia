package com.kampungpedia.data

import com.kampungpedia.data.network.voucher.VoucherApi
import com.kampungpedia.data.network.voucher.model.ListGamesResponse
import com.kampungpedia.data.network.voucher.model.ListVoucherResponse
import io.ktor.client.call.*

class VoucherRepository(
    private val voucherApi: VoucherApi
) {

    suspend fun getGames() =
        voucherApi.games().body<ListGamesResponse>()

    suspend fun getVoucher() =
        voucherApi.voucher().body<ListVoucherResponse>()

    companion object
}