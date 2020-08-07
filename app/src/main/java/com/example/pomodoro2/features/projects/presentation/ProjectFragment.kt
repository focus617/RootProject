package com.example.pomodoro2.features.projects.presentation

import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
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

        setHasOptionsMenu(true)

        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.options_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_create_data -> {
                Toast.makeText(
                    context, "Pseudo_Data created",
                    Toast.LENGTH_SHORT
                ).show()
                true
            }

            R.id.action_delete_data -> {
                Toast.makeText(
                    context, "Pseudo_Data deleted",
                    Toast.LENGTH_SHORT
                ).show()
                true
            }

            R.id.aboutFragment -> {
                NavigationUI.onNavDestinationSelected(
                    item,
                    requireView().findNavController()
                )
            }

            else -> super.onOptionsItemSelected(item);
        }
    }
}