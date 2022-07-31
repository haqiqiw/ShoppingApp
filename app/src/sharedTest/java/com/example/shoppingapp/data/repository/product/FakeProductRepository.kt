package com.example.shoppingapp.data.repository.product

import com.example.shoppingapp.core.util.Resource
import com.example.shoppingapp.domain.model.Product
import com.example.shoppingapp.domain.model.ProductForm
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeProductRepository : ProductRepository {

    var getProductsFlow = flow<Resource<List<Product>>> {}
    override suspend fun getProducts(isCached: Boolean): Flow<Resource<List<Product>>> {
        return getProductsFlow
    }

    var updateProductFlow = flow<Resource<Product>> {}
    override suspend fun updateProduct(id: Int, body: ProductForm): Flow<Resource<Product>> {
        return updateProductFlow
    }
}
