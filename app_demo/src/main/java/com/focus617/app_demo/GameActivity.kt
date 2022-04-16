package com.focus617.app_demo

import android.opengl.GLSurfaceView
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.focus617.app_demo.engine.AndroidWindow
import com.focus617.app_demo.engine.Sandbox

class GameActivity : AppCompatActivity() {

    // EntryPoint for XGame
    lateinit var game: Sandbox

    private lateinit var mGLSurfaceView: GLSurfaceView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 创建一个GLSurfaceView实例,并将其设置为此Activity的ContentView。
        mGLSurfaceView = AndroidWindow.createWindow(this)
        setContentView(mGLSurfaceView)

        // 以这个GLSurfaceView实例作为Sandbox的Window，初始化引擎
        game = Sandbox(mGLSurfaceView as AndroidWindow)
    }

    override fun onResume() {
        super.onResume()
        mGLSurfaceView.onResume()
    }


    override fun onPause() {
        super.onPause()
        mGLSurfaceView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        game.onDestroy()
    }
}