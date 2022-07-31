package com.example.shoppingapp.data.preference.base

class FakeBasePreference : BasePreference {

    private val preferenceMap: MutableMap<String, Any> = hashMapOf()

    override fun <T> read(key: String, defaultValue: T): T {
        return (preferenceMap[key] ?: defaultValue) as T
    }

    override fun <T> save(key: String, value: T) {
        preferenceMap[key] = value!!
    }

    override fun clear(key: String) {
        preferenceMap.remove(key)
    }
}
