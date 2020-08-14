package com.example.pomodoro2.features.countDownTimer.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.pomodoro2.R
import com.example.pomodoro2.framework.platform.BaseFragment
import com.example.pomodoro2.framework.platform.deprecated.MyViewModelFactory

class CountDownTimerFragment : BaseFragment() {

    private lateinit var countDownTimerViewModel: CountDownTimerViewModel

    override fun layoutId(): Int {
        return R.layout.fragment_timer
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        countDownTimerViewModel =
            ViewModelProvider(this,
                MyViewModelFactory
            ).get(CountDownTimerViewModel::class.java)

        val root = super.onCreateView(inflater, container, savedInstanceState)
        val textView: TextView = root.findViewById(R.id.text_timer)
        countDownTimerViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}