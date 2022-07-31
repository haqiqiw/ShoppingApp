package com.example.shoppingapp.core.util

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager

object KeyboardUtils {

    fun hideSoftKeyboard(activity: Activity?): Boolean {
        if (activity == null) return false
        val view = activity.currentFocus ?: return false
        val inputMethodManager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
