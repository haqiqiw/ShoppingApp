package com.example.shoppingapp.data.preference.base

import android.content.Context
import android.content.SharedPreferences

class BasePreferenceImpl(context: Context, name: String) : BasePreference {

    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences(name, Context.MODE_PRIVATE)
    }

    override fun <T> read(key: String, defaultValue: T): T {
        val value = when (defaultValue) {
            is Int -> {
                sharedPreferences.getInt(key, defaultValue)
            }
            is String -> {
                sharedPreferences.getString(key, defaultValue)
            }
            is Boolean -> {
                sharedPreferences.getBoolean(key, defaultValue)
            }
            is Float -> {
                sharedPreferences.getFloat(key, defaultValue)
            }
            is Long -> {
                sharedPreferences.getLong(key, defaultValue)
            }
            else -> {
                throw UnsupportedOperationException()
            }
        } as T
        return value
    }

    override fun <T> save(key: String, value: T) {
        val editor = sharedPreferences.edit()
        when (value) {
            is Int -> {
                editor.putInt(key, value)
            }
            is String -> {
                editor.putString(key, value)
            }
            is Boolean -> {
                editor.putBoolean(key, value)
            }
            is Float -> {
                editor.putFloat(key, value)
            }
            is Long -> {
                editor.putLong(key, value)
            }
            else -> {
                throw UnsupportedOperationException()
            }
        }
        editor.apply()
    }

    override fun clear(key: String) {
        sharedPreferences.edit().run {
            remove(key)
            apply()
        }
    }
}
