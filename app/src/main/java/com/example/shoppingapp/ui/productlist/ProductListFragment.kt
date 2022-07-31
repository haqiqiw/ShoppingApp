package com.example.shoppingapp.ui.productlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.example.shoppingapp.R
import com.example.shoppingapp.databinding.ProductListFragmentBinding
import com.example.shoppingapp.domain.model.Product
import com.example.shoppingapp.ui.BaseFragment
import com.example.shoppingapp.ui.FragmentConstant

class ProductListFragment(
    viewModelFactory: ViewModelProvider.Factory? = null
) : BaseFragment<ProductListFragmentBinding, ProductListViewModel>(), MenuProvider {

    private lateinit var productListAdapter: ProductListAdapter

    override val viewModel: ProductListViewModel by viewModels {
        viewModelFactory ?: ProductListViewModelFactory(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        requireActivity().addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun initViewBinding(inflater: LayoutInflater, container: ViewGroup?): ProductListFragmentBinding {
        return ProductListFragmentBinding.inflate(inflater, container, false)
    }

    override fun setUpView() {
        super.setUpView()
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.fetchProducts()
            binding.swipeRefresh.isRefreshing = false
        }

        productListAdapter = ProductListAdapter { product ->
            viewModel.onEvent(ProductListUiEvent.GoToProductDetail(product))
        }
        binding.recyclerView.adapter = productListAdapter
    }

    override fun renderUi() {
        super.renderUi()
        collectLifecycleFlow(viewModel.uiState) { uiState ->
            if (uiState.isLoading) {
                renderLoading()
            } else {
                renderData(uiState)
            }
        }
        collectLifecycleFlow(viewModel.uiEvent) { uiEvent ->
            when (uiEvent) {
                is ProductListUiEvent.ShowLoadingDialog -> {
                    showLoadingDialog()
                }
                is ProductListUiEvent.DismissLoadingDialog -> {
                    dismissLoadingDialog()
                }
                is ProductListUiEvent.ShowSnackbar -> {
                    showSnackbar(uiEvent.message)
                }
                is ProductListUiEvent.GoToProductDetail -> {
                    goToProductDetail(uiEvent.product)
                }
                is ProductListUiEvent.GoToLogin -> {
                    goToLogin()
                }
            }
        }
        setFragmentResultListener(FragmentConstant.KEY_REFRESH_DATA) { _, bundle ->
            val result = bundle.getBoolean(FragmentConstant.KEY_REFRESH_DATA) as? Boolean ?: false
            if (result) {
                viewModel.fetchProducts(isCached = true)
            }
        }
    }

    private fun renderLoading() {
        binding.progressBar.isVisible = true
        binding.recyclerView.isVisible = false
    }

    private fun renderData(uiState: ProductListUiState) {
        binding.progressBar.isVisible = false
        binding.recyclerView.isVisible = true
        productListAdapter.submitList(uiState.products)
    }

    private fun goToProductDetail(product: Product) {
        val bundle = bundleOf(FragmentConstant.KEY_PRODUCT to product)
        findNavController().navigate(R.id.navigation_product_detail, bundle)
    }

    private fun goToLogin() {
        val options = navOptions {
            popUpTo(R.id.navigation_product_list) {
                inclusive = true
            }
        }
        findNavController().navigate(R.id.navigation_login, null, options)
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.product_list_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.user_logout -> {
                viewModel.onLogout()
                true
            }
            else -> false
        }
    }
}
