package com.kampungpedia.repository

import android.app.Activity
import android.app.Application
import android.content.Context
import com.kampungpedia.data.ProfileRepository
import com.kampungpedia.data.local.ProfileLocal
import com.kampungpedia.data.network.ApiNetwork
import com.kampungpedia.data.network.ApiWorker
import com.kampungpedia.data.network.profile.ProfileApi
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

fun ProfileRepository.Companion.create(context: Application, apiNetwork: ApiNetwork) = ProfileRepository(
    ProfileApi(
        apiNetwork
    ),
    ProfileLocal(
        context
    )
).also {
    Napier.base(DebugAntilog())
}