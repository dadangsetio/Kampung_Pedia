package com.kampungpedia.utils

class UnexpectedException(message: String? = "Unexpected", throwable: Throwable? = null) :
    Exception(message, throwable)

class LoadingException: Exception("Loading...")


