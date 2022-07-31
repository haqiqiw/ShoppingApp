package com.example.shoppingapp.ui.login

import androidx.fragment.app.Fragment
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.example.shoppingapp.R
import com.example.shoppingapp.core.util.FakeDispatchersProvider
import com.example.shoppingapp.domain.model.LoginForm
import com.example.shoppingapp.domain.usecase.user.FakeUserUseCase
import com.example.shoppingapp.ui.BaseFragmentTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test

@ExperimentalCoroutinesApi
class LoginFragmentTest : BaseFragmentTest<LoginViewModel>() {

    override fun initViewModel(): LoginViewModel {
        return LoginViewModel(
            FakeDispatchersProvider(testDispatcher),
            FakeUserUseCase()
        )
    }

    override fun initFragment(testViewModelFactory: TestViewModelFactory): Fragment {
        return LoginFragment(testViewModelFactory)
    }

    @Test
    fun `fragment should display initial view`() {
        // Given
        val uiState = LoginUiState()
        viewModel.injectUiState(uiState)

        // When
        scenario.onFragment {
            // Then
            onView(withId(R.id.etUsername)).check(matches(isDisplayed()))
            onView(withId(R.id.etPassword)).check(matches(isDisplayed()))
            onView(withId(R.id.btnLogin)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun `fragment should display view with data`() {
        // Given
        val loginForm = LoginForm("wawan", "knalpot")
        val uiState = LoginUiState(loginForm)
        viewModel.injectUiState(uiState)

        // When
        scenario.onFragment {
            // Then
            onView(withId(R.id.etUsername)).check(matches(isDisplayed()))
            onView(withText(loginForm.username)).check(matches(isDisplayed()))
            onView(withId(R.id.etPassword)).check(matches(isDisplayed()))
            onView(withText(loginForm.password)).check(matches(isDisplayed()))
            onView(withId(R.id.btnLogin)).check(matches(isDisplayed()))
        }
    }
}
