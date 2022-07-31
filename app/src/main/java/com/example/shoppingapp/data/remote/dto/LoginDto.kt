package com.example.shoppingapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class LoginDto(
    @SerializedName("token")
    val token: String
)
