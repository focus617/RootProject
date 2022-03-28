package com.focus617.tankwar.ui.game

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.view.*
import androidx.lifecycle.ViewModelProvider
import com.focus617.platform.uicontroller.BaseFragment
import com.focus617.platform.view_util.setupSnackbar
import com.focus617.tankwar.R
import com.focus617.tankwar.databinding.FragmentGameBinding

class GameFragment : BaseFragment(), SurfaceHolder.Callback {

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

        setupSurfaceView()
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

    private fun setupSurfaceView() {
        surfaceView = binding.surfaceView
        surfaceView.holder.addCallback(this)
    }

    override fun surfaceCreated(holder: SurfaceHolder){
        draw()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
    }

    private fun draw() {
        // 创建画布
        val canvas: Canvas = surfaceView.holder.lockCanvas()
        // 画笔
        val paint = Paint()

        canvas.run {
            // 设置画布背景
            drawColor(Color.BLACK)

            paint.color = Color.BLUE          //设置画笔颜色
            drawRect(0F, 0F, 100F, 100F, paint)

            save()
            paint.color = Color.RED           //设置画笔颜色
            paint.style = Paint.Style.FILL    //设置填充样式
            paint.strokeWidth = 5F            //设置画笔宽度
            rotate(90F, (width / 2).toFloat(), (height / 2).toFloat())
            drawLine(0F, (height / 2).toFloat(), width.toFloat(), height.toFloat(), paint)
            restore()
        }

        surfaceView.holder.unlockCanvasAndPost(canvas)
    }

}