package com.example.shoppingapp.ui.login

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingapp.core.util.DispatchersProvider
import com.example.shoppingapp.core.util.Resource
import com.example.shoppingapp.domain.model.LoginForm
import com.example.shoppingapp.domain.usecase.user.UserUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class LoginViewModel(
    private val dispatchers: DispatchersProvider,
    private val userUseCase: UserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<LoginUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    @VisibleForTesting
    fun injectUiState(uiState: LoginUiState) {
        _uiState.value = uiState
    }

    fun onLogin(username: String, password: String) {
        viewModelScope.launch(dispatchers.io) {
            val loginForm = LoginForm(username, password)
            _uiState.value = uiState.value.copy(loginForm = loginForm)

            if (!validateLogin(username, password)) return@launch

            userUseCase.userLogin(loginForm)
                .onEach { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            onEvent(LoginUiEvent.ShowLoadingDialog)
                        }
                        is Resource.Success -> {
                            onEvent(LoginUiEvent.DismissLoadingDialog)
                            onEvent(LoginUiEvent.GoToProductList)
                        }
                        is Resource.Error -> {
                            onEvent(LoginUiEvent.DismissLoadingDialog)
                            onEvent(LoginUiEvent.ShowSnackbar(resource.message))
                        }
                    }
                }
                .launchIn(this)
        }
    }

    fun onEvent(event: LoginUiEvent) {
        viewModelScope.launch(dispatchers.default) {
            _uiEvent.emit(event)
        }
    }

    private suspend fun validateLogin(username: String, password: String): Boolean {
        var isValid = true
        when {
            username.isBlank() && password.isBlank() -> {
                isValid = false
                _uiEvent.emit(
                    LoginUiEvent.ShowSnackbar("Username and password cannot be empty.")
                )
            }
            username.isBlank() && password.isNotBlank() -> {
                isValid = false
                _uiEvent.emit(
                    LoginUiEvent.ShowSnackbar("Username cannot be empty.")
                )
            }
            username.isNotBlank() && password.isBlank() -> {
                isValid = false
                _uiEvent.emit(
                    LoginUiEvent.ShowSnackbar("Password cannot be empty.")
                )
            }
            password.length < 6 -> {
                isValid = false
                _uiEvent.emit(
                    LoginUiEvent.ShowSnackbar("Password must be at least 6 characters.")
                )
            }
        }
        return isValid
    }
}
