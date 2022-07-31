package com.example.shoppingapp.ui.productdetail

import androidx.fragment.app.Fragment
import androidx.lifecycle.SavedStateHandle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.example.shoppingapp.R
import com.example.shoppingapp.core.util.FakeDispatchersProvider
import com.example.shoppingapp.domain.model.Product
import com.example.shoppingapp.domain.usecase.product.FakeProductUseCase
import com.example.shoppingapp.ui.BaseFragmentTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.Matchers.allOf
import org.junit.Test

@ExperimentalCoroutinesApi
class ProductDetailFragmentTest : BaseFragmentTest<ProductDetailViewModel>() {

    override fun initViewModel(): ProductDetailViewModel {
        return ProductDetailViewModel(
            FakeDispatchersProvider(testDispatcher),
            FakeProductUseCase(),
            SavedStateHandle.createHandle(null, null)
        )
    }

    override fun initFragment(testViewModelFactory: TestViewModelFactory): Fragment {
        return ProductDetailFragment(testViewModelFactory)
    }

    @Test
    fun `fragment should display initial view`() {
        // Given
        val uiState = ProductDetailUiState()
        viewModel.injectUiState(uiState)

        // When
        scenario.onFragment {
            // Then
            onView(withId(R.id.ivThumbnail)).check(matches(isDisplayed()))
            onView(withId(R.id.etId)).check(matches(isDisplayed()))
            onView(withId(R.id.etTitle)).check(
                matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))
            )
            onView(withId(R.id.etPrice)).check(
                matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))
            )
            onView(withId(R.id.etCategory)).check(
                matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))
            )
            onView(withId(R.id.etDescription)).check(
                matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))
            )
            onView(withId(R.id.btnEdit)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun `fragment should display view with data`() {
        // Given
        val product = Product(1, "Kaos", "Kaos Bagus", "image.png", "1.0", "5.0", 1, "Kaos")
        val uiState = ProductDetailUiState(product)
        viewModel.injectUiState(uiState)

        // When
        scenario.onFragment {
            // Then
            onView(withId(R.id.ivThumbnail)).check(matches(isDisplayed()))
            onView(withId(R.id.etId)).check(
                matches(
                    allOf(
                        withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE),
                        withText(product.id.toString())
                    )
                )
            )
            onView(withId(R.id.etTitle)).check(
                matches(
                    allOf(
                        withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE),
                        withText(product.title)
                    )
                )
            )
            onView(withId(R.id.etPrice)).check(
                matches(
                    allOf(
                        withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE),
                        withText(product.price)
                    )
                )
            )
            onView(withId(R.id.etCategory)).check(
                matches(
                    allOf(
                        withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE),
                        withText(product.category)
                    )
                )
            )
            onView(withId(R.id.etDescription)).check(
                matches(
                    allOf(
                        withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE),
                        withText(product.description)
                    )
                )
            )
            onView(withId(R.id.btnEdit)).check(matches(isDisplayed()))
        }
    }
}
