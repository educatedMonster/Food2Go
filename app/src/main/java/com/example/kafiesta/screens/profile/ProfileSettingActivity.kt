package com.example.kafiesta.screens.profile

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.kafiesta.R
import com.example.kafiesta.constants.UserConst
import com.example.kafiesta.databinding.ActivityProfileSettingBinding
import com.example.kafiesta.screens.BaseActivity
import com.example.kafiesta.screens.main.MainViewModel
import com.example.kafiesta.screens.profile.sub.ContactInfoActivity
import com.example.kafiesta.screens.profile.sub.ShopInfoActivity
import com.example.kafiesta.screens.profile.sub.UserInfoActivity
import com.example.kafiesta.utilities.helpers.SharedPrefs
import com.example.kafiesta.utilities.helpers.getSecurePrefs
import com.trackerteer.taskmanagement.utilities.extensions.setSafeOnClickListener
import timber.log.Timber

class ProfileSettingActivity : BaseActivity() {
    override val hideStatusBar: Boolean get() = false
    override val showBackButton: Boolean get() = true

    private var userId = 0L
    private lateinit var binding: ActivityProfileSettingBinding
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
        initActionBar()
        initLiveData()
        initEventListener()
    }

    private fun initRequest() {
        requestMainViewModel()
    }

    private fun requestMainViewModel() {
        mainViewModel.getUserId(userId)
    }

    private fun initBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile_setting)
        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel
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

    private fun initLiveData() {
        mainViewModel.isLoading.observe(this) {
            setLoading(it)
        }
    }

    private fun initEventListener() {
        binding.apply {
            layoutUserInfo.setSafeOnClickListener {
                proceedToActivity(UserInfoActivity::class.java)
            }
            layoutContactInfo.setSafeOnClickListener {
                proceedToActivity(ContactInfoActivity::class.java)
            }
            layoutShopInfo.setSafeOnClickListener {
                proceedToActivity(ShopInfoActivity::class.java)
            }
            circleImageView.setSafeOnClickListener {
//                startGettingImage()
            }
        }
    }

    private fun setLoading(set: Boolean) {
        try {
            if (set) {
                binding.shimmerViewContainer.startShimmer()
                binding.shimmerViewContainer.visibility = View.VISIBLE
                binding.constraintLayoutHeaderContent.visibility = View.GONE
            } else {
                binding.shimmerViewContainer.stopShimmer()
                binding.shimmerViewContainer.visibility = View.GONE
                binding.constraintLayoutHeaderContent.visibility = View.VISIBLE
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
    }
}