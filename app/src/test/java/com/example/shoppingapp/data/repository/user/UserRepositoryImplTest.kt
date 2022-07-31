package com.example.shoppingapp.data.repository.user

import app.cash.turbine.test
import com.example.shoppingapp.core.util.Resource
import com.example.shoppingapp.data.preference.UserPreference
import com.example.shoppingapp.data.preference.base.FakeBasePreference
import com.example.shoppingapp.data.remote.dto.LoginDto
import com.example.shoppingapp.data.remote.service.UserService
import com.example.shoppingapp.domain.model.LoginForm
import com.example.shoppingapp.rule.TestCoroutineRule
import com.google.common.truth.Truth.assertThat
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

@ExperimentalCoroutinesApi
class UserRepositoryImplTest {

    @get:Rule
    val testCoroutineRule: TestCoroutineRule = TestCoroutineRule()

    private lateinit var userRepository: UserRepository

    private lateinit var userService: UserService

    private lateinit var userPreference: UserPreference

    @Before
    fun setUp() {
        userService = mockk(relaxed = true, relaxUnitFun = true)
        userPreference = UserPreference(FakeBasePreference())
        userRepository = UserRepositoryImpl(userService, userPreference)
    }

    @After
    fun tearDown() {
        clearMocks(userService)
    }

    @Test
    fun `userLogin throws Exception is correct`() = testCoroutineRule.runTest {
        // Given
        coEvery { userService.userLogin(any()) } throws Exception("Error")

        // When
        userRepository.userLogin(LoginForm("wawan", "knalpot")).test {
            // Then
            awaitItem() as Resource.Loading
            with(awaitItem() as Resource.Error) {
                assertThat(message).isEqualTo("Error")
            }
            awaitComplete()
        }
    }

    @Test
    fun `userLogin error is correct`() = testCoroutineRule.runTest {
        // Given
        coEvery { userService.userLogin(any()) } returns Response.error(500, mockk(relaxed = true))

        // When
        userRepository.userLogin(LoginForm("wawan", "knalpot")).test {
            // Then
            awaitItem() as Resource.Loading
            with(awaitItem() as Resource.Error) {
                assertThat(message).isEqualTo("Response.error()")
            }
            awaitComplete()
        }
    }

    @Test
    fun `userLogin success is correct`() = testCoroutineRule.runTest {
        // Given
        coEvery { userService.userLogin(any()) } returns Response.success((LoginDto("abcde")))

        // When
        userRepository.userLogin(LoginForm("wawan", "knalpot")).test {
            // Then
            awaitItem() as Resource.Loading
            with(awaitItem() as Resource.Success) {
                assertThat(data?.token).isEqualTo("abcde")
                assertThat(userPreference.userToken).isEqualTo("abcde")
            }
            awaitComplete()
        }
    }
}
