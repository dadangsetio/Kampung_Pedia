package com.kampungpedia

import android.content.Context
import com.kampungpedia.data.KampungDB
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver

actual class DriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver {
        val driver = AndroidSqliteDriver(KampungDB.Schema, context, "kampung.db")
//        KampungDB.Schema.migrate(driver, 0, 1)
        return driver
    }
}