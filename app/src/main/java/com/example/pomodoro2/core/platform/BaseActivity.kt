package com.example.pomodoro2.core.platform

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * Base Activity class with helper methods for handling fragment transactions and back button
 * events.
 *
 * @see AppCompatActivity
 */
abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onBackPressed() {
/*
        (supportFragmentManager.findFragmentById(
            id.fragmentContainer) as BaseFragment).onBackPressed()
*/
        super.onBackPressed()
    }

    abstract fun fragment(): BaseFragment
}