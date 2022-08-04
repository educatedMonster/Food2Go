package com.example.kafiesta.screens.main

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.kafiesta.R
import com.example.kafiesta.databinding.ActivityLoginBinding
import com.example.kafiesta.screens.BaseActivity

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
    }

    private fun initBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.lifecycleOwner = this

        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
            .create(LoginViewModel::class.java)


        binding.tvText.text = "Main Activity Test"

        viewModel.login(this,"jadalmario.freelancer@gmail.com", "@Unknown0322")
    }


}