package com.example.shoppingapp.domain.usecase.user

import com.example.shoppingapp.core.util.Resource
import com.example.shoppingapp.data.remote.dto.LoginDto
import com.example.shoppingapp.domain.model.LoginForm
import kotlinx.coroutines.flow.Flow

interface UserUseCase {
    suspend fun userLogin(body: LoginForm): Flow<Resource<LoginDto>>

    suspend fun userLogout(): Flow<Resource<Unit>>
}
