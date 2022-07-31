package com.example.shoppingapp.data.remote.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceBuilder {

    private const val BASE_URL = "https://fakestoreapi.com/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
    }

    val userService: UserService by lazy {
        retrofit.create(UserService::class.java)
    }

    val productService: ProductService by lazy {
        retrofit.create(ProductService::class.java)
    }
}
