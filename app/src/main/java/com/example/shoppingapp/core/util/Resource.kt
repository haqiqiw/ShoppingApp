package com.example.shoppingapp.core.util

import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception

sealed class Resource<out R> {

    object Loading : Resource<Nothing>()
    data class Success<out T>(val data: T?) : Resource<T>()
    class Error<T>(val message: String) : Resource<T>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[message=$message]"
            is Loading -> "Loading"
        }
    }

    companion object {
        fun <T> createError(exception: Exception): Error<T> {
            return when (exception) {
                is HttpException -> {
                    Error("Oops, something went wrong!")
                }
                is IOException -> {
                    Error("Couldn't reach server, check your internet connection.")
                }
                else -> {
                    createError(exception.message)
                }
            }
        }

        fun <T> createError(message: String? = null): Error<T> {
            return Error(
                if (message != null && message.isNotBlank()) {
                    message
                } else {
                    "An error occured."
                }
            )
        }
    }
}
