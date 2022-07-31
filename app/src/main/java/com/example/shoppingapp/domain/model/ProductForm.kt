package com.example.shoppingapp.domain.model

data class ProductForm(
    val category: String,
    val description: String,
    val image: String,
    val price: Double,
    val title: String
)
