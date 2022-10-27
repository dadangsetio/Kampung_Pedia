package com.kampungpedia.data

import com.kampungpedia.data.local.ProfileLocal
import com.kampungpedia.data.network.NetworkResponse
import com.kampungpedia.data.network.profile.model.ProfileResponse
import com.kampungpedia.data.network.profile.ProfileApi
import com.kampungpedia.data.network.profile.ProfileEditRequest
import io.ktor.client.call.*

class ProfileRepository internal constructor(
    private val profileApi: ProfileApi,
    private val profileLocal: ProfileLocal
){
    suspend fun getProfile(
        forceUpdate: Boolean = false
    ): ProfileResponse {
        val  profilePref = profileLocal.profile

        return if (forceUpdate || profilePref == null){
            val response = profileApi.profle()
            val profileResponse = response.body<ProfileResponse>()
            response.apply {
                if (this.status.value == 200){
                    profileLocal.profile = profileResponse
                }
            }

            profileResponse
        }else{
            profilePref
        }
    }

    fun delete() = profileLocal.delete()

    suspend fun editProfile(
        request: ProfileEditRequest
    ) =  profileApi.profle(request)
        .apply {
            println(this.body<String>())
            if (this.status.value == 200){
                profileLocal.profile = this.body()
            }
        }.body<ProfileResponse>()

    companion object
}