package com.example.shoppingapp.data.preference.base

interface BasePreference {

    fun <T> read(key: String, defaultValue: T): T

    fun <T> save(key: String, value: T)

    fun clear(key: String)
}
