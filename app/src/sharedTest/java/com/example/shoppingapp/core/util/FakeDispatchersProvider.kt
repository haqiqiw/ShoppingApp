package com.example.shoppingapp.core.util

import kotlinx.coroutines.CoroutineDispatcher

class FakeDispatchersProvider(
    private val coroutineDispatcher: CoroutineDispatcher
) : DispatchersProvider {
    override val main: CoroutineDispatcher
        get() = coroutineDispatcher
    override val io: CoroutineDispatcher
        get() = coroutineDispatcher
    override val default: CoroutineDispatcher
        get() = coroutineDispatcher
}
