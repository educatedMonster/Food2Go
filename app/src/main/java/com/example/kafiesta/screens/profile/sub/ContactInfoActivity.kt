package com.example.kafiesta.screens.profile.sub

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.kafiesta.R
import com.example.kafiesta.constants.UserConst
import com.example.kafiesta.databinding.ActivityContactInfoBinding
import com.example.kafiesta.screens.BaseActivity
import com.example.kafiesta.screens.main.MainViewModel
import com.example.kafiesta.utilities.helpers.SharedPrefs
import com.example.kafiesta.utilities.helpers.getSecurePrefs

class ContactInfoActivity : BaseActivity() {

    override val hideStatusBar: Boolean get() = false
    override val showBackButton: Boolean get() = true

    private lateinit var binding: ActivityContactInfoBinding
    private var mActionBar: ActionBar? = null

    private val mainViewModel: MainViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(application)
            .create(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initConfig()
    }
    override fun onResume() {
        super.onResume()
        initRequest()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initConfig() {
        initBinding()
        initActionBar()
    }

    private fun initRequest() {
        requestMainViewModel()
    }

    private fun requestMainViewModel() {
        mainViewModel.getMe()
    }

    private fun initBinding() {
        binding = DataBindingUtil.setContentView(this,R.layout.activity_contact_info)
        binding.lifecycleOwner = this
//        binding.viewModel = viewModel
    }

    private fun initActionBar() {
        setSupportActionBar(binding.toolbar)
        mActionBar = supportActionBar
        if (mActionBar != null) {
            mActionBar!!.setDisplayHomeAsUpEnabled(true)
            mActionBar!!.setDisplayShowHomeEnabled(true)
            mActionBar!!.title = getString(R.string.title_activity_shop)
        } else {
            throw IllegalArgumentException(getString(R.string.error_message_illegal_argument_exception))
        }
    }
}