package com.kampungpedia

import com.russhwolf.settings.Settings
import io.ktor.client.*
import io.ktor.client.engine.darwin.*
import platform.UIKit.UIDevice

actual class Platform actual constructor() {
    actual val platform: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion

    actual fun httpClient(config: HttpClientConfig<*>.() -> Unit) = HttpClient(Darwin) {
        config(this)
        engine {
            configureRequest {
                setAllowsCellularAccess(true)
            }
        }
    }

}