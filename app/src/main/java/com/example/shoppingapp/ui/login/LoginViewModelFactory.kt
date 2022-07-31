package com.example.shoppingapp.ui.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shoppingapp.core.util.DispatchersProvider
import com.example.shoppingapp.core.util.DispatchersProviderImpl
import com.example.shoppingapp.data.preference.UserPreference
import com.example.shoppingapp.data.remote.service.ServiceBuilder
import com.example.shoppingapp.data.remote.service.UserService
import com.example.shoppingapp.data.repository.user.UserRepository
import com.example.shoppingapp.data.repository.user.UserRepositoryImpl
import com.example.shoppingapp.domain.usecase.user.UserUseCase
import com.example.shoppingapp.domain.usecase.user.UserUseCaseImpl

class LoginViewModelFactory(
    context: Context,
    userService: UserService = ServiceBuilder.userService,
    userPreference: UserPreference = UserPreference.create(context),
    userRepository: UserRepository = UserRepositoryImpl(userService, userPreference),
    private val dispatchersProvider: DispatchersProvider = DispatchersProviderImpl(),
    private val userUseCase: UserUseCase = UserUseCaseImpl(userRepository, userPreference)

) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(dispatchersProvider, userUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
