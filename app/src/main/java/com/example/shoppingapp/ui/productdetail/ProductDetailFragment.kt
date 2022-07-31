package com.example.shoppingapp.ui.productdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.example.shoppingapp.core.util.KeyboardUtils
import com.example.shoppingapp.databinding.ProductDetailFragmentBinding
import com.example.shoppingapp.domain.model.Product
import com.example.shoppingapp.ui.BaseFragment
import com.example.shoppingapp.ui.FragmentConstant
import kotlinx.coroutines.delay

class ProductDetailFragment(
    viewModelFactory: ViewModelProvider.Factory? = null
) : BaseFragment<ProductDetailFragmentBinding, ProductDetailViewModel>() {

    override val viewModel: ProductDetailViewModel by viewModels {
        viewModelFactory ?: ProductDetailViewModelFactory(this, requireContext())
    }

    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): ProductDetailFragmentBinding {
        return ProductDetailFragmentBinding.inflate(inflater, container, false)
    }

    override fun setUpArguments(arguments: Bundle?) {
        super.setUpArguments(arguments)
        val product = arguments?.getSerializable(FragmentConstant.KEY_PRODUCT) as? Product
        if (product != null) {
            setToolbarTitle(product.title)
            viewModel.initUiState(product)
        }
    }

    override fun setUpView() {
        super.setUpView()

        binding.etDescription.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                KeyboardUtils.hideSoftKeyboard(activity)
                viewModel.onEditProduct(
                    binding.etTitle.text.toString(),
                    binding.etPrice.text.toString(),
                    binding.etCategory.text.toString(),
                    binding.etDescription.text.toString()
                )
                return@setOnEditorActionListener true
            }
            false
        }

        binding.btnEdit.setOnClickListener {
            viewModel.onEditProduct(
                binding.etTitle.text.toString(),
                binding.etPrice.text.toString(),
                binding.etCategory.text.toString(),
                binding.etDescription.text.toString()
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
                is ProductDetailUiEvent.ShowLoadingDialog -> {
                    showLoadingDialog()
                }
                is ProductDetailUiEvent.DismissLoadingDialog -> {
                    dismissLoadingDialog()
                }
                is ProductDetailUiEvent.ShowSnackbar -> {
                    showSnackbar(uiEvent.message)
                }
                is ProductDetailUiEvent.GoToProductList -> {
                    delay(150L)
                    goToProductList()
                }
            }
        }
    }

    private fun renderData(uiState: ProductDetailUiState) {
        Glide.with(this)
            .load(uiState.image)
            .transform(CenterCrop())
            .into(binding.ivThumbnail)

        binding.etId.setText(uiState.id)
        binding.etTitle.setText(uiState.title)
        binding.etPrice.setText(uiState.price)
        binding.etCategory.setText(uiState.category)
        binding.etDescription.setText(uiState.description)
    }

    private fun goToProductList() {
        setFragmentResult(
            FragmentConstant.KEY_REFRESH_DATA,
            bundleOf(FragmentConstant.KEY_REFRESH_DATA to true)
        )
        findNavController().popBackStack()
    }
}
