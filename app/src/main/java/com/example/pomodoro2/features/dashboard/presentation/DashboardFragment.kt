package com.example.pomodoro2.features.dashboard.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.pomodoro2.R
import com.example.pomodoro2.framework.platform.BaseFragment
import com.example.pomodoro2.databinding.FragmentDashboardBinding
import com.example.pomodoro2.framework.platform.deprecated.MyViewModelFactory

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

        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentDashboardBinding = DataBindingUtil.inflate(
            inflater, layoutId(), container, false
        )

        // Get a reference to the ViewModel associated with this fragment.
        dashboardViewModel =
            ViewModelProvider(this,
                MyViewModelFactory
            ).get(DashboardViewModel::class.java)

        // To use the View Model with data binding, you have to explicitly
        // give the binding object a reference to it.
        binding.viewModel = dashboardViewModel

        // Specify the current activity as the lifecycle owner of the binding.
        // This is necessary so that the binding can observe LiveData updates.
        binding.lifecycleOwner = this

        // Observer for the network error.
        dashboardViewModel.eventNetworkError.observe(this, Observer<Boolean> { isNetworkError ->
            if (isNetworkError) onNetworkError()
        })

        /* Replaced by Databinding
        dashboardViewModel.text.observe(viewLifecycleOwner, Observer {
            text_dashboard.text = it
        })*/

        return binding.root
    }

    /**
     * Method for displaying a Toast error message for network errors.
     */
    private fun onNetworkError() {
        if(!dashboardViewModel.isNetworkErrorShown.value!!) {
            Toast.makeText(activity, "Network Error", Toast.LENGTH_LONG).show()
            dashboardViewModel.onNetworkErrorShown()
        }
    }
}