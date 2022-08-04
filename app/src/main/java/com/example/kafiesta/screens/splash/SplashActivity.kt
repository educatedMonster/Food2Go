package com.example.kafiesta.screens.splash

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.kafiesta.R
import com.example.kafiesta.databinding.ActivitySplashBinding
import com.example.kafiesta.screens.BaseActivity
import com.example.kafiesta.screens.main.LoginActivity

class SplashActivity : BaseActivity() {

    override val hideStatusBar: Boolean
        get() = false
    override val showBackButton: Boolean
        get() = false

    private lateinit var binding: ActivitySplashBinding
    private var progressStatus: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initConfig()
    }

    /**
     * After going to Login Activity we need to finish this activity
     */
    override fun onPause() {
        super.onPause()
        finish()
    }

    /**
     * Initialize all configurations
     */
    private fun initConfig() {
        initBinding()
        initHideActionBar()
        initLoading()
    }

    /**
     * Hide action bar because it is a splash screen
     */
    private fun initHideActionBar() {
        supportActionBar?.hide()
    }

    private fun initBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        binding.lifecycleOwner = this
    }

    /**
     * Initiate our splash screen progress bar
     */
    private fun initLoading() {
        /**
         * We need to run this task on thread
         */
        Thread {
            /**
             * We need to track the operation to set a progress on our progress bar
             */
            while (progressStatus < 95) {
                try {
                    progressStatus += 16
                    Thread.sleep(200)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
                binding.progressLoading.progress = progressStatus
            }
            if (binding.progressLoading.max >= 90) {
                proceedToActivity(LoginActivity::class.java, true)
            }
        }.start()
    }
}