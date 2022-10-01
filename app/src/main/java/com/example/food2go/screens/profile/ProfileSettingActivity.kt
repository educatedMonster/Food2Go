package com.example.food2go.screens.profile

import android.annotation.SuppressLint
import android.app.Activity
import android.app.TimePickerDialog
import android.content.ComponentName
import android.content.Intent
import android.net.ParseException
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Parcelable
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.appcompat.app.ActionBar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.food2go.R
import com.example.food2go.constants.RequestCodeTag
import com.example.food2go.constants.ServerConst.IS_SUCCESS
import com.example.food2go.constants.UserConst
import com.example.food2go.constants.UserConst.PROFILE_ROLE
import com.example.food2go.constants.UserConst.PROFILE_STATUS
import com.example.food2go.constants.UserConst.SHOP_CLOSED_STATUS
import com.example.food2go.constants.UserConst.SHOP_OPEN_STATUS
import com.example.food2go.databinding.ActivityProfileSettingBinding
import com.example.food2go.domain.ProfileDomain
import com.example.food2go.domain.UserInformationDomain
import com.example.food2go.domain.UserShopDomain
import com.example.food2go.screens.BaseActivity
import com.example.food2go.screens.main.MainViewModel
import com.example.food2go.utilities.extensions.showToast
import com.example.food2go.utilities.helpers.FileUtils
import com.example.food2go.utilities.helpers.SharedPrefs
import com.example.food2go.utilities.helpers.getSecurePrefs
import com.example.food2go.utilities.initMultiplePermission
import com.trackerteer.taskmanagement.utilities.extensions.gone
import com.trackerteer.taskmanagement.utilities.extensions.setSafeOnClickListener
import com.trackerteer.taskmanagement.utilities.extensions.visible
import timber.log.Timber
import java.io.File
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*


class ProfileSettingActivity : BaseActivity() {
    override val hideStatusBar: Boolean get() = false
    override val showBackButton: Boolean get() = true

    private var userId = 0L
    private var infoId = 0L
    private var shopId = 0L
    private var openHour: Int = 0
    private var openMinute: Int = 0
    private var closeHour: Int = 0
    private var closeMinute: Int = 0
    private lateinit var binding: ActivityProfileSettingBinding
    private var mActionBar: ActionBar? = null
    private var imageUrl: String? = null
    private var outputFileUri: Uri? = null
    private var mFile: File? = null
    private var isGetImage = false
    private var profile: ProfileDomain? = null
    private var openCloseStatus: String? = null
    private val mainViewModel: MainViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(application)
            .create(MainViewModel::class.java)
    }

    // Todo - Test dropdown
//    private var mIAutoCompleteAdapter: ArrayAdapterInstantAuto? = null
//    private var mAutoItems = ArrayList<InstantAutoItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userId = SharedPrefs(getSecurePrefs(this)).getString(UserConst.USER_ID)!!.toLong()
        infoId = SharedPrefs(getSecurePrefs(this)).getString(UserConst.INFO_ID)!!.toLong()
        shopId = SharedPrefs(getSecurePrefs(this)).getString(UserConst.SHOP_ID)!!.toLong()
        initConfig()
    }

    override fun onResume() {
        super.onResume()
        initRequest()
    }

    override fun shouldRegisterForActivityResult(): Boolean {
        return true // this will override the BaseActivity method and we can use onActivityResult
    }

    override fun onActivityResult(requestCode: Int, result: ActivityResult) {
        if (result.resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                RequestCodeTag.REQUEST_CODE_CAMERA -> {
                    val isCamera: Boolean = if (result.data == null || result.data!!.data == null) {
                        true
                    } else {
                        val action = result.data!!.action
                        action != null && action == MediaStore.ACTION_IMAGE_CAPTURE
                    }
                    val selectedImageUri: Uri?
                    if (isCamera) {
                        selectedImageUri = outputFileUri
                        if (selectedImageUri == null) return
                        Glide.with(this).load(selectedImageUri).into(binding.imageView)
                    } else {
                        selectedImageUri = result.data!!.data
                        if (selectedImageUri == null) return
                        outputFileUri = selectedImageUri
                        Glide.with(this).load(selectedImageUri).into(binding.imageView)
                    }
                    binding.imageView.visible()
                    if (outputFileUri != null) {
                        mFile = FileUtils.getFile(this, outputFileUri)!!
                    }
                    isGetImage = true
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
            R.id.open -> {
                openCloseStatus = SHOP_OPEN_STATUS
            }
            R.id.close -> {
                openCloseStatus = SHOP_CLOSED_STATUS
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initConfig() {
        initBinding()
        initActionBar()
        initMultiplePermission(this)
        initLiveData()
        initEventListener()
    }

    private fun initRequest() {
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
            mActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
            mActionBar!!.setDisplayHomeAsUpEnabled(true)
            mActionBar!!.setDisplayShowHomeEnabled(true)
            mActionBar!!.setDisplayUseLogoEnabled(true)
            binding.collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar)
            binding.collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar)
        } else {
            throw IllegalArgumentException(getString(R.string.error_message_illegal_argument_exception))
        }
    }

    private fun initLiveData() {
        mainViewModel.profile.observe(this) {
            profile = it!!
            openCloseStatus = profile!!.user_shop!!.status
            binding.toolbar.title = profile!!.user_shop!!.name
            imageUrl = it.user_shop!!.imageURL
            if (outputFileUri == null) {
                Glide.with(this).load(imageUrl)
                    .into(binding.imageView)
            }
        }

        mainViewModel.updateFormState.observe(this) {
            if (it.isSuccess.matches(IS_SUCCESS.toRegex())) {
                onBackPressed()
            }
            showToast(it.message)
        }

        mainViewModel.isUploaded.observe(this) {
            if (it) {
                setLoading(it)
            }
        }

        mainViewModel.isLoading.observe(this) {
            setLoading(it)
        }
    }

    private fun initEventListener() {
        binding.textInputChangePassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                @Suppress("DEPRECATION")
                binding.textLayoutPassword.isPasswordVisibilityToggleEnabled =
                    binding.textInputChangePassword.text.toString().isNotEmpty()
            }
        })

        binding.apply {
            buttonUpdate.setSafeOnClickListener {

                // User
                val firstName = binding.textInputFirstName.text.toString()
                val lastName = binding.textInputLastName.text.toString()
                val emailAddress = binding.textInputEmailAddress.text.toString()

                //Contact
                val inputAddress = binding.textInputAddress.text.toString()
                val primaryNumber = binding.textInputPrimaryNumber.text.toString()
                val secondNumber = binding.textInputSecondNumber.text.toString()

                //Shop
                val shopName = binding.textInputShopName.text.toString()
                val shopAddress = binding.textInputShopAddress.text.toString()
                val shopContact = binding.textInputShopContact.text.toString()
                val shopOpen = binding.textInputTimeOpen.text.toString()
                val shopClose = binding.textInputTimeClose.text.toString()
                val shopStatus = openCloseStatus!!
                val cbDay1 = binding.cbDay1.isChecked
                val cbDay2 = binding.cbDay2.isChecked
                val cbDay3 = binding.cbDay3.isChecked
                val cbDay4 = binding.cbDay4.isChecked
                val cbDay5 = binding.cbDay5.isChecked
                val cbDay6 = binding.cbDay6.isChecked
                val cbDay7 = binding.cbDay7.isChecked
                val cbGcash = binding.cbGcash.isChecked
                val cbCod = binding.cbCod.isChecked
                val deliveryCharge = binding.textInputDeliveryCharge.text.toString()
                val password = binding.textInputChangePassword.text.toString()

                val userInfo = UserInformationDomain(
                    id = infoId,
                    user_id = userId,
                    complete_address = inputAddress,
                    primary_contact = primaryNumber,
                    secondary_contact = secondNumber
                )

                val userShop = UserShopDomain(
                    id = shopId,
                    user_id = userId,
                    name = shopName,
                    address = shopAddress,
                    contact = shopContact,
                    open_hour = set24Hours(shopOpen),
                    close_hour = set24Hours(shopClose),
//                    status = onValidateStatus(shopStatus),
                    status = shopStatus,
                    monday = onValidateCb(cbDay1),
                    tuesday = onValidateCb(cbDay2),
                    wednesday = onValidateCb(cbDay3),
                    thursday = onValidateCb(cbDay4),
                    friday = onValidateCb(cbDay5),
                    saturday = onValidateCb(cbDay6),
                    sunday = onValidateCb(cbDay7),
                    pm_gcash = onValidateCb(cbGcash),
                    pm_cod = onValidateCb(cbCod),
                    is_active = 1L,
                    delivery_charge = deliveryCharge,
                    imageURL = imageUrl,
                    description = ""
                )

                val userProfile = ProfileDomain(
                    id = userId,
                    firstName = firstName,
                    lastName = lastName,
                    fullName = "$firstName $lastName",
                    email = emailAddress,
                    password = password,
                    status = PROFILE_STATUS,
                    role = PROFILE_ROLE,
                    userInformation = userInfo,
                    user_shop = userShop
                )

                if (isGetImage) {
                    mainViewModel!!.updateUserInfo(userProfile, mFile!!)
                } else {
                    mainViewModel!!.updateUserInfo(userProfile, null)
                }
            }

            layoutContactUser.setOnClickListener {
                if (layoutIncludeUser.visibility == View.VISIBLE) {
                    layoutIncludeUser.gone()
                } else {
                    layoutIncludeUser.visible()
                }
            }

            layoutContactInfo.setOnClickListener {
                if (layoutIncludeContact.visibility == View.VISIBLE) {
                    layoutIncludeContact.gone()
                } else {
                    layoutIncludeContact.visible()
                }
            }

            layoutShopInfo.setOnClickListener {
                if (layoutIncludeShop.visibility == View.VISIBLE) {
                    layoutIncludeShop.gone()
                } else {
                    layoutIncludeShop.visible()
                }
            }

            imageView.setSafeOnClickListener {
                startGettingImage()
            }

        }
    }

    private fun setLoading(set: Boolean) {
        try {
            if (set) {
                binding.shimmerViewContainer.startShimmer()
                binding.shimmerViewContainer.visible()
                binding.constraintLayoutHeaderContent.gone()
            } else {
                binding.shimmerViewContainer.stopShimmer()
                binding.shimmerViewContainer.gone()
                binding.constraintLayoutHeaderContent.visible()
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    private fun onValidateCb(b: Boolean): Long {
        return if (b) 1L else 0L
    }

    private fun onValidateStatus(b: Boolean): String {
        return if (b) SHOP_OPEN_STATUS else SHOP_CLOSED_STATUS
    }

    fun popupOpeningTimePicker(view: View) {
        val timePickerDialog = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
            this.openHour = hour
            this.openMinute = minute

            val timeSet: String
            when {
                openHour > 12 -> {
                    openHour -= 12
                    timeSet = "PM"
                }
                openHour == 0 -> {
                    openHour += 12
                    timeSet = "AM"
                }
                openHour == 12 -> timeSet = "PM"
                else -> timeSet = "AM"
            }
            val minuteString = if (openMinute < 10) "0$openMinute" else "$openMinute"
            binding.textInputTimeOpen.text = "$openHour:$minuteString $timeSet"
        }
        val timePiker = TimePickerDialog(this, R.style.DialogTheme, timePickerDialog, openHour, openMinute, false)
        timePiker.setTitle("Select Opening Time")
        timePiker.show()
    }

    fun popupClosingTimePicker(view: View) {
        val timePickerDialog = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
            this.closeHour = hour
            this.closeMinute = minute

            val timeSet: String
            when {
                closeHour > 12 -> {
                    closeHour -= 12
                    timeSet = "PM"
                }
                closeHour == 0 -> {
                    closeHour += 12
                    timeSet = "AM"
                }
                closeHour == 12 -> timeSet = "PM"
                else -> timeSet = "AM"
            }
            val minuteString = if (closeMinute < 10) "0$closeMinute" else "$closeMinute"
            binding.textInputTimeClose.text = "$closeHour:$minuteString $timeSet"
        }

        val timePiker =
            TimePickerDialog(this, R.style.DialogTheme, timePickerDialog, closeHour, closeMinute, false)
        timePiker.setTitle("Select Opening Time")
        timePiker.show()
    }

    private fun set24Hours(timeComeFromServer: String): String {
        val timeComeFromServer2 = timeComeFromServer.uppercase(Locale.getDefault())
        var time: LocalTime? = null
        var formatter: DateTimeFormatter? = null
        try {
            val parser: DateTimeFormatter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH)
            } else {
                TODO("VERSION.SDK_INT < O")
            }

            formatter = DateTimeFormatter.ofPattern("h:mm a")
            time = LocalTime.parse(timeComeFromServer2, parser)!!
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return time!!.format(formatter!!)
    }

    private fun startGettingImage() {
        val fileName = "foodtwogo-${System.currentTimeMillis()}"

        @Suppress("DEPRECATION")
        val rootDirectory =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val imageFile = File.createTempFile(fileName, ".jpg", rootDirectory)
        outputFileUri = Uri.fromFile(imageFile)
        setFileChooser()
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun setFileChooser() {
        val intentCameraArray = ArrayList<Intent>()
        val intentCapture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val cameraList = packageManager.queryIntentActivities(intentCapture, 0)
        for (cl in cameraList) {
            val packageName = cl.activityInfo.packageName
            val intent = Intent(intentCapture)
            intent.component = ComponentName(cl.activityInfo.packageName, cl.activityInfo.name)
            intent.setPackage(packageName)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri)
            intentCameraArray.add(intent)
        }
        val intentGallery = Intent()
        intentGallery.type = "image/*"
        intentGallery.action = Intent.ACTION_PICK

        val intentChooser =
            Intent.createChooser(intentGallery, getString(R.string.title_select_image))
        intentChooser.putExtra(
            Intent.EXTRA_INITIAL_INTENTS,
            intentCameraArray.toTypedArray<Parcelable>()
        )
        startActivityForResult(RequestCodeTag.REQUEST_CODE_CAMERA, intentChooser)
    }
}
