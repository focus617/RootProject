package com.example.pomodoro2.features.tasks.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.navGraphViewModels
import com.example.pomodoro2.R
import com.example.pomodoro2.framework.platform.BaseDialogFragment
import com.example.pomodoro2.databinding.FragmentLoginBinding
import com.example.pomodoro2.databinding.FragmentNewTaskBinding
import com.example.pomodoro2.domain.Task
import com.example.pomodoro2.framework.platform.MyViewModelFactory
import kotlinx.android.synthetic.main.fragment_login.*

class NewTaskFragment : BaseDialogFragment() {

    private lateinit var viewModel: TasksViewModel
    private var task = Task(title="", imageId = R.drawable.read_book, priority = 100)

    override fun layoutId(): Int {
        return R.layout.fragment_new_task
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding: FragmentNewTaskBinding = DataBindingUtil.inflate(
            inflater, layoutId(), container, false
        )

        // Get a reference to the ViewModel associated with this fragment.
        viewModel =
            ViewModelProvider(this, MyViewModelFactory).get(TasksViewModel::class.java)

        binding.task = task
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.setOnShowListener {
            dialog?.setTitle(getString(R.string.create_task))
            dialog?.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        btnSave.setOnClickListener {
            viewModel.createNewTask(task)
            dismiss()
        }

    }
}
