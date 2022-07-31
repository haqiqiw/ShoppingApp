package com.example.shoppingapp.ui.login

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.example.shoppingapp.R
import com.example.shoppingapp.core.util.KeyboardUtils
import com.example.shoppingapp.databinding.LoginFragmentBinding
import com.example.shoppingapp.ui.BaseFragment

class LoginFragment(
    viewModelFactory: ViewModelProvider.Factory? = null
) : BaseFragment<LoginFragmentBinding, LoginViewModel>() {

    override val viewModel: LoginViewModel by viewModels {
        viewModelFactory ?: LoginViewModelFactory(requireContext())
    }

    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): LoginFragmentBinding {
        return LoginFragmentBinding.inflate(inflater, container, false)
    }

    override fun setUpView() {
        super.setUpView()
        binding.etPassword.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                KeyboardUtils.hideSoftKeyboard(activity)
                viewModel.onLogin(
                    binding.etUsername.text.toString(),
                    binding.etPassword.text.toString()
                )
                return@setOnEditorActionListener true
            }
            false
        }

        binding.btnLogin.setOnClickListener {
            KeyboardUtils.hideSoftKeyboard(activity)
            viewModel.onLogin(
                binding.etUsername.text.toString(),
                binding.etPassword.text.toString()
            )
        }
    }

    override fun renderUi() {
        super.renderUi()
        collectLifecycleFlow(viewModel.uiState) { uiState ->
            renderData(uiState)
        }
        collectLifecycleFlow(viewModel.uiEvent) { uiEvent ->
            when (uiEvent) {
                is LoginUiEvent.ShowLoadingDialog -> {
                    showLoadingDialog()
                }
                is LoginUiEvent.DismissLoadingDialog -> {
                    dismissLoadingDialog()
                }
                is LoginUiEvent.ShowSnackbar -> {
                    showSnackbar(uiEvent.message)
                }
                is LoginUiEvent.GoToProductList -> {
                    goToProductList()
                }
            }
        }
    }

    private fun renderData(uiState: LoginUiState) {
        binding.etUsername.setText(uiState.username)
        binding.etPassword.setText(uiState.password)
    }

    private fun goToProductList() {
        val options = navOptions {
            popUpTo(R.id.navigation_login) {
                inclusive = true
            }
        }
        findNavController().navigate(R.id.navigation_product_list, null, options)
    }
}
