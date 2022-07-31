package com.example.shoppingapp.domain.usecase.user

import app.cash.turbine.test
import com.example.shoppingapp.core.util.Resource
import com.example.shoppingapp.data.preference.UserPreference
import com.example.shoppingapp.data.preference.base.FakeBasePreference
import com.example.shoppingapp.data.remote.dto.LoginDto
import com.example.shoppingapp.data.repository.user.FakeUserRepository
import com.example.shoppingapp.domain.model.LoginForm
import com.example.shoppingapp.rule.TestCoroutineRule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class UserUseCaseImplTest {

    @get:Rule
    val testCoroutineRule: TestCoroutineRule = TestCoroutineRule()

    private lateinit var userUseCase: UserUseCase

    private lateinit var userRepository: FakeUserRepository

    private lateinit var userPreference: UserPreference

    @Before
    fun setUp() {
        userRepository = FakeUserRepository()
        userPreference = UserPreference(FakeBasePreference())
        userUseCase = UserUseCaseImpl(userRepository, userPreference)
    }

    @Test
    fun `userLogin is correct`() = testCoroutineRule.runTest {
        // Given
        val mockResource = Resource.Success(LoginDto("abcde"))
        userRepository.userLoginFlow = flow { emit(mockResource) }

        // When
        userUseCase.userLogin(LoginForm("wawan", "knalpot")).test {
            // Then
            with(awaitItem() as Resource.Success) {
                assertThat(data?.token).isEqualTo("abcde")
            }
            awaitComplete()
        }
    }

    @Test
    fun `userLogout is correct`() = testCoroutineRule.runTest {
        // Given
        userPreference.userToken = "abcde"

        // When
        userUseCase.userLogout().test {
            // Then
            awaitItem() as Resource.Loading
            with(awaitItem() as Resource.Success) {
                assertThat(userPreference.userToken).isEmpty()
            }
            awaitComplete()
        }
    }
}