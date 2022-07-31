package com.example.shoppingapp.data.repository.user

import com.example.shoppingapp.core.util.Resource
import com.example.shoppingapp.data.preference.UserPreference
import com.example.shoppingapp.data.remote.dto.LoginDto
import com.example.shoppingapp.data.remote.service.UserService
import com.example.shoppingapp.data.remote.service.getMessage
import com.example.shoppingapp.data.remote.service.isSuccess
import com.example.shoppingapp.domain.model.LoginForm
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UserRepositoryImpl(
    private val userService: UserService,
    private val userPreference: UserPreference
) : UserRepository {
    override suspend fun userLogin(body: LoginForm): Flow<Resource<LoginDto>> = flow {
        emit(Resource.Loading)
        try {
            val response = userService.userLogin(body)
            if (response.isSuccess()) {
                val responseBody = response.body()
                responseBody?.token?.let { token ->
                    userPreference.userToken = token
                }
                emit(Resource.Success(responseBody))
            } else {
                emit(Resource.createError(response.getMessage()))
            }
        } catch (e: Exception) {
            emit(Resource.createError(e))
        }
    }
}
