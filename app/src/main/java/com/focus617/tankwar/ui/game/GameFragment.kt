package com.focus617.tankwar.ui.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.SurfaceView
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.focus617.platform.uicontroller.BaseFragment
import com.focus617.platform.view_util.setupSnackbar
import com.focus617.tankwar.R
import com.focus617.tankwar.databinding.FragmentGameBinding

class GameFragment : BaseFragment() {

    private var _binding: FragmentGameBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var viewModel: GameViewModel
    private lateinit var surfaceView: SurfaceView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[GameViewModel::class.java]

        _binding = FragmentGameBinding.inflate(inflater, container, false)
        val root: View = binding.root

        surfaceView = binding.surfaceView

        welcome()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupSnackbar()
    }

    private fun setupSnackbar() {
        view?.setupSnackbar(this, viewModel.snackbarText)
    }

    private fun welcome() {
        viewModel.showSnackbarMessage(R.string.WelcomeMessage)
    }
}