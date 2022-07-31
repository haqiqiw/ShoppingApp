package com.example.shoppingapp.ui.productdetail

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.example.shoppingapp.core.util.FakeDispatchersProvider
import com.example.shoppingapp.core.util.Resource
import com.example.shoppingapp.domain.model.Product
import com.example.shoppingapp.domain.usecase.product.FakeProductUseCase
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
class ProductDetailViewModelTest {

    @get:Rule
    val testCoroutineRule: TestCoroutineRule = TestCoroutineRule()

    private lateinit var productDetailViewModel: ProductDetailViewModel

    private lateinit var dispatchersProvider: FakeDispatchersProvider

    private lateinit var productUseCase: FakeProductUseCase

    private lateinit var savedStateHandle: SavedStateHandle

    @Before
    fun setUp() {
        dispatchersProvider = FakeDispatchersProvider(testCoroutineRule.getTestDispatcher())
        productUseCase = FakeProductUseCase()
        savedStateHandle = SavedStateHandle.createHandle(null, null)
        productDetailViewModel = ProductDetailViewModel(dispatchersProvider, productUseCase, savedStateHandle)
    }

    @Test
    fun `initUiState is correct`() = testCoroutineRule.runTest {
        // Given
        val product = Product(1, "Kaos", "Kaos Bagus", "image.png", "1.0", "5.0", 1, "Kaos")

        // When
        productDetailViewModel.initUiState(product)
        advanceUntilIdle()

        // then
        with(productDetailViewModel.uiState.value.product) {
            assertThat(id).isEqualTo(product.id)
            assertThat(category).isEqualTo(product.category)
            assertThat(description).isEqualTo(product.description)
            assertThat(image).isEqualTo(product.image)
            assertThat(price).isEqualTo(product.price)
            assertThat(title).isEqualTo(product.title)
        }
    }

    @Test
    fun `onEditProduct title empty is correct`() = testCoroutineRule.runTest {
        // When
        val job = launch {
            productDetailViewModel.onEditProduct("", "1.0", "Kaos", "Kaos Bagus")
        }

        // Then
        productDetailViewModel.uiEvent.test {
            job.join()
            with(awaitItem() as ProductDetailUiEvent.ShowSnackbar) {
                assertThat(message).isEqualTo("Product title cannot be empty.")
            }
            with(productDetailViewModel.uiState.value.product) {
                assertThat(title).isEmpty()
                assertThat(price).isEqualTo("1.0")
                assertThat(category).isEqualTo("Kaos")
                assertThat(description).isEqualTo("Kaos Bagus")
            }
            job.cancel()
        }
    }

    @Test
    fun `onEditProduct price empty is correct`() = testCoroutineRule.runTest {
        // When
        val job = launch {
            productDetailViewModel.onEditProduct("Kaos", "", "Kaos", "Kaos Bagus")
        }

        // Then
        productDetailViewModel.uiEvent.test {
            job.join()
            with(awaitItem() as ProductDetailUiEvent.ShowSnackbar) {
                assertThat(message).isEqualTo("Product price cannot be empty.")
            }
            with(productDetailViewModel.uiState.value.product) {
                assertThat(title).isEqualTo("Kaos")
                assertThat(price).isEmpty()
                assertThat(category).isEqualTo("Kaos")
                assertThat(description).isEqualTo("Kaos Bagus")
            }
            job.cancel()
        }
    }

    @Test
    fun `onEditProduct category empty is correct`() = testCoroutineRule.runTest {
        // When
        val job = launch {
            productDetailViewModel.onEditProduct("Kaos", "1.0", "", "Kaos Bagus")
        }

        // Then
        productDetailViewModel.uiEvent.test {
            job.join()
            with(awaitItem() as ProductDetailUiEvent.ShowSnackbar) {
                assertThat(message).isEqualTo("Product category cannot be empty.")
            }
            with(productDetailViewModel.uiState.value.product) {
                assertThat(title).isEqualTo("Kaos")
                assertThat(price).isEqualTo("1.0")
                assertThat(category).isEmpty()
                assertThat(description).isEqualTo("Kaos Bagus")
            }
            job.cancel()
        }
    }

    @Test
    fun `onEditProduct description empty is correct`() = testCoroutineRule.runTest {
        // When
        val job = launch {
            productDetailViewModel.onEditProduct("Kaos", "1.0", "Kaos", "")
        }

        // Then
        productDetailViewModel.uiEvent.test {
            job.join()
            with(awaitItem() as ProductDetailUiEvent.ShowSnackbar) {
                assertThat(message).isEqualTo("Product description cannot be empty.")
            }
            with(productDetailViewModel.uiState.value.product) {
                assertThat(title).isEqualTo("Kaos")
                assertThat(price).isEqualTo("1.0")
                assertThat(category).isEqualTo("Kaos")
                assertThat(description).isEmpty()
            }
            job.cancel()
        }
    }

    @Test
    fun `onEditProduct loading is correct`() = testCoroutineRule.runTest {
        // Given
        val resource = Resource.Loading
        productUseCase.updateProductFlow = flow {
            emit(resource)
        }

        // When
        val job = launch {
            productDetailViewModel.onEditProduct("Kaos", "1.0", "Kaos", "Kaos Bagus")
        }

        // Then
        productDetailViewModel.uiEvent.test {
            job.join()
            awaitItem() as ProductDetailUiEvent.ShowLoadingDialog
            with(productDetailViewModel.uiState.value.product) {
                assertThat(title).isEqualTo("Kaos")
                assertThat(price).isEqualTo("1.0")
                assertThat(category).isEqualTo("Kaos")
                assertThat(description).isEqualTo("Kaos Bagus")
            }
            job.cancel()
        }
    }

    @Test
    fun `onEditProduct success is correct`() = testCoroutineRule.runTest {
        // Given
        val resource = Resource.Success(Product.createEmptyProduct())
        productUseCase.updateProductFlow = flow {
            emit(resource)
        }

        // When
        val job = launch {
            productDetailViewModel.onEditProduct("Kaos", "1.0", "Kaos", "Kaos Bagus")
        }

        // Then
        productDetailViewModel.uiEvent.test {
            job.join()
            awaitItem() as ProductDetailUiEvent.DismissLoadingDialog
            awaitItem() as ProductDetailUiEvent.GoToProductList
            with(productDetailViewModel.uiState.value.product) {
                assertThat(title).isEqualTo("Kaos")
                assertThat(price).isEqualTo("1.0")
                assertThat(category).isEqualTo("Kaos")
                assertThat(description).isEqualTo("Kaos Bagus")
            }
            job.cancel()
        }
    }

    @Test
    fun `onEditProduct error is correct`() = testCoroutineRule.runTest {
        // Given
        val resource = Resource.Error<Product>("Error")
        productUseCase.updateProductFlow = flow {
            emit(resource)
        }

        // When
        val job = launch {
            productDetailViewModel.onEditProduct("Kaos", "1.0", "Kaos", "Kaos Bagus")
        }

        // Then
        productDetailViewModel.uiEvent.test {
            job.join()
            awaitItem() as ProductDetailUiEvent.DismissLoadingDialog
            with(awaitItem() as ProductDetailUiEvent.ShowSnackbar) {
                assertThat(message).isEqualTo("Error")
            }
            with(productDetailViewModel.uiState.value.product) {
                assertThat(title).isEqualTo("Kaos")
                assertThat(price).isEqualTo("1.0")
                assertThat(category).isEqualTo("Kaos")
                assertThat(description).isEqualTo("Kaos Bagus")
            }
            job.cancel()
        }
    }

    @Test
    fun `onUiEvent is correct`() = testCoroutineRule.runTest {
        // When
        val job = launch {
            productDetailViewModel.onEvent(ProductDetailUiEvent.GoToProductList)
        }

        // Then
        productDetailViewModel.uiEvent.test {
            job.join()
            awaitItem() as ProductDetailUiEvent.GoToProductList
            job.cancel()
        }
    }
}
