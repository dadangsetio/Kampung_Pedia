package com.kampungpedia.android.ui.utils

import java.text.NumberFormat
import java.util.*
import java.util.Currency

fun Long.asCurrency(locale: Locale = Locale("in", "ID")): String {
    val nb = NumberFormat.getCurrencyInstance()
    nb.currency = Currency.getInstance(locale)
    nb.maximumFractionDigits = 0
    return nb.format(this)
}