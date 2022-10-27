package com.kampungpedia

import com.squareup.sqldelight.db.SqlDriver

// in src/commonMain/kotlin
expect class DriverFactory {
    fun createDriver(): SqlDriver
}
