package com.kampungpedia

import com.kampungpedia.data.DataConstant
import platform.Foundation.NSUserDefaults
import platform.darwin.NSObject

actual typealias SharedPref = NSObject

actual fun SharedPref.getInt(key: String) : Int {
    return NSUserDefaults.standardUserDefaults.integerForKey(key).toInt()
}

actual fun SharedPref.setInt(key: String, value : Int){
    NSUserDefaults.standardUserDefaults.setInteger(value.toLong(),key)
}

actual fun SharedPref.setString(key: String, value: String) {
   NSUserDefaults.standardUserDefaults.setString(value, key)
}

actual fun SharedPref.getString(key: String): String {
    return NSUserDefaults.standardUserDefaults.stringForKey(key) ?: ""
}

actual fun SharedPref.remove(key: String){
    return NSUserDefaults.standardUserDefaults.remove(key)
}