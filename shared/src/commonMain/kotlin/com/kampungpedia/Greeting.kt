package com.kampungpedia

class Greeting {
    fun greeting(): String {
        return "Hello, ${Platform().platform}!"
    }
    val client =Platform().httpClient()

}