package com.example.shoppingapp.data.remote.service

import retrofit2.Response

fun <T> Response<T>.isSuccess(): Boolean {
    return isSuccessful && body() != null
}

fun <T> Response<T>.getMessage(): String? {
    return when {
        message().isNotBlank() -> message()
        raw().message.isNotBlank() -> raw().message
        else -> null
    }
}
