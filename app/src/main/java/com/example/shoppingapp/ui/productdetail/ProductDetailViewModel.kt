package com.example.shoppingapp.ui.productdetail

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingapp.core.util.DispatchersProvider
import com.example.shoppingapp.core.util.Resource
import com.example.shoppingapp.domain.model.Product
import com.example.shoppingapp.domain.model.ProductForm
import com.example.shoppingapp.domain.usecase.product.ProductUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ProductDetailViewModel(
    private val dispatchers: DispatchersProvider,
    private val productUseCase: ProductUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductDetailUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<ProductDetailUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    init {
        savedStateHandle.getStateFlow(KEY_UI_STATE, ProductDetailUiState()).let {
            _uiState.value = it.value
        }
    }

    @VisibleForTesting
    fun injectUiState(uiState: ProductDetailUiState) {
        _uiState.value = uiState
    }

    fun initUiState(product: Product) {
        viewModelScope.launch(dispatchers.default) {
            val newUiState = uiState.value.copy(product = product)
            _uiState.emit(newUiState)
            savedStateHandle[KEY_UI_STATE] = newUiState
        }
    }

    fun onEditProduct(title: String, price: String, category: String, description: String) {
        viewModelScope.launch(dispatchers.io) {
            val newUiState = uiState.value.copy(
                product = uiState.value.product.copy(
                    title = title,
                    price = price,
                    category = category,
                    description = description
                )
            )
            _uiState.value = newUiState
            savedStateHandle[KEY_UI_STATE] = newUiState

            if (!validateProduct(title, price, category, description)) return@launch

            val productForm = ProductForm(
                category,
                description,
                uiState.value.image,
                price.toDoubleOrNull() ?: 0.0,
                title
            )
            val productId = uiState.value.id.toIntOrNull() ?: 0
            productUseCase.updateProduct(productId, productForm)
                .onEach { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            onEvent(ProductDetailUiEvent.ShowLoadingDialog)
                        }
                        is Resource.Success -> {
                            onEvent(ProductDetailUiEvent.DismissLoadingDialog)
                            onEvent(ProductDetailUiEvent.GoToProductList)
                        }
                        is Resource.Error -> {
                            onEvent(ProductDetailUiEvent.DismissLoadingDialog)
                            onEvent(ProductDetailUiEvent.ShowSnackbar(resource.message))
                        }
                    }
                }
                .launchIn(this)
        }
    }

    private suspend fun validateProduct(title: String, price: String, category: String, description: String): Boolean {
        var isValid = true
        when {
            title.isBlank() -> {
                isValid = false
                _uiEvent.emit(
                    ProductDetailUiEvent.ShowSnackbar("Product title cannot be empty.")
                )
            }
            price.isBlank() -> {
                isValid = false
                _uiEvent.emit(
                    ProductDetailUiEvent.ShowSnackbar("Product price cannot be empty.")
                )
            }
            category.isBlank() -> {
                isValid = false
                _uiEvent.emit(
                    ProductDetailUiEvent.ShowSnackbar("Product category cannot be empty.")
                )
            }
            description.isBlank() -> {
                isValid = false
                _uiEvent.emit(
                    ProductDetailUiEvent.ShowSnackbar("Product description cannot be empty.")
                )
            }
        }
        return isValid
    }

    fun onEvent(event: ProductDetailUiEvent) {
        viewModelScope.launch(dispatchers.default) {
            _uiEvent.emit(event)
        }
    }

    companion object {
        private const val KEY_UI_STATE = "ui_state"
    }
}
