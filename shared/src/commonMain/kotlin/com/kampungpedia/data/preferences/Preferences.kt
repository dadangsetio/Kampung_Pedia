package com.kampungpedia.data.preferences

import com.kampungpedia.*

class Preferences(private val sharedPref: SharedPref) {
    fun getInt(key: String): Int {
        return sharedPref.getInt(key)
    }
    fun setInt(key: String, int: Int){
        sharedPref.setInt(key, int)
    }
    fun setString(key: String, value: String) {
        sharedPref.setString(key, value)
    }
    fun getString(key: String): String{
        return sharedPref.getString(key)
    }
}