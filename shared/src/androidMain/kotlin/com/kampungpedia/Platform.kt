package com.kampungpedia

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import java.util.concurrent.TimeUnit

actual class Platform actual constructor() {
    actual val platform: String = "Android ${android.os.Build.VERSION.SDK_INT}"

    actual fun httpClient(config: HttpClientConfig<*>.() -> Unit) = HttpClient(OkHttp) {
        config(this)

        engine {
            config {
                retryOnConnectionFailure(true)
                connectTimeout(0, TimeUnit.SECONDS)
            }
        }
    }
}