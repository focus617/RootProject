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
import com.example.pomodoro2.data.DataSourceContainer
import com.example.pomodoro2.data.DefaultTaskRepository
import com.example.pomodoro2.framework.base.BaseFragment
import com.example.pomodoro2.databinding.FragmentDashboardBinding
import com.example.pomodoro2.features.dashboard.domain.DashboardInteractors
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardFragment : BaseFragment() {

    private lateinit var dashboardViewModel: DashboardViewModel

    override fun layoutId(): Int {
        return R.layout.fragment_dashboard
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get a reference to the ViewModel associated with this fragment.
        dashboardViewModel = buildViewModel()
    }

    private fun buildViewModel(): DashboardViewModel {
        // Build the ViewModelFactory with Interactors for this feature
        val application = requireNotNull(this.activity).application

        // TODO: change to dashboard repository
        val taskRepository = DefaultTaskRepository.getInstance(
            DataSourceContainer.roomTaskDataSource,
            DataSourceContainer.inMemoryDataSource
        )
        DashboardViewModelFactory.inject(
            application,
            DashboardInteractors(
                //TODO: Add use case set for activity feature here
            )
        )

        // Get a reference to the ViewModel associated with this fragment.
        return ViewModelProvider(requireActivity(), DashboardViewModelFactory)
            .get(DashboardViewModel::class.java)
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