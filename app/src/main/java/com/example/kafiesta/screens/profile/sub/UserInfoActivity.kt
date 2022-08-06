package com.example.kafiesta.screens.profile.sub

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.kafiesta.R
import com.example.kafiesta.constants.UserConst
import com.example.kafiesta.databinding.ActivityUserInfoBinding
import com.example.kafiesta.screens.BaseActivity
import com.example.kafiesta.screens.main.MainViewModel
import com.example.kafiesta.utilities.extensions.isEmailValid2
import com.example.kafiesta.utilities.extensions.isNotEmpty
import com.example.kafiesta.utilities.helpers.SharedPrefs
import com.example.kafiesta.utilities.helpers.getSecurePrefs
import com.google.android.material.textfield.TextInputEditText
import com.trackerteer.taskmanagement.utilities.extensions.setSafeOnClickListener

class UserInfoActivity : BaseActivity() {
    override val hideStatusBar: Boolean get() = false
    override val showBackButton: Boolean get() = true

    private var userId = 0L
    private lateinit var binding: ActivityUserInfoBinding
    private var mActionBar: ActionBar? = null

    private val mainViewModel: MainViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(application)
            .create(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userId = SharedPrefs(getSecurePrefs(this)).getString(UserConst.ID)!!.toLong()
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
        initLiveData()
        initEventListener()
        initActionBar()
    }

    private fun initRequest() {
        requestMainViewModel()
    }

    private fun requestMainViewModel() {
        mainViewModel.getUserId(userId)
    }

    private fun initBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_info)
        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel
    }

    private fun initLiveData() {
        mainViewModel.userResult.observe(this) {
//            setLoading(it)
        }


        mainViewModel.isLoading.observe(this) {
//            setLoading(it)
        }


    }

    private fun initEventListener() {
        binding.apply {
            buttonUpdate.setSafeOnClickListener {
                val firstName = binding.textInputFirstName
                val lastName = binding.textInputLastName
                val emailAddress = binding.textInputEmailAddress
                onValidate(firstName, lastName, emailAddress)
            }
        }
    }

    private fun initActionBar() {
        setSupportActionBar(binding.toolbar)
        mActionBar = supportActionBar
        if (mActionBar != null) {
            mActionBar!!.setDisplayHomeAsUpEnabled(true)
            mActionBar!!.setDisplayShowHomeEnabled(true)
            mActionBar!!.title = getString(R.string.title_activity_user_info)
        } else {
            throw IllegalArgumentException(getString(R.string.error_message_illegal_argument_exception))
        }
    }

    private fun onValidate(
        firstName: TextInputEditText,
        lastName: TextInputEditText,
        emailAddress: TextInputEditText,
    ) {
        if (!isNotEmpty(firstName, true)) {
            return
        }

        if (!isNotEmpty(lastName, true)) {
            return
        }

        if (!isNotEmpty(emailAddress, true) || !isEmailValid2(emailAddress, true)) {
            return
        }

//        mainViewModel.updateUserInfo(
//            BasicInformationData(
//                firstName = firstName,
//                lastName = lastName,
//                address = emailAddress
//            )
//        )
    }

}