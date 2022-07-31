package com.example.shoppingapp.data.remote.dto

import com.example.shoppingapp.data.local.entity.ProductEntity
import com.google.gson.annotations.SerializedName

data class ProductDto(
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
    @SerializedName("rating")
    val rating: RatingDto,
    @SerializedName("title")
    val title: String
) {
    fun toProductEntity(): ProductEntity {
        return ProductEntity(
            id = id,
            category = category,
            description = description,
            image = image,
            price = price.toString(),
            rate = rating.rate.toString(),
            ratingCount = rating.count,
            title = title
        )
    }
}
