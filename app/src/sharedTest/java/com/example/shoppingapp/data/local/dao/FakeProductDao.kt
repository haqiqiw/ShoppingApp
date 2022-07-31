package com.example.shoppingapp.data.local.dao

import com.example.shoppingapp.data.local.entity.ProductEntity

class FakeProductDao : ProductDao {
    override suspend fun insertProducts(products: List<ProductEntity>) = Unit

    override fun updateProduct(
        id: Int,
        category: String,
        description: String,
        image: String,
        price: String,
        title: String
    ) = Unit

    override fun deleteProducts(productIds: List<Int>) = Unit

    var getProductsResult = listOf<ProductEntity>()
    override fun getProducts(): List<ProductEntity> {
        return getProductsResult
    }

    var getProductByIdResult = ProductEntity(
        -1, "", "", "", "", "", 0, ""
    )
    override fun getProductById(id: Int): ProductEntity {
        return getProductByIdResult
    }
}
