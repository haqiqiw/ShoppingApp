package com.example.shoppingapp.ui.productdetail

import android.content.Context
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.example.shoppingapp.core.util.DispatchersProvider
import com.example.shoppingapp.core.util.DispatchersProviderImpl
import com.example.shoppingapp.data.local.ProductDatabase
import com.example.shoppingapp.data.local.dao.ProductDao
import com.example.shoppingapp.data.remote.service.ProductService
import com.example.shoppingapp.data.remote.service.ServiceBuilder
import com.example.shoppingapp.data.repository.product.ProductRepository
import com.example.shoppingapp.data.repository.product.ProductRepositoryImpl
import com.example.shoppingapp.domain.usecase.product.ProductUseCase
import com.example.shoppingapp.domain.usecase.product.ProductUseCaseImpl

class ProductDetailViewModelFactory(
    owner: SavedStateRegistryOwner,
    context: Context,
    productService: ProductService = ServiceBuilder.productService,
    productDao: ProductDao = ProductDatabase.create(context).productDao(),
    productRepository: ProductRepository = ProductRepositoryImpl(productService, productDao),
    private val dispatchersProvider: DispatchersProvider = DispatchersProviderImpl(),
    private val productUseCase: ProductUseCase = ProductUseCaseImpl(productRepository)
) : AbstractSavedStateViewModelFactory(owner, null) {
    override fun <T : ViewModel?> create(key: String, modelClass: Class<T>, handle: SavedStateHandle): T {
        if (modelClass.isAssignableFrom(ProductDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProductDetailViewModel(dispatchersProvider, productUseCase, handle) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
