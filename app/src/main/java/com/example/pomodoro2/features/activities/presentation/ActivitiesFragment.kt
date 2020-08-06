package com.example.pomodoro2.features.activities.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.pomodoro2.R
import com.example.pomodoro2.core.platform.BaseFragment

class ActivitiesFragment : BaseFragment() {

    private lateinit var activitiesViewModel: ActivitiesViewModel

    override fun layoutId(): Int {
        return R.layout.fragment_activity
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        activitiesViewModel =
                ViewModelProvider(this).get(ActivitiesViewModel::class.java)
        val root = super.onCreateView(inflater, container, savedInstanceState)
        val textView: TextView = root.findViewById(R.id.text_activity)
        activitiesViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}