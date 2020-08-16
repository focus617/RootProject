package com.example.pomodoro2.features.countDownTimer.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.pomodoro2.R
import com.example.pomodoro2.data.DataSourceContainer
import com.example.pomodoro2.data.DefaultTaskRepository
import com.example.pomodoro2.features.countDownTimer.domain.CountDownTimerInteractors
import com.example.pomodoro2.framework.platform.BaseFragment

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

        val root = super.onCreateView(inflater, container, savedInstanceState)
        val textView: TextView = root.findViewById(R.id.text_timer)
        countDownTimerViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}