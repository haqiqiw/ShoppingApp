package com.example.shoppingapp.domain.usecase.product

import app.cash.turbine.test
import com.example.shoppingapp.core.util.Resource
import com.example.shoppingapp.data.repository.product.FakeProductRepository
import com.example.shoppingapp.domain.model.Product
import com.example.shoppingapp.domain.model.ProductForm
import com.example.shoppingapp.rule.TestCoroutineRule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ProductUseCaseImplTest {

    @get:Rule
    val testCoroutineRule: TestCoroutineRule = TestCoroutineRule()

    private lateinit var productUseCase: ProductUseCase

    private lateinit var productRepository: FakeProductRepository

    @Before
    fun setUp() {
        productRepository = FakeProductRepository()
        productUseCase = ProductUseCaseImpl(productRepository)
    }

    @Test
    fun `getProducts is correct`() = testCoroutineRule.runTest {
        // Given
        val mockResource = Resource.Success(
            listOf(Product.createEmptyProduct(), Product.createEmptyProduct())
        )
        productRepository.getProductsFlow = flow { emit(mockResource) }

        // When
        productUseCase.getProducts(false).test {
            // Then
            with(awaitItem() as Resource.Success) {
                assertThat(data?.size).isEqualTo(2)
            }
            awaitComplete()
        }
    }

    @Test
    fun `updateProduct is correct`() = testCoroutineRule.runTest {
        // Given
        val mockResource = Resource.Success(Product.createEmptyProduct())
        productRepository.updateProductFlow = flow { emit(mockResource) }

        // When
        productUseCase.updateProduct(1, ProductForm("Kaos", "Kaos Bagus", "image.png", 1.0, "Kaos")).test {
            // Then
            with(awaitItem() as Resource.Success) {
                assertThat(data).isNotNull()
            }
            awaitComplete()
        }
    }
}
