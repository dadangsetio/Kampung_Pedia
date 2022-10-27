package com.kampungpedia.repository

import android.app.Application
import android.content.Context
import com.kampungpedia.data.network.ApiNetwork
import com.russhwolf.settings.AndroidSettings

fun ApiNetwork.Companion.create(context: Application) = ApiNetwork(
    AndroidSettings(
        context.getSharedPreferences("app_cache", Context.MODE_PRIVATE),
        true
    )
)