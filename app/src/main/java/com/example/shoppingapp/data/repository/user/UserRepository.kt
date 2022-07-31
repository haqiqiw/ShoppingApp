package com.example.shoppingapp.data.repository.user

import com.example.shoppingapp.core.util.Resource
import com.example.shoppingapp.data.remote.dto.LoginDto
import com.example.shoppingapp.domain.model.LoginForm
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun userLogin(body: LoginForm): Flow<Resource<LoginDto>>
}
