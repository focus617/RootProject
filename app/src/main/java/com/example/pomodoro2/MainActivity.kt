package com.example.pomodoro2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.pomodoro2.databinding.ActivityMainBinding
import com.example.pomodoro2.databinding.NavHeaderBinding
import com.example.pomodoro2.features.login.presentation.UserProfileViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var headerBinding: NavHeaderBinding

    private val navController by lazy { findNavController(R.id.nav_host_fragment) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupDataBinding()
        setupActionBar()
        setupNavigation()
        setupDrawerViewModel()

    }



    private fun setupDataBinding() {

        activityMainBinding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        // TODO: check how to improve below unnecessary remove?
        var view = activityMainBinding.drawerNavigationView.getHeaderView(0)
        activityMainBinding.drawerNavigationView.removeHeaderView(view)

        headerBinding = DataBindingUtil.inflate(
            layoutInflater, R.layout.nav_header,
            activityMainBinding.drawerNavigationView, false
        )
        headerBinding.ivEdit.setOnClickListener {
            navController.navigate(R.id.loginFragment)
            drawerLayout.closeDrawer(GravityCompat.START)
        }
        activityMainBinding.drawerNavigationView.addHeaderView(headerBinding.root)

    }

    private fun setupActionBar() {
        val appBarConfiguration by lazy {
            // Passing each menu ID as a set of Ids because each
            // menu should be considered as top level destinations.
            AppBarConfiguration(
                topLevelDestinationIds = setOf(
                    R.id.navigation_project, R.id.navigation_timer, R.id.navigation_statistics
                )
            )
        }
        setupActionBarWithNavController(navController, appBarConfiguration)
    }
    
    private fun setupNavigation() {
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
        NavigationUI.setupWithNavController(activityMainBinding.bottomNavigationView, navController)
        NavigationUI.setupWithNavController(activityMainBinding.drawerNavigationView, navController)
    }

    private fun setupDrawerViewModel() {
        try {
            val viewModelProvider = ViewModelProvider(
                navController.getViewModelStoreOwner(R.id.nav_graph),
                ViewModelProvider.AndroidViewModelFactory(application)
            )
            var userProfileViewModel = viewModelProvider.get(UserProfileViewModel::class.java)
            headerBinding.viewModel = userProfileViewModel
            userProfileViewModel.loadProfile()
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, drawerLayout)
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}