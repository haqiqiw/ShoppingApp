package com.example.shoppingapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import com.example.shoppingapp.core.dialog.LoadingDialog
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

abstract class BaseFragment<VB : ViewBinding, VM : ViewModel> : Fragment() {

    private var _binding: VB? = null
    protected val binding: VB get() = _binding!!

    abstract val viewModel: VM

    private val loadingDialog: LoadingDialog by lazy { LoadingDialog(requireContext()) }

    abstract fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = initViewBinding(inflater, container)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpArguments(arguments)
        setUpView()
        renderUi()
    }

    protected open fun setUpArguments(arguments: Bundle?) {}

    protected open fun setUpView() {}

    protected open fun renderUi() {}

    protected fun <T> collectLifecycleFlow(
        flow: Flow<T>?,
        collect: suspend (T) -> Unit
    ) {
        if (flow == null) return
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                flow.collect {
                    collect(it)
                }
            }
        }
    }

    protected fun setToolbarTitle(title: String) {
        (requireActivity() as? AppCompatActivity)?.supportActionBar?.title = title
    }

    protected fun showLoadingDialog() {
        loadingDialog.show()
    }

    protected fun dismissLoadingDialog() {
        loadingDialog.dismiss()
    }

    protected fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }
}
