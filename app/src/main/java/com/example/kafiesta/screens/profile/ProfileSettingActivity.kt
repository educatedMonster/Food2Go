package com.example.kafiesta.screens.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.kafiesta.R
import com.example.kafiesta.constants.ServerConst.IS_SUCCESS
import com.example.kafiesta.constants.UserConst
import com.example.kafiesta.databinding.ActivityProfileSettingBinding
import com.example.kafiesta.databinding.LayoutCustomToolbarShopBinding
import com.example.kafiesta.domain.ProfileDomain
import com.example.kafiesta.domain.UserInformationsDomain
import com.example.kafiesta.domain.UserShopDomain
import com.example.kafiesta.screens.BaseActivity
import com.example.kafiesta.screens.main.MainViewModel
import com.example.kafiesta.utilities.extensions.isEmailValid
import com.example.kafiesta.utilities.extensions.isNotEmpty
import com.example.kafiesta.utilities.extensions.showToast
import com.example.kafiesta.utilities.helpers.SharedPrefs
import com.example.kafiesta.utilities.helpers.getSecurePrefs
import com.google.android.material.textfield.TextInputEditText
import com.trackerteer.taskmanagement.utilities.extensions.gone
import com.trackerteer.taskmanagement.utilities.extensions.setSafeOnClickListener
import com.trackerteer.taskmanagement.utilities.extensions.visible
import timber.log.Timber

class ProfileSettingActivity : BaseActivity() {
    override val hideStatusBar: Boolean get() = false
    override val showBackButton: Boolean get() = true

    private var userId = 0L
    private var infoId = 0L
    private var shopId = 0L
    private lateinit var binding: ActivityProfileSettingBinding
    private lateinit var toolbarShopBinding: LayoutCustomToolbarShopBinding
    private var mActionBar: ActionBar? = null

    private val mainViewModel: MainViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(application)
            .create(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userId = SharedPrefs(getSecurePrefs(this)).getString(UserConst.USER_ID)!!.toLong()
        infoId = SharedPrefs(getSecurePrefs(this)).getString(UserConst.INFO_ID)?.toLong()?: 0L
        shopId = SharedPrefs(getSecurePrefs(this)).getString(UserConst.SHOP_ID)?.toLong()?: 0L
        initConfig()
    }

    override fun onResume() {
        super.onResume()
        initRequest()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initConfig() {
        initRequest()
        initBinding()
        initActionBar()
        initLiveData()
        initEventListener()
    }

    private fun initRequest() {
        requestMainViewModel()
    }

    private fun requestMainViewModel() {
        mainViewModel.getMe()
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
            toolbarShopBinding = DataBindingUtil.inflate(
                LayoutInflater.from(this),
                R.layout.layout_custom_toolbar_shop,
                null,
                false
            )
            toolbarShopBinding.textViewTitle.text = getString(R.string.title_activity_shop)
            toolbarShopBinding.lifecycleOwner = this
            toolbarShopBinding.mainViewModel = mainViewModel
            mActionBar!!.customView = toolbarShopBinding.root
        } else {
            throw IllegalArgumentException(getString(R.string.error_message_illegal_argument_exception))
        }
    }

    private fun initLiveData() {
        mainViewModel.profile.observe(this) {

        }

        mainViewModel.data.observe(this) {
            if(it.status.matches("success".toRegex())){
                showToast(it.result.fullName)
            }
        }

        mainViewModel.updateFormState.observe(this) {
            if (it.isSuccess.matches(IS_SUCCESS.toRegex())) {
                onBackPressed()
            }
            showToast(it.message)
        }

        mainViewModel.isLoading.observe(this) {
            setLoading(it)
        }
    }

    private fun initEventListener() {
        binding.apply {
            buttonUpdate.setSafeOnClickListener {

                // User
                val firstName = binding.textInputFirstName.text.toString()
                val lastName = binding.textInputLastName.text.toString()
                val emailAddress = binding.textInputEmailAddress.text.toString()

                //Contact
                val textInputAddress = binding.textInputAddress.text.toString()
                val textInputPrimaryNumber = binding.textInputPrimaryNumber.text.toString()
                val textInputSecondNumber = binding.textInputSecondNumber.text.toString()

                //Shop
                val textInputShopName = binding.textInputShopName.text.toString()
                val textInputShopAddress = binding.textInputShopAddress.text.toString()
                val textInputShopContact = binding.textInputShopContact.text.toString()
                val cbDay1 = binding.cbDay1.isChecked
                val cbDay2 = binding.cbDay2.isChecked
                val cbDay3 = binding.cbDay3.isChecked
                val cbDay4 = binding.cbDay4.isChecked
                val cbDay5 = binding.cbDay5.isChecked
                val cbDay6 = binding.cbDay6.isChecked
                val cbDay7 = binding.cbDay7.isChecked
                val cbGcash = binding.cbGcash.isChecked
                val cbCod = binding.cbCod.isChecked

                val userInfo = UserInformationsDomain(
                    id = infoId,
                    user_id = userId,
                    complete_address = textInputAddress,
                    primary_contact = textInputPrimaryNumber,
                    secondary_contact = textInputSecondNumber
                )

                val userShop = UserShopDomain(
                    id = shopId,
                    user_id = userId,
                    name = textInputShopName,
                    address = textInputShopAddress,
                    contact = textInputShopContact,
                    open_hour = null,
                    close_hour = null,
                    status = "open",
                    monday = onValidateCb(cbDay1),
                    tuesday = onValidateCb(cbDay2),
                    wednesday = onValidateCb(cbDay3),
                    thursday = onValidateCb(cbDay4),
                    friday = onValidateCb(cbDay5),
                    saturday = onValidateCb(cbDay6),
                    sunday = onValidateCb(cbDay7),
                    pm_gcash = onValidateCb(cbGcash),
                    pm_cod = onValidateCb(cbCod),
                    is_active = 1L
                )

                val a = ProfileDomain(
                    id = userId,
                    firstName = firstName,
                    lastName = lastName,
                    fullName = "$firstName $lastName",
                    email = emailAddress,
                    status = "active",
                    role = "client",
                    userInformations = userInfo,
                    user_shop = userShop
                )

                mainViewModel?.updateUserInfo(a)
            }

            layoutContactUser.setOnClickListener {
                if (this.layoutIncludeUser.visibility == View.VISIBLE) {
                    this.layoutIncludeUser.gone()
                } else {
                    this.layoutIncludeUser.visible()
                }
            }

            layoutContactInfo.setOnClickListener {
                if (this.layoutIncludeContact.visibility == View.VISIBLE) {
                    this.layoutIncludeContact.gone()
                } else {
                    this.layoutIncludeContact.visible()
                }
            }

            layoutShopInfo.setOnClickListener {
                if (this.layoutIncludeShop.visibility == View.VISIBLE) {
                    this.layoutIncludeShop.gone()
                } else {
                    this.layoutIncludeShop.visible()
                }
            }

            circleAddProduct.setSafeOnClickListener {
                showToast("Image click: startGettingImage")
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

    private fun onValidate(
        firstName: TextInputEditText,
        lastName: TextInputEditText,
        emailAddress: TextInputEditText,
    ): Boolean {
        if (!isNotEmpty(firstName, true)) {
            return false
        } else if (!isNotEmpty(lastName, true)) {
            return false
        } else if (!isNotEmpty(emailAddress, true)) {
            return false
        } else if (!isEmailValid(emailAddress, true)) {
            return false
        }
        return true

        Timber.d("Validate")

    }

    private fun onValidateCb(b: Boolean): Long {
        return if (b) 1L else 0L
    }
}