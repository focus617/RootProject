package com.focus617.tankwar.ui.game

import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.focus617.platform.uicontroller.BaseFragment
import com.focus617.tankwar.R
import com.focus617.tankwar.databinding.FragmentGameBinding
import com.google.android.material.snackbar.Snackbar

class GameFragment : BaseFragment() {

    private var _binding: FragmentGameBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var viewModel: GameViewModel
    private lateinit var renderer: XRenderer


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel = ViewModelProvider(this)[GameViewModel::class.java]
        viewModel.snackbarText.observe(this.viewLifecycleOwner, Observer { event ->
            event.getContentIfNotHandled()?.let {
                Snackbar.make(root, it, Snackbar.LENGTH_SHORT).show()
            }
        })
        viewModel.showSnackbarMessage(R.string.WelcomeMessage)

        setupXRenderer()
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupXRenderer() {
        val surfaceView = binding.surfaceView
        renderer = XRenderer(requireContext(), surfaceView)

    }


}