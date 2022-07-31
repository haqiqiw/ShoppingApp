package com.example.shoppingapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UpdateProductDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("category")
    val category: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("price")
    val price: Double,
    @SerializedName("title")
    val title: String
)
