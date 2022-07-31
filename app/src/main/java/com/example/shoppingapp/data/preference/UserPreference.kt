package com.example.shoppingapp.data.preference

import android.content.Context
import com.example.shoppingapp.data.preference.base.BasePreference
import com.example.shoppingapp.data.preference.base.BasePreferenceImpl

class UserPreference(private val basePreference: BasePreference) {

    var userToken: String
        get() = basePreference.read(KEY_USER_TOKEN, "")
        set(userToken) = basePreference.save(KEY_USER_TOKEN, userToken)

    companion object {
        private const val KEY_USER_TOKEN = "user_token"
        private const val PREFERENCE_NAME = "user_preference"

        private var instance: UserPreference? = null

        fun create(context: Context): UserPreference {
            if (instance == null) {
                instance = UserPreference(BasePreferenceImpl(context, PREFERENCE_NAME))
            }
            return instance as UserPreference
        }
    }
}
