package com.example.shoppingapp.domain.usecase.user

import com.example.shoppingapp.core.util.Resource
import com.example.shoppingapp.data.preference.UserPreference
import com.example.shoppingapp.data.remote.dto.LoginDto
import com.example.shoppingapp.data.repository.user.UserRepository
import com.example.shoppingapp.domain.model.LoginForm
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UserUseCaseImpl(
    private val userRepository: UserRepository,
    private val userPreference: UserPreference
) : UserUseCase {
    override suspend fun userLogin(body: LoginForm): Flow<Resource<LoginDto>> {
        return userRepository.userLogin(body)
    }

    override suspend fun userLogout(): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)

        delay(1000L)
        userPreference.userToken = ""
        emit(Resource.Success(Unit))
    }
}
