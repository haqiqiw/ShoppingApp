package com.example.shoppingapp.ui.productlist

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingapp.core.util.DispatchersProvider
import com.example.shoppingapp.core.util.Resource
import com.example.shoppingapp.domain.usecase.product.ProductUseCase
import com.example.shoppingapp.domain.usecase.user.UserUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ProductListViewModel(
    private val dispatchers: DispatchersProvider,
    private val productUseCase: ProductUseCase,
    private val userUseCase: UserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductListUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<ProductListUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    init {
        fetchProducts()
    }

    @VisibleForTesting
    fun injectUiState(uiState: ProductListUiState) {
        _uiState.value = uiState
    }

    fun fetchProducts(isCached: Boolean = false) {
        viewModelScope.launch(dispatchers.io) {
            productUseCase.getProducts(isCached)
                .onEach { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            _uiState.value = uiState.value.copy(isLoading = true)
                        }
                        is Resource.Success -> {
                            _uiState.value = uiState.value.copy(
                                products = resource.data.orEmpty(),
                                isLoading = false
                            )
                        }
                        is Resource.Error -> {
                            _uiState.value = uiState.value.copy(isLoading = false)
                            onEvent(ProductListUiEvent.ShowSnackbar(resource.message))
                        }
                    }
                }.launchIn(this)
        }
    }

    fun onLogout() {
        viewModelScope.launch(dispatchers.io) {
            userUseCase.userLogout()
                .onEach { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            onEvent(ProductListUiEvent.ShowLoadingDialog)
                        }
                        is Resource.Success -> {
                            onEvent(ProductListUiEvent.DismissLoadingDialog)
                            onEvent(ProductListUiEvent.GoToLogin)
                        }
                        else -> {
                            onEvent(ProductListUiEvent.DismissLoadingDialog)
                        }
                    }
                }
                .launchIn(this)
        }
    }

    fun onEvent(event: ProductListUiEvent) {
        viewModelScope.launch(dispatchers.default) {
            _uiEvent.emit(event)
        }
    }
}
