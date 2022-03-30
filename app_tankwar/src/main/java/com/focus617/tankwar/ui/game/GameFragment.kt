package com.focus617.tankwar.ui.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.focus617.platform.uicontroller.BaseFragment
import com.focus617.tankwar.R
import com.focus617.tankwar.databinding.FragmentGameBinding
import com.focus617.tankwar.di.scene.TestScene
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class GameFragment : BaseFragment() {
    private lateinit var viewModel: GameViewModel

    @TestScene
    @Inject
    lateinit var scene: IDraw

    private var _binding: FragmentGameBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel = ViewModelProvider(this)[GameViewModel::class.java]
        viewModel.snackbarText.observe(this.viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let {
                Snackbar.make(root, it, Snackbar.LENGTH_SHORT).show()
            }
        }
        viewModel.showSnackbarMessage(R.string.WelcomeMessage)

        initXRenderer()
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initXRenderer() {
        XRenderer(binding.surfaceView, scene)
    }

}