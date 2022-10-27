package com.kampungpedia.data.local

import com.kampungpedia.SharedPref
import com.kampungpedia.data.network.auth.model.AuthResponse
import com.kampungpedia.getString
import com.kampungpedia.remove
import com.kampungpedia.setString
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import com.russhwolf.settings.coroutines.FlowSettings
import com.russhwolf.settings.coroutines.getIntFlow
import com.russhwolf.settings.set
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@OptIn(ExperimentalSettingsApi::class)
class AuthLocal internal constructor(
    private val settings: FlowSettings,
//    private val settings: Settings,
    private val json: Json
){
    companion object {
        const val KEY_AUTH = "key-auth"
    }

//    fun auth() = settings.getStringOrNullFlow(KEY_AUTH).map {
//        it?.let {
//            json.decodeFromString(AuthResponse.serializer(), it)
//        }
//    }

    fun auth(): Flow<AuthResponse?>  =
        settings.getStringFlow(KEY_AUTH).let { flow ->
        flow.map {
            it.let { json.decodeFromString(AuthResponse.serializer(), it) }
        }
    }
    suspend fun auth(authResponse: AuthResponse?){
        authResponse?.let {
            settings.putString(KEY_AUTH, json.encodeToString(AuthResponse.serializer(), it))
        }
    }

//    suspend fun auth(authResponse: AuthResponse){
//        settings.putString(KEY_AUTH, json.encodeToString(AuthResponse.serializer(), authResponse))
//    }
//
//    suspend fun auth(authResponse: Flow<AuthResponse>){
//        authResponse.collectLatest {
//            settings.putString(KEY_AUTH, json.encodeToString(AuthResponse.serializer(), it))
//        }
//    }

    suspend fun delete(){
        settings.clear()
    }

}