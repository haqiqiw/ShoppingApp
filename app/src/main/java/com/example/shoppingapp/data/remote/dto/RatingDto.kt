package com.example.shoppingapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class RatingDto(
    @SerializedName("count")
    val count: Int,
    @SerializedName("rate")
    val rate: Double
)
