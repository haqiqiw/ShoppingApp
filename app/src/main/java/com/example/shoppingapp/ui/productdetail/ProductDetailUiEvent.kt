package com.example.shoppingapp.ui.productdetail

sealed class ProductDetailUiEvent {
    object ShowLoadingDialog : ProductDetailUiEvent()
    object DismissLoadingDialog : ProductDetailUiEvent()
    data class ShowSnackbar(val message: String) : ProductDetailUiEvent()
    object GoToProductList : ProductDetailUiEvent()
}
