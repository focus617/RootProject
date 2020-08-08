package com.example.pomodoro2.features.projects.presentation

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.pomodoro2.R
import com.example.pomodoro2.core.platform.BaseFragment
import com.example.pomodoro2.core.platform.EventObserver
import com.example.pomodoro2.databinding.FragmentProjectBinding
import com.example.pomodoro2.features.infra.database.AppDatabase
import com.example.pomodoro2.features.infra.database.Project
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_project.*

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

        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentProjectBinding = DataBindingUtil.inflate(
            inflater, layoutId(), container, false
        )

        val application = requireNotNull(this.activity).application

        // Create an instance of the ViewModel Factory.
        val dataSource = AppDatabase.getInstance(application).projectDao
        val viewModelFactory = ProjectsViewModelFactory(dataSource, application)

        // Get a reference to the ViewModel associated with this fragment.
        projectsViewModel =
            ViewModelProvider(this, viewModelFactory).get(ProjectsViewModel::class.java)

        // To use the View Model with data binding, you have to explicitly
        // give the binding object a reference to it.
        binding.viewModel = projectsViewModel

        // Specify the current activity as the lifecycle owner of the binding.
        // This is necessary so that the binding can observe LiveData updates.
        binding.lifecycleOwner = this

        // Add an Observer on the state variable for showing a SnackBar message
        projectsViewModel.showSnackBarEvent.observe(viewLifecycleOwner, EventObserver {
            Snackbar.make(
                requireActivity().findViewById(android.R.id.content),
                it,
                Snackbar.LENGTH_SHORT // How long to display the message.
            ).show()
        })

        // Add an Observer on the state variable for Navigating
        projectsViewModel.navigateToActivityFragment.observe(viewLifecycleOwner, EventObserver {
            requireView().findNavController().navigate(
                ProjectFragmentDirections.actionNavigationProjectToNavigationActivity()
            )
        })

        // TODO: for initial debug&testing only, plan to replace it with a snack bar in future
        projectsViewModel.text.observe(viewLifecycleOwner, Observer
        {
            text_goal.text = it
        })

        setHasOptionsMenu(true)

        return binding.root
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

                projectsViewModel.createTestData()
                true
            }

            R.id.action_delete_data -> {
                Toast.makeText(
                    context, "Pseudo_Data deleted",
                    Toast.LENGTH_SHORT
                ).show()
                projectsViewModel.clearTestData()
                true
            }

            R.id.aboutFragment -> {
                NavigationUI.onNavDestinationSelected(
                    item,
                    requireView().findNavController()
                )
            }

            R.id.action_snack_bar -> {
                projectsViewModel.testSnackBar()

                val project =
                    Project(title = "学习Android开发", imageId = R.drawable.read_book, priority = 1)
                projectsViewModel.doNavigating(project)
                true
            }


            else -> super.onOptionsItemSelected(item);
        }
    }
}