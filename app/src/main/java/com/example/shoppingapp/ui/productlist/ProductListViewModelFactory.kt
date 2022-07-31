package com.example.shoppingapp.ui.productlist

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shoppingapp.core.util.DispatchersProvider
import com.example.shoppingapp.core.util.DispatchersProviderImpl
import com.example.shoppingapp.data.local.ProductDatabase
import com.example.shoppingapp.data.local.dao.ProductDao
import com.example.shoppingapp.data.preference.UserPreference
import com.example.shoppingapp.data.remote.service.ProductService
import com.example.shoppingapp.data.remote.service.ServiceBuilder
import com.example.shoppingapp.data.remote.service.UserService
import com.example.shoppingapp.data.repository.product.ProductRepository
import com.example.shoppingapp.data.repository.product.ProductRepositoryImpl
import com.example.shoppingapp.data.repository.user.UserRepository
import com.example.shoppingapp.data.repository.user.UserRepositoryImpl
import com.example.shoppingapp.domain.usecase.product.ProductUseCase
import com.example.shoppingapp.domain.usecase.product.ProductUseCaseImpl
import com.example.shoppingapp.domain.usecase.user.UserUseCase
import com.example.shoppingapp.domain.usecase.user.UserUseCaseImpl

class ProductListViewModelFactory(
    context: Context,
    productService: ProductService = ServiceBuilder.productService,
    productDao: ProductDao = ProductDatabase.create(context).productDao(),
    productRepository: ProductRepository = ProductRepositoryImpl(productService, productDao),
    userService: UserService = ServiceBuilder.userService,
    userPreference: UserPreference = UserPreference.create(context),
    userRepository: UserRepository = UserRepositoryImpl(userService, userPreference),
    private val dispatchersProvider: DispatchersProvider = DispatchersProviderImpl(),
    private val productUseCase: ProductUseCase = ProductUseCaseImpl(productRepository),
    private val userUseCase: UserUseCase = UserUseCaseImpl(userRepository, userPreference)
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProductListViewModel(dispatchersProvider, productUseCase, userUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
