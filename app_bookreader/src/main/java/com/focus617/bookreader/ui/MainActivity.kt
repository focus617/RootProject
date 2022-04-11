package com.focus617.bookreader.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.focus617.bookreader.R
import com.focus617.bookreader.databinding.ActivityMainBinding
import com.focus617.bookreader.framework.MyViewModelFactory
import com.focus617.bookreader.ui.home.HomeViewModel
import com.focus617.platform.uicontroller.BaseActivity
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    @Inject
    lateinit var viewModelFactory: MyViewModelFactory
    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel =
            ViewModelProvider(this, viewModelFactory)[HomeViewModel::class.java]

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setNavigation()
    }

    private fun setNavigation() {
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val collapsingToolbarLayout = binding.appBarMain.collapsingToolbar
        val toolbar = binding.appBarMain.toolbar

        navController = findNavController(R.id.nav_host_fragment_content_main)

        // Setup drawer navigation
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        navView.setupWithNavController(navController)
        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)

        // Setup actionBar with collapsing toolBar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        Glide.with(this).load(R.drawable.mango).into(binding.appBarMain.headerImageView)
        collapsingToolbarLayout.setupWithNavController(toolbar, navController, appBarConfiguration)

        // trial: make slideshow fragment as no_action_bar mode
//        navController.addOnDestinationChangedListener { _, destination, _ ->
//            when (destination.id) {
//                R.id.nav_slideshow -> binding.appBarMain.appBar.visibility = View.GONE
//                else -> {
//                    binding.appBarMain.appBar.visibility = View.VISIBLE
//                    binding.appBarMain.collapsingToolbar.visibility = View.VISIBLE
//                }
//            }
//        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // Process open file intent.
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.data?.also { uri -> viewModel.addDocument(uri) }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = binding.drawerLayout
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Timber.i("onSaveInstanceState called")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Timber.i("onRestoreInstanceState called")
    }

    companion object {
        const val READ_REQUEST_CODE = 100
    }
}