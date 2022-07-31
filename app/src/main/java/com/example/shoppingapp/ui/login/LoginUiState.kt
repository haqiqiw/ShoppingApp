package com.example.shoppingapp.ui.login

import com.example.shoppingapp.domain.model.LoginForm

data class LoginUiState(
    val loginForm: LoginForm = LoginForm("donero", "ewedon")
) {
    val username get() = loginForm.username
    val password get() = loginForm.password
}
