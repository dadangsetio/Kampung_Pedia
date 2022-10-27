package com.kampungpedia.android.ui.utils

import android.os.Build
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*


const val DATEFORMAT_DB = "yyyy-M-d HH:mm:ss"

fun String.asDateFormat(format: String = "EEEE, dd MMMM yyyy "): String{
    val formatter = SimpleDateFormat(format, Locale("in", "ID"))
    val formatterDB = SimpleDateFormat(DATEFORMAT_DB, Locale("in", "ID"))
    val db = formatterDB.parse(this)
    db?.let {
        return formatter.format(db)
    }
    return ""
}