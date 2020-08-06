package com.example.pomodoro2.features.projects.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.pomodoro2.R
import com.example.pomodoro2.core.platform.BaseFragment

class ProjectFragment : BaseFragment() {

    private lateinit var projectsViewModel: ProjectsViewModel

    override fun layoutId(): Int {
        return R.layout.fragment_project
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        projectsViewModel = ViewModelProvider(this).get(ProjectsViewModel::class.java)
        val root = super.onCreateView(inflater, container, savedInstanceState)
        val textView: TextView = root.findViewById(R.id.text_goal)
        projectsViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}