package com.kampungpedia

import com.kampungpedia.data.KampungDB
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver

actual class DriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(KampungDB.Schema, "kampung.db")
    }
}