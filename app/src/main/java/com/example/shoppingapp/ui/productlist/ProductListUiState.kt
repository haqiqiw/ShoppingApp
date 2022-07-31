package com.example.shoppingapp.ui.productlist

import com.example.shoppingapp.domain.model.Product
import java.io.Serializable

data class ProductListUiState(
    val products: List<Product> = listOf(),
    val isLoading: Boolean = false
) : Serializable
