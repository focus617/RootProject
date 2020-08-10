package com.example.pomodoro2.features.dashboard.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.pomodoro2.R
import com.example.pomodoro2.core.platform.BaseFragment
import com.example.pomodoro2.databinding.FragmentDashboardBinding
import com.example.pomodoro2.features.infra.database.AppDatabase
import com.example.pomodoro2.features.projects.presentation.ProjectsViewModel
import com.example.pomodoro2.features.projects.presentation.ProjectsViewModelFactory
import kotlinx.android.synthetic.main.fragment_activity.*
import kotlinx.android.synthetic.main.fragment_dashboard.*

class DashboardFragment : BaseFragment() {

    private lateinit var dashboardViewModel: DashboardViewModel

    override fun layoutId(): Int {
        return R.layout.fragment_dashboard
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        dashboardViewModel =
                ViewModelProvider(this).get(DashboardViewModel::class.java)

        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentDashboardBinding = DataBindingUtil.inflate(
            inflater, layoutId(), container, false
        )

        // Create an instance of the ViewModel Factory.
//        val application = requireNotNull(this.activity).application
//        val dataSource = AppDatabase.getInstance(application).projectDao
//        val viewModelFactory = ProjectsViewModelFactory(dataSource, application)

        // Get a reference to the ViewModel associated with this fragment.
//        dashboardViewModel =
//            ViewModelProvider(this, viewModelFactory).get(DashboardViewModel::class.java)

        // To use the View Model with data binding, you have to explicitly
        // give the binding object a reference to it.
        binding.viewModel = dashboardViewModel

        // Specify the current activity as the lifecycle owner of the binding.
        // This is necessary so that the binding can observe LiveData updates.
        binding.lifecycleOwner = this

        /* Replaced by Databinding
        dashboardViewModel.text.observe(viewLifecycleOwner, Observer {
            text_dashboard.text = it
        })*/

        return binding.root
    }
}