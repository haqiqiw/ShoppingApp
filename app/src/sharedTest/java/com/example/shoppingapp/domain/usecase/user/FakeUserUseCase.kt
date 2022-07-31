package com.example.shoppingapp.domain.usecase.user

import com.example.shoppingapp.core.util.Resource
import com.example.shoppingapp.data.remote.dto.LoginDto
import com.example.shoppingapp.domain.model.LoginForm
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeUserUseCase : UserUseCase {

    var userLoginFlow = flow<Resource<LoginDto>> {}
    override suspend fun userLogin(body: LoginForm): Flow<Resource<LoginDto>> {
        return userLoginFlow
    }

    var userLogoutFlow = flow<Resource<Unit>> {}
    override suspend fun userLogout(): Flow<Resource<Unit>> {
        return userLogoutFlow
    }
}
