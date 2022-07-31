package com.example.shoppingapp.ui.productlist

import com.example.shoppingapp.domain.model.Product

sealed class ProductListUiEvent {
    object ShowLoadingDialog : ProductListUiEvent()
    object DismissLoadingDialog : ProductListUiEvent()
    data class ShowSnackbar(val message: String) : ProductListUiEvent()
    data class GoToProductDetail(val product: Product) : ProductListUiEvent()
    object GoToLogin : ProductListUiEvent()
}
