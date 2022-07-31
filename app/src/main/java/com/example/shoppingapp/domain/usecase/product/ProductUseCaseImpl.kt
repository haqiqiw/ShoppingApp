package com.example.shoppingapp.domain.usecase.product

import com.example.shoppingapp.core.util.Resource
import com.example.shoppingapp.data.repository.product.ProductRepository
import com.example.shoppingapp.domain.model.Product
import com.example.shoppingapp.domain.model.ProductForm
import kotlinx.coroutines.flow.Flow

class ProductUseCaseImpl(
    private val productRepository: ProductRepository
) : ProductUseCase {
    override suspend fun getProducts(isCached: Boolean): Flow<Resource<List<Product>>> {
        return productRepository.getProducts(isCached)
    }

    override suspend fun updateProduct(id: Int, body: ProductForm): Flow<Resource<Product>> {
        return productRepository.updateProduct(id, body)
    }
}
