package com.example.pomodoro2.features.countDownTimer.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.pomodoro2.R
import com.example.pomodoro2.data.DataSourceContainer
import com.example.pomodoro2.databinding.FragmentTaskBinding
import com.example.pomodoro2.databinding.FragmentTimerBinding
import com.example.pomodoro2.domain.repository.DefaultTaskRepository
import com.example.pomodoro2.features.countDownTimer.domain.CountDownTimerInteractors
import com.example.pomodoro2.framework.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CountDownTimerFragment : BaseFragment() {

    private lateinit var countDownTimerViewModel: CountDownTimerViewModel

    override fun layoutId(): Int {
        return R.layout.fragment_timer
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        countDownTimerViewModel = buildViewModel()
    }

    private fun buildViewModel(): CountDownTimerViewModel {
        // Build the ViewModelFactory with Interactors for this feature
        val application = requireNotNull(this.activity).application

        // TODO: change to activity repository
        val taskRepository = DefaultTaskRepository.getInstance(
            DataSourceContainer.remoteTaskDataSource,
            DataSourceContainer.roomTaskDataSource,
            DataSourceContainer.inMemoryDataSource
        )
        CountDownTimerViewModelFactory.inject(
            application,
            CountDownTimerInteractors(
                //TODO: Add use case set for activity feature here
            )
        )

        // Get a reference to the ViewModel associated with this fragment.
        return ViewModelProvider(requireActivity(), CountDownTimerViewModelFactory)
            .get(CountDownTimerViewModel::class.java)
    }


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentTimerBinding = DataBindingUtil.inflate(
            inflater, layoutId(), container, false
        )

        // To use the View Model with data binding, you have to explicitly
        // give the binding object a reference to it.
        binding.viewModel = countDownTimerViewModel

        // Specify the current activity as the lifecycle owner of the binding.
        // This is necessary so that the binding can observe LiveData updates.
        binding.lifecycleOwner = this



        return binding.root
    }
}