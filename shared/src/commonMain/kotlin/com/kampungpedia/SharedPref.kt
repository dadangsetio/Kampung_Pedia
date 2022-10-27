package com.kampungpedia

expect class SharedPref

expect fun SharedPref.getInt(key: String) : Int
expect fun SharedPref.setInt(key: String, value: Int)

expect fun SharedPref.getString(key: String) : String
expect fun SharedPref.setString(key: String, value: String)

expect fun SharedPref.remove(key: String)