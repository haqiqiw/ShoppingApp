package com.example.shoppingapp.rule

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

@ExperimentalCoroutinesApi
class TestCoroutineRule : TestRule {

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    override fun apply(base: Statement?, description: Description?): Statement {
        return object : Statement() {
            override fun evaluate() {
                Dispatchers.setMain(testDispatcher)
                base?.evaluate()
                Dispatchers.resetMain()
            }
        }
    }

    fun getTestDispatcher(): CoroutineDispatcher = testDispatcher

    fun runTest(block: suspend TestScope.() -> Unit) {
        testScope.runTest { block() }
    }
}
