package com.example.shoppingapp.data.repository.product

import com.example.shoppingapp.core.util.Resource
import com.example.shoppingapp.domain.model.Product
import com.example.shoppingapp.domain.model.ProductForm
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun getProducts(isCached: Boolean): Flow<Resource<List<Product>>>

    suspend fun updateProduct(id: Int, body: ProductForm): Flow<Resource<Product>>
}
