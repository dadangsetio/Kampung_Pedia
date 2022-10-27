package com.kampungpedia.data

import com.kampungpedia.data.local.BannerStorage
import com.kampungpedia.data.network.NetworkResponse
import com.kampungpedia.data.network.banner.BannerApi
import com.kampungpedia.data.network.banner.model.BannerData
import com.kampungpedia.data.network.banner.model.BannerResponse
import io.ktor.client.call.*

class BannerRepository(
    private val bannerApi: BannerApi,
    private val bannerStorage: BannerStorage
) {

    suspend fun get(
        forceUpdate: Boolean = false
    ): BannerResponse {
        val db = bannerStorage.getAll()
        return if (forceUpdate || db.isEmpty()){
            val response = bannerApi.banners()
            response.apply {
                if (this.status.value == 200){
                    val respon = this.body<BannerResponse>()
                    bannerStorage.insert(list = respon.data)
                }
            }.body<BannerResponse>()
        }else{
            BannerResponse(data = db)
        }
    }

    fun delete(){
        bannerStorage.deleteAll()
    }

    companion object
}