package com.kampungpedia.data.network

import kotlinx.coroutines.internal.synchronized
import kotlin.jvm.Volatile

object SessionManager {
    /**
     * User Token
     */
    var userToken: String? = ""
}
