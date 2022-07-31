package com.example.shoppingapp.data.repository.product

import com.example.shoppingapp.core.util.Resource
import com.example.shoppingapp.data.local.dao.ProductDao
import com.example.shoppingapp.data.remote.service.ProductService
import com.example.shoppingapp.data.remote.service.getMessage
import com.example.shoppingapp.data.remote.service.isSuccess
import com.example.shoppingapp.domain.model.Product
import com.example.shoppingapp.domain.model.ProductForm
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ProductRepositoryImpl(
    private val productService: ProductService,
    private val productDao: ProductDao
) : ProductRepository {
    override suspend fun getProducts(isCached: Boolean): Flow<Resource<List<Product>>> = flow {
        emit(Resource.Loading)

        if (!isCached) {
            try {
                val response = productService.getProducts()
                if (response.isSuccess()) {
                    val responseBody = response.body().orEmpty()
                    productDao.deleteProducts(responseBody.map { it.id })
                    productDao.insertProducts(responseBody.map { it.toProductEntity() })
                } else {
                    emit(Resource.createError(response.getMessage()))
                }
            } catch (e: Exception) {
                emit(Resource.createError(e))
            }
        }

        val localProducts = productDao.getProducts().map { it.toProduct() }
        emit(Resource.Success(localProducts))
    }

    override suspend fun updateProduct(id: Int, body: ProductForm): Flow<Resource<Product>> = flow {
        emit(Resource.Loading)

        try {
            val response = productService.udpateProduct(id, body)
            if (response.isSuccess()) {
                productDao.updateProduct(
                    id, body.category, body.description, body.image, body.price.toString(), body.title
                )
                val product = productDao.getProductById(id).toProduct()
                emit(Resource.Success(product))
            } else {
                emit(Resource.createError(response.getMessage()))
            }
        } catch (e: Exception) {
            emit(Resource.createError(e))
        }
    }
}
