package com.example.shoppingapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.shoppingapp.domain.model.Product

@Entity(tableName = "product_table")
data class ProductEntity(
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "category")
    val category: String,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "image")
    val image: String,
    @ColumnInfo(name = "price")
    val price: String,
    @ColumnInfo(name = "rate")
    val rate: String,
    @ColumnInfo(name = "rating_count")
    val ratingCount: Int,
    @ColumnInfo(name = "title")
    val title: String
) {
    fun toProduct(): Product {
        return Product(
            id = id,
            category = category,
            description = description,
            image = image,
            price = price,
            rate = rate,
            ratingCount = ratingCount,
            title = title
        )
    }

    companion object {
        fun createEmptyProduct(): ProductEntity {
            return ProductEntity(-1, "", "", "", "", "", 0, "")
        }
    }
}
