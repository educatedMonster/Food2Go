package com.example.kafiesta.screens.login

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.kafiesta.R
import com.example.kafiesta.databinding.ActivityLoginBinding
import com.example.kafiesta.screens.BaseActivity
import com.example.kafiesta.screens.main.MainActivity
import com.example.kafiesta.utilities.extensions.hideView
import com.example.kafiesta.utilities.extensions.showView

class LoginActivity : BaseActivity() {

    override val hideStatusBar: Boolean
        get() = false
    override val showBackButton: Boolean
        get() = false

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initConfig()
    }

    /**
     * Call all the functions here that needed to be initialized first
     */
    private fun initConfig() {
        initBinding()
        initListener()
        initLiveData()
    }

    private fun initBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.lifecycleOwner = this

        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
            .create(LoginViewModel::class.java)

        viewModel.login("jadalmario.freelancer@gmail.com", "@Unknown0322")

    }

    /**
     * Initialize event listeners
     */
    private fun initListener() {

    }

    /**
     * Initialize the live data to be observe on this activity
     */
    private fun initLiveData() {
        //Observe `userDomain` live data from our LoginViewModel
        viewModel.userDomain.observe(this) { user ->
            //if user.message does not contains "successfully" that means user inputted wrong
            //credentials or doesn't have a data on our server that needed to register first
//            if (!user.message.contains("successfully")) {
//                showToast(user.message)
//            }

            //if the user.status is true we can now proceed to the MainActivity activity
            if (user.expiresIn != 0L) {
                //proceedToActivity function can be found at BaseActivity.kt
                proceedToActivity(MainActivity::class.java, true)
            }
        }

        viewModel.isLoading.observe(this) {
            setLoading(it)
        }
    }

    private fun setLoading(set: Boolean) {
        //Wrap it in try and catch block to avoid exceptions when hiding and showing a views
        //This catch exception will occur when the user changes from this activity to another
        try {
            if (set) {
                //showView function will saw it on Context.kt class to show the view
                //hideView function will saw it on Context.kt class to hide the view
                showView(binding.progressLoading)
            } else {
                hideView(binding.progressLoading)
            }
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }
}