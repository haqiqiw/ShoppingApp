package com.example.shoppingapp.ui.productdetail

import com.example.shoppingapp.domain.model.Product
import java.io.Serializable

data class ProductDetailUiState(
    val product: Product = Product.createEmptyProduct()
) : Serializable {
    val id get() = product.id.toString()
    val category get() = product.category
    val description get() = product.description
    val image get() = product.image
    val price get() = product.price
    val title get() = product.title
}
