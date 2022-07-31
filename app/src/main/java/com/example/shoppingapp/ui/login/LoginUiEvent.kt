package com.example.shoppingapp.ui.login

sealed class LoginUiEvent {
    object ShowLoadingDialog : LoginUiEvent()
    object DismissLoadingDialog : LoginUiEvent()
    data class ShowSnackbar(val message: String) : LoginUiEvent()
    object GoToProductList : LoginUiEvent()
}
