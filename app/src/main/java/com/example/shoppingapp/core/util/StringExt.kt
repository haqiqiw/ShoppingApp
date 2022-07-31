package com.example.shoppingapp.core.util

import java.util.Locale

fun String.capitalizeWords(): String = replaceFirstChar {
    if (it.isLowerCase()) {
        it.titlecase(Locale.getDefault())
    } else {
        it.toString()
    }
}
