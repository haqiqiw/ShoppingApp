package com.example.shoppingapp.data.remote.service

import com.example.shoppingapp.data.remote.dto.ProductDto
import com.example.shoppingapp.data.remote.dto.UpdateProductDto
import com.example.shoppingapp.domain.model.ProductForm
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

interface ProductService {
    @GET("/products")
    suspend fun getProducts(): Response<List<ProductDto>>

    @PATCH("products/{id}")
    suspend fun udpateProduct(@Path("id") id: Int, @Body body: ProductForm): Response<UpdateProductDto>
}
