package com.example.shoppingapp.data.remote.service

import com.example.shoppingapp.data.remote.dto.LoginDto
import com.example.shoppingapp.domain.model.LoginForm
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {
    @POST("auth/login")
    suspend fun userLogin(@Body body: LoginForm): Response<LoginDto>
}
