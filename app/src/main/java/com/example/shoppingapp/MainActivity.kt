package com.example.shoppingapp

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.shoppingapp.data.preference.UserPreference

class MainActivity : AppCompatActivity() {

    private var isLoggedIn: Boolean = false
    private val userPreference: UserPreference by lazy { UserPreference.create(this@MainActivity) }

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        setUpUserPreference()
        setUpToolbar()
        setUpNavigation()
    }

    private fun setUpUserPreference() {
        isLoggedIn = userPreference.userToken.isNotBlank()
    }

    private fun setUpToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    private fun setUpNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
        navHostFragment?.let {
            val navController = it.navController
            val navGraph = navController.navInflater.inflate(R.navigation.mobile_navigation)

            val destionation = if (isLoggedIn) {
                R.id.navigation_product_list
            } else {
                R.id.navigation_login
            }
            navGraph.setStartDestination(destionation)
            navController.graph = navGraph

            appBarConfiguration = AppBarConfiguration(setOf(R.id.navigation_login, R.id.navigation_product_list))
            setupActionBarWithNavController(navController, appBarConfiguration)

            val toolbar = findViewById<Toolbar>(R.id.toolbar)
            navController.addOnDestinationChangedListener { _, destination, _ ->
                when (destination.id) {
                    R.id.navigation_login -> {
                        toolbar.visibility = View.GONE
                    }
                    else -> {
                        toolbar.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(findNavController(R.id.nav_host_fragment)) ||
            super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
