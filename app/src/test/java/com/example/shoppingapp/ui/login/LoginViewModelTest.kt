package com.example.shoppingapp.ui.login

import app.cash.turbine.test
import com.example.shoppingapp.core.util.FakeDispatchersProvider
import com.example.shoppingapp.core.util.Resource
import com.example.shoppingapp.data.remote.dto.LoginDto
import com.example.shoppingapp.domain.usecase.user.FakeUserUseCase
import com.example.shoppingapp.rule.TestCoroutineRule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class LoginViewModelTest {

    @get:Rule
    val testCoroutineRule: TestCoroutineRule = TestCoroutineRule()

    private lateinit var loginViewModel: LoginViewModel

    private lateinit var dispatchersProvider: FakeDispatchersProvider

    private lateinit var userUseCase: FakeUserUseCase

    @Before
    fun setUp() {
        dispatchersProvider = FakeDispatchersProvider(testCoroutineRule.getTestDispatcher())
        userUseCase = FakeUserUseCase()
        loginViewModel = LoginViewModel(dispatchersProvider, userUseCase)
    }

    @Test
    fun `onLogin username and password empty is correct`() = testCoroutineRule.runTest {
        // When
        val job = launch {
            loginViewModel.onLogin("", "")
        }

        // Then
        loginViewModel.uiEvent.test {
            job.join()
            with(awaitItem() as LoginUiEvent.ShowSnackbar) {
                assertThat(message).isEqualTo("Username and password cannot be empty.")
            }
            with(loginViewModel.uiState.value.loginForm) {
                assertThat(username).isEmpty()
                assertThat(password).isEmpty()
            }
            job.cancel()
        }
    }

    @Test
    fun `onLogin username empty is correct`() = testCoroutineRule.runTest {
        // When
        val job = launch {
            loginViewModel.onLogin("", "knalpot")
        }

        // Then
        loginViewModel.uiEvent.test {
            job.join()
            with(awaitItem() as LoginUiEvent.ShowSnackbar) {
                assertThat(message).isEqualTo("Username cannot be empty.")
            }
            with(loginViewModel.uiState.value.loginForm) {
                assertThat(username).isEmpty()
                assertThat(password).isEqualTo("knalpot")
            }
            job.cancel()
        }
    }

    @Test
    fun `onLogin password empty is correct`() = testCoroutineRule.runTest {
        // When
        val job = launch {
            loginViewModel.onLogin("wawan", "")
        }

        // Then
        loginViewModel.uiEvent.test {
            job.join()
            with(awaitItem() as LoginUiEvent.ShowSnackbar) {
                assertThat(message).isEqualTo("Password cannot be empty.")
            }
            with(loginViewModel.uiState.value.loginForm) {
                assertThat(username).isEqualTo("wawan")
                assertThat(password).isEmpty()
            }
            job.cancel()
        }
    }

    @Test
    fun `onLogin password length less than 6 is correct`() = testCoroutineRule.runTest {
        // When
        val job = launch {
            loginViewModel.onLogin("wawan", "kna")
        }

        // Then
        loginViewModel.uiEvent.test {
            job.join()
            with(awaitItem() as LoginUiEvent.ShowSnackbar) {
                assertThat(message).isEqualTo("Password must be at least 6 characters.")
            }
            with(loginViewModel.uiState.value.loginForm) {
                assertThat(username).isEqualTo("wawan")
                assertThat(password).isEqualTo("kna")
            }
            job.cancel()
        }
    }

    @Test
    fun `onLogin loading is correct`() = testCoroutineRule.runTest {
        // Given
        val resource = Resource.Loading
        userUseCase.userLoginFlow = flow {
            emit(resource)
        }

        // When
        val job = launch {
            loginViewModel.onLogin("wawan", "knalpot")
        }

        // Then
        loginViewModel.uiEvent.test {
            job.join()
            awaitItem() as LoginUiEvent.ShowLoadingDialog
            with(loginViewModel.uiState.value.loginForm) {
                assertThat(username).isEqualTo("wawan")
                assertThat(password).isEqualTo("knalpot")
            }
            job.cancel()
        }
    }

    @Test
    fun `onLogin success is correct`() = testCoroutineRule.runTest {
        // Given
        val resource = Resource.Success(LoginDto("abcde"))
        userUseCase.userLoginFlow = flow {
            emit(resource)
        }

        // When
        val job = launch {
            loginViewModel.onLogin("wawan", "knalpot")
        }

        // Then
        loginViewModel.uiEvent.test {
            job.join()
            awaitItem() as LoginUiEvent.DismissLoadingDialog
            awaitItem() as LoginUiEvent.GoToProductList
            with(loginViewModel.uiState.value.loginForm) {
                assertThat(username).isEqualTo("wawan")
                assertThat(password).isEqualTo("knalpot")
            }
            job.cancel()
        }
    }

    @Test
    fun `onLogin error is correct`() = testCoroutineRule.runTest {
        // Given
        val resource = Resource.Error<LoginDto>("Error")
        userUseCase.userLoginFlow = flow {
            emit(resource)
        }

        // When
        val job = launch {
            loginViewModel.onLogin("wawan", "knalpot")
        }

        // Then
        loginViewModel.uiEvent.test {
            job.join()
            awaitItem() as LoginUiEvent.DismissLoadingDialog
            with(awaitItem() as LoginUiEvent.ShowSnackbar) {
                assertThat(message).isEqualTo("Error")
            }
            with(loginViewModel.uiState.value.loginForm) {
                assertThat(username).isEqualTo("wawan")
                assertThat(password).isEqualTo("knalpot")
            }
            job.cancel()
        }
    }

    @Test
    fun `onUiEvent is correct`() = testCoroutineRule.runTest {
        // When
        val job = launch {
            loginViewModel.onEvent(LoginUiEvent.GoToProductList)
        }

        // Then
        loginViewModel.uiEvent.test {
            job.join()
            awaitItem() as LoginUiEvent.GoToProductList
            job.cancel()
        }
    }
}
