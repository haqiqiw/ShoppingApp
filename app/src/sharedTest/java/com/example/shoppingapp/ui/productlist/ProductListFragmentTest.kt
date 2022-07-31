package com.example.shoppingapp.ui.productlist

import androidx.fragment.app.Fragment
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.example.shoppingapp.R
import com.example.shoppingapp.core.util.FakeDispatchersProvider
import com.example.shoppingapp.domain.model.Product
import com.example.shoppingapp.domain.usecase.product.FakeProductUseCase
import com.example.shoppingapp.domain.usecase.user.FakeUserUseCase
import com.example.shoppingapp.ui.BaseFragmentTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.not
import org.junit.Test

@ExperimentalCoroutinesApi
class ProductListFragmentTest : BaseFragmentTest<ProductListViewModel>() {

    override fun initViewModel(): ProductListViewModel {
        return ProductListViewModel(
            FakeDispatchersProvider(testDispatcher),
            FakeProductUseCase(),
            FakeUserUseCase()
        )
    }

    override fun initFragment(testViewModelFactory: TestViewModelFactory): Fragment {
        return ProductListFragment(testViewModelFactory)
    }

    @Test
    fun `fragment should display initial view`() {
        // Given
        val uiState = ProductListUiState(listOf(), true)
        viewModel.injectUiState(uiState)

        // When
        scenario.onFragment {
            // Then
            onView(withId(R.id.swipeRefresh)).check(matches(isDisplayed()))
            onView(withId(R.id.progressBar)).check(matches(isDisplayed()))
            onView(withId(R.id.recyclerView)).check(matches(not(isDisplayed())))
        }
    }

    @Test
    fun `fragment should display view with data`() {
        // Given
        val product = Product(1, "Kaos", "Kaos Bagus", "image.png", "1.0", "5.0", 1, "Kaos")
        val uiState = ProductListUiState(listOf(product), false)
        viewModel.injectUiState(uiState)

        // When
        scenario.onFragment {
            // Then
            onView(withId(R.id.swipeRefresh)).check(matches(isDisplayed()))
            onView(withId(R.id.progressBar)).check(matches(not(isDisplayed())))
            onView(withId(R.id.recyclerView)).check(matches(isDisplayed()))
            onView(withId(R.id.tvName)).check(
                matches(allOf(isDisplayed(), withText(product.title)))
            )
            onView(withId(R.id.tvPrice)).check(
                matches(allOf(isDisplayed(), withText("$${product.price}")))
            )
            onView(withId(R.id.tvInfo)).check(
                matches(allOf(isDisplayed(), withText("★ ${product.rate} - ${product.category}")))
            )
        }
    }
}
