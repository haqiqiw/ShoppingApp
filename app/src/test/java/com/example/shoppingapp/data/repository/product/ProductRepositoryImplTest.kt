package com.example.shoppingapp.data.repository.product

import app.cash.turbine.test
import com.example.shoppingapp.core.util.Resource
import com.example.shoppingapp.data.local.dao.FakeProductDao
import com.example.shoppingapp.data.local.entity.ProductEntity
import com.example.shoppingapp.data.remote.dto.ProductDto
import com.example.shoppingapp.data.remote.dto.RatingDto
import com.example.shoppingapp.data.remote.dto.UpdateProductDto
import com.example.shoppingapp.data.remote.service.ProductService
import com.example.shoppingapp.domain.model.ProductForm
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
class ProductRepositoryImplTest {

    @get:Rule
    val testCoroutineRule: TestCoroutineRule = TestCoroutineRule()

    private lateinit var productRepository: ProductRepository

    private lateinit var productService: ProductService

    private lateinit var productDao: FakeProductDao

    @Before
    fun setUp() {
        productService = mockk(relaxed = true, relaxUnitFun = true)
        productDao = FakeProductDao()
        productRepository = ProductRepositoryImpl(productService, productDao)
    }

    @After
    fun tearDown() {
        clearMocks(productService)
    }

    @Test
    fun `getProducts throws Exception is correct`() = testCoroutineRule.runTest {
        // Given
        productDao.getProductsResult = listOf(ProductEntity.createEmptyProduct())
        coEvery { productService.getProducts() } throws Exception("Error")

        // When
        productRepository.getProducts(false).test {
            // Then
            awaitItem() as Resource.Loading
            with(awaitItem() as Resource.Error) {
                assertThat(message).isEqualTo("Error")
            }
            with(awaitItem() as Resource.Success) {
                assertThat(data?.size).isEqualTo(1)
            }
            awaitComplete()
        }
    }

    @Test
    fun `getProducts error is correct`() = testCoroutineRule.runTest {
        // Given
        productDao.getProductsResult = listOf(ProductEntity.createEmptyProduct())
        coEvery { productService.getProducts() } returns Response.error(500, mockk(relaxed = true))

        // When
        productRepository.getProducts(false).test {
            // Then
            awaitItem() as Resource.Loading
            with(awaitItem() as Resource.Error) {
                assertThat(message).isEqualTo("Response.error()")
            }
            with(awaitItem() as Resource.Success) {
                assertThat(data?.size).isEqualTo(1)
            }
            awaitComplete()
        }
    }

    @Test
    fun `getProducts success is correct`() = testCoroutineRule.runTest {
        // Given
        productDao.getProductsResult = listOf(ProductEntity.createEmptyProduct())
        coEvery { productService.getProducts() } returns Response.success(
            listOf(ProductDto(1, "Kaos", "Kaos Bagus", "image.png", 1.0, RatingDto(1, 5.0), "Kaos"))
        )

        // When
        productRepository.getProducts(false).test {
            // Then
            awaitItem() as Resource.Loading
            with(awaitItem() as Resource.Success) {
                assertThat(data?.size).isEqualTo(1)
            }
            awaitComplete()
        }
    }

    @Test
    fun `getProducts isCached success is correct`() = testCoroutineRule.runTest {
        // Given
        productDao.getProductsResult = listOf(ProductEntity.createEmptyProduct())

        // When
        productRepository.getProducts(true).test {
            // Then
            awaitItem() as Resource.Loading
            with(awaitItem() as Resource.Success) {
                assertThat(data?.size).isEqualTo(1)
            }
            awaitComplete()
        }
    }

    @Test
    fun `updateProduct throws Exception is correct`() = testCoroutineRule.runTest {
        // Given
        coEvery { productService.udpateProduct(any(), any()) } throws Exception("Error")

        // When
        productRepository.updateProduct(1, ProductForm("Kaos", "Kaos Bagus", "image.png", 1.0, "Kaos")).test {
            // Then
            awaitItem() as Resource.Loading
            with(awaitItem() as Resource.Error) {
                assertThat(message).isEqualTo("Error")
            }
            awaitComplete()
        }
    }

    @Test
    fun `updateProduct error is correct`() = testCoroutineRule.runTest {
        // Given
        coEvery { productService.udpateProduct(any(), any()) } returns Response.error(500, mockk(relaxed = true))

        // When
        productRepository.updateProduct(1, ProductForm("Kaos", "Kaos Bagus", "image.png", 1.0, "Kaos")).test {
            // Then
            awaitItem() as Resource.Loading
            with(awaitItem() as Resource.Error) {
                assertThat(message).isEqualTo("Response.error()")
            }
            awaitComplete()
        }
    }

    @Test
    fun `updateProduct success is correct`() = testCoroutineRule.runTest {
        // Given
        productDao.getProductByIdResult = ProductEntity.createEmptyProduct()
        coEvery { productService.udpateProduct(any(), any()) } returns Response.success(
            UpdateProductDto(1, "Kaos", "Kaos Bagus", "image.png", 1.0, "Kaos")
        )

        // When
        productRepository.updateProduct(1, ProductForm("Kaos", "Kaos Bagus", "image.png", 1.0, "Kaos")).test {
            // Then
            awaitItem() as Resource.Loading
            with(awaitItem() as Resource.Success) {
                assertThat(data).isNotNull()
            }
            awaitComplete()
        }
    }
}
