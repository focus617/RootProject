package com.focus617.platform.uicontroller

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import timber.log.Timber

open class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("${javaClass.simpleName} Created.")
        ActivityCollector.addActivity(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.d("${javaClass.simpleName} Destroyed.")
        ActivityCollector.removeActivity(this)
    }

    // For Broadcast Testing with force offline function
    lateinit var forceOfflineReceiver: ForceOfflineReceiver

    inner class ForceOfflineReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            AlertDialog.Builder(context).apply {
                setTitle("Warning")
                setMessage("You are forced to be offline.")
                setCancelable(false)
                setPositiveButton("OK") { _, _ ->
                    ActivityCollector.finishAll() // 销毁所有Activity
                }
                show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        forceOfflineReceiver = ForceOfflineReceiver()
        registerForceOfflineReceiver()
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(forceOfflineReceiver)
    }


    private fun registerForceOfflineReceiver() {
        val intentFilter = IntentFilter()
        intentFilter.addAction("com.focus617.bookreader.tester.FORCE_OFFLINE")
        forceOfflineReceiver = ForceOfflineReceiver()
        registerReceiver(forceOfflineReceiver, intentFilter)
    }

    fun sendOfflineBroadcast() {
        val intent = Intent("com.focus617.bookreader.tester.FORCE_OFFLINE")
        intent.setPackage(packageName)
        sendBroadcast(intent)
    }

    fun toast(string: String) {
        Toast.makeText(this, string, Toast.LENGTH_LONG).show()
    }

    fun setupFullScreen() {
        val mControlsView: View = window.decorView
        val uiOptions =
            View.SYSTEM_UI_FLAG_LOW_PROFILE or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        mControlsView.systemUiVisibility = uiOptions
    }
}