package com.example.shoppingapp.domain.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Product(
    @SerializedName("id")
    val id: Int,
    @SerializedName("category")
    val category: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("price")
    val price: String,
    @SerializedName("rate")
    val rate: String,
    @SerializedName("rating_count")
    val ratingCount: Int,
    @SerializedName("title")
    val title: String
) : Serializable {

    companion object {
        fun createEmptyProduct(): Product {
            return Product(-1, "", "", "", "", "", 0, "")
        }
    }
}
