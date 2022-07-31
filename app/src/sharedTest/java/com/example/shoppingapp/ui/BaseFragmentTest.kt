package com.example.shoppingapp.ui

import android.os.Build
import androidx.fragment.app.Fragment
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shoppingapp.R
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
@Config(
    instrumentedPackages = ["androidx.loader.content"],
    sdk = [Build.VERSION_CODES.P]
)
abstract class BaseFragmentTest<VM : ViewModel> {

    protected lateinit var viewModel: VM
    protected lateinit var scenario: FragmentScenario<Fragment>

    abstract fun initViewModel(): VM

    abstract fun initFragment(testViewModelFactory: TestViewModelFactory): Fragment

    protected val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        viewModel = initViewModel()
        scenario = launchFragmentInContainer(
            themeResId = R.style.Theme_ShoppingApp,
            initialState = Lifecycle.State.STARTED
        ) {
            initFragment(TestViewModelFactory(viewModel))
        }
    }

    class TestViewModelFactory(private val viewModel: ViewModel) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return viewModel as T
        }
    }
}
