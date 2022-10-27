package com.kampungpedia.data.local

import com.kampungpedia.DriverFactory
import com.kampungpedia.data.KampungDB
import com.kampungpedia.data.Banner
import com.kampungpedia.data.network.banner.model.BannerData

class BannerStorage(databaseFactory: DriverFactory) {
    private val database = KampungDB(databaseFactory.createDriver())
    private val queries = database.bannerQueries

    fun insert(banner: BannerData? = null, list: List<BannerData>? = null){
        list?.let { list1 ->
            list1.map {
                queries.insertBanner(it.id.toLong(), it.banner)
            }
        }
        banner?.let {
            queries.insertBanner(it.id.toLong(), it.banner)
        }
    }

    fun getAll() = queries.selectAll().executeAsList().map { it.toBannerData() }

    fun deleteAll() {
        queries.deleteAll()
    }
}

fun Banner.toBannerData() = BannerData(
    id.toInt(),
    banner
)