package com.kampungpedia

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.kampungpedia.data.DataConstant

actual typealias SharedPref = Application

actual fun SharedPref.getInt(key: String ) : Int{
    val prefs: SharedPreferences = this.getSharedPreferences(DataConstant.PREFERENCES_FILENAME, MODE_PRIVATE)
    return prefs.getInt(key, -1)
}

actual fun SharedPref.setInt(key: String, value: Int) {
    val prefs: SharedPreferences = this.getSharedPreferences(DataConstant.PREFERENCES_FILENAME, MODE_PRIVATE)
    val editor = prefs.edit()
    editor.putInt(key,value)
    editor.apply()
}

actual fun SharedPref.setString(key: String, value: String) {
    val prefs = this.getSharedPreferences(DataConstant.PREFERENCES_FILENAME, MODE_PRIVATE)
    val editor = prefs.edit()
    editor.putString(key, value)
    editor.apply()
}

actual fun SharedPref.getString(key: String): String {
    val prefs = this.getSharedPreferences(DataConstant.PREFERENCES_FILENAME, MODE_PRIVATE)
    return prefs.getString(key, "") ?: ""
}

actual fun SharedPref.remove(key: String){
    val prefs = this.getSharedPreferences(DataConstant.PREFERENCES_FILENAME, MODE_PRIVATE)
    prefs.edit().remove(key).apply()
}