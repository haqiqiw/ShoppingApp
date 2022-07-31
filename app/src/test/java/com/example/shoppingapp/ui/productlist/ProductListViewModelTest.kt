package com.example.shoppingapp.ui.productlist

import app.cash.turbine.test
import com.example.shoppingapp.core.util.FakeDispatchersProvider
import com.example.shoppingapp.core.util.Resource
import com.example.shoppingapp.domain.model.Product
import com.example.shoppingapp.domain.usecase.product.FakeProductUseCase
import com.example.shoppingapp.domain.usecase.user.FakeUserUseCase
import com.example.shoppingapp.rule.TestCoroutineRule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ProductListViewModelTest {

    @get:Rule
    val testCoroutineRule: TestCoroutineRule = TestCoroutineRule()

    private lateinit var productListViewModel: ProductListViewModel

    private lateinit var dispatchersProvider: FakeDispatchersProvider

    private lateinit var productUseCase: FakeProductUseCase

    private lateinit var userUseCase: FakeUserUseCase

    @Before
    fun setUp() {
        dispatchersProvider = FakeDispatchersProvider(testCoroutineRule.getTestDispatcher())
        productUseCase = FakeProductUseCase()
        userUseCase = FakeUserUseCase()
        productListViewModel = ProductListViewModel(dispatchersProvider, productUseCase, userUseCase)
    }

    @Test
    fun `fetchProducts loading is correct`() = testCoroutineRule.runTest {
        // Given
        val resource = Resource.Loading
        productUseCase.getProductsFlow = flow {
            emit(resource)
        }

        // When
        productListViewModel.fetchProducts()
        advanceUntilIdle()

        // then
        with(productListViewModel.uiState.value) {
            assertThat(products.size).isEqualTo(0)
            assertThat(isLoading).isEqualTo(true)
        }
    }

    @Test
    fun `fetchProducts success is correct`() = testCoroutineRule.runTest {
        // Given
        val resource = Resource.Success(listOf(Product.createEmptyProduct()))
        productUseCase.getProductsFlow = flow {
            emit(resource)
        }

        // When
        productListViewModel.fetchProducts()
        advanceUntilIdle()

        // then
        with(productListViewModel.uiState.value) {
            assertThat(products.size).isEqualTo(1)
            assertThat(isLoading).isEqualTo(false)
        }
    }

    @Test
    fun `fetchProducts error is correct`() = testCoroutineRule.runTest {
        // Given
        val resource = Resource.Error<List<Product>>("Error")
        productUseCase.getProductsFlow = flow {
            emit(resource)
        }

        // When
        val job = launch {
            productListViewModel.fetchProducts()
            advanceUntilIdle()
        }

        // Then
        productListViewModel.uiEvent.test {
            job.join()
            with(awaitItem() as ProductListUiEvent.ShowSnackbar) {
                assertThat(message).isEqualTo("Error")
            }
            cancelAndIgnoreRemainingEvents()
            with(productListViewModel.uiState.value) {
                assertThat(products.size).isEqualTo(0)
                assertThat(isLoading).isEqualTo(false)
            }
            job.cancel()
        }
    }

    @Test
    fun `onLogout loading is correct`() = testCoroutineRule.runTest {
        // Given
        val resource = Resource.Loading
        userUseCase.userLogoutFlow = flow {
            emit(resource)
        }

        // When
        val job = launch {
            productListViewModel.onLogout()
        }

        // Then
        productListViewModel.uiEvent.test {
            job.join()
            awaitItem() as ProductListUiEvent.ShowLoadingDialog
            job.cancel()
        }
    }

    @Test
    fun `onLogout success is correct`() = testCoroutineRule.runTest {
        // Given
        val resource = Resource.Success(Unit)
        userUseCase.userLogoutFlow = flow {
            emit(resource)
        }

        // When
        val job = launch {
            productListViewModel.onLogout()
        }

        // Then
        productListViewModel.uiEvent.test {
            job.join()
            awaitItem() as ProductListUiEvent.DismissLoadingDialog
            awaitItem() as ProductListUiEvent.GoToLogin
            job.cancel()
        }
    }

    @Test
    fun `onLogout error is correct`() = testCoroutineRule.runTest {
        // Given
        val resource = Resource.Error<Unit>("Error")
        userUseCase.userLogoutFlow = flow {
            emit(resource)
        }

        // When
        val job = launch {
            productListViewModel.onLogout()
        }

        // Then
        productListViewModel.uiEvent.test {
            job.join()
            awaitItem() as ProductListUiEvent.DismissLoadingDialog
            job.cancel()
        }
    }

    @Test
    fun `onUiEvent is correct`() = testCoroutineRule.runTest {
        // When
        val job = launch {
            productListViewModel.onEvent(ProductListUiEvent.GoToLogin)
        }

        // Then
        productListViewModel.uiEvent.test {
            job.join()
            awaitItem() as ProductListUiEvent.GoToLogin
            job.cancel()
        }
    }
}
