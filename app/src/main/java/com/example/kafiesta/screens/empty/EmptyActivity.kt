package com.example.kafiesta.screens.empty

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.kafiesta.R
import com.example.kafiesta.constants.UserConst
import com.example.kafiesta.databinding.ActivityEmptyBinding
import com.example.kafiesta.screens.BaseActivity
import com.example.kafiesta.utilities.helpers.SharedPrefs
import com.example.kafiesta.utilities.helpers.getSecurePrefs

class EmptyActivity : BaseActivity() {
    private var userId = 0L
    private var mActionBar: ActionBar? = null
    override val hideStatusBar: Boolean get() = false
    override val showBackButton: Boolean get() = false

    private lateinit var binding: ActivityEmptyBinding
    private val emptyViewModel: EmptyViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory(application)
            .create(EmptyViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userId =
            SharedPrefs(getSecurePrefs(this)).getString(UserConst.USER_ID)!!.toLong()
        initConfig()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initConfig() {
        initBinding()
        initActionBar()
    }

    private fun initBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_empty)
        binding.lifecycleOwner = this
    }

    private fun initActionBar() {
        setSupportActionBar(binding.toolbar)
        mActionBar = supportActionBar
        if (mActionBar != null) {
            mActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
            mActionBar!!.setDisplayHomeAsUpEnabled(true)
            mActionBar!!.setDisplayShowHomeEnabled(true)
            mActionBar!!.setDisplayUseLogoEnabled(true)
        } else {
            throw IllegalArgumentException(getString(R.string.error_message_illegal_argument_exception))
        }
    }
}