package com.kampungpedia.repository

import android.app.Application
import android.content.Context
import com.kampungpedia.data.AuthRepository
import com.kampungpedia.data.local.AuthLocal
import com.kampungpedia.data.network.ApiNetwork
import com.kampungpedia.data.network.auth.AuthApi
import com.russhwolf.settings.AndroidSettings
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.coroutines.toFlowSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.serialization.json.Json

@OptIn(ExperimentalSettingsApi::class, ExperimentalCoroutinesApi::class)
fun AuthRepository.Companion.create(context: Application, apiNetwork: ApiNetwork) = AuthRepository(
    AuthApi(
        apiNetwork
    ),
    AuthLocal(
        AndroidSettings(
            context.getSharedPreferences("app_cache", Context.MODE_PRIVATE),
            true
        ).toFlowSettings(Dispatchers.Main),
        Json {
            isLenient = true
            ignoreUnknownKeys = true
        }
    )
)