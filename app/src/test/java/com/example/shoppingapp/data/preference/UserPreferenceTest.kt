package com.example.shoppingapp.data.preference

import com.example.shoppingapp.data.preference.base.FakeBasePreference
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class UserPreferenceTest {

    private lateinit var userPreference: UserPreference

    @Before
    fun setUp() {
        userPreference = UserPreference(FakeBasePreference())
    }

    @Test
    fun `userToken set is correct`() {
        // Given
        val mockUserToken = "abcde"

        // When
        userPreference.userToken = mockUserToken

        // Then
        assertThat(userPreference.userToken).isEqualTo(mockUserToken)
    }

    @Test
    fun `userToken get is correct`() {
        // Given
        val mockUserToken = "abcde"
        userPreference.userToken = mockUserToken

        // When
        val result = userPreference.userToken

        // Then
        assertThat(result).isEqualTo(mockUserToken)
    }
}
