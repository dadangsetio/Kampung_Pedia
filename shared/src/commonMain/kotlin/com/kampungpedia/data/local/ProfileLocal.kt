package com.kampungpedia.data.local

import com.kampungpedia.SharedPref
import com.kampungpedia.data.network.profile.model.ProfileResponse
import com.kampungpedia.getString
import com.kampungpedia.remove
import com.kampungpedia.setString
import kotlinx.serialization.json.Json

class ProfileLocal internal constructor(private val pref: SharedPref){

    companion object{
        const val KEY_PROFILE = "key-profile"
    }

    var profile: ProfileResponse?
    get() {
        return pref.getString(KEY_PROFILE).takeIf { it.isNotEmpty() }?.let {
            Json.decodeFromString(ProfileResponse.serializer(), it)
        }
    }
    set(value) {
        value?.let {
            pref.setString(KEY_PROFILE, Json.encodeToString(ProfileResponse.serializer(), value))
        }
    }

    fun delete(){
        pref.remove(KEY_PROFILE)
    }
}