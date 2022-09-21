package com.example.kafiesta.screens.profile

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
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.kafiesta.R
import com.example.kafiesta.constants.RequestCodeTag
import com.example.kafiesta.constants.ServerConst.IS_SUCCESS
import com.example.kafiesta.constants.UserConst
import com.example.kafiesta.constants.UserConst.PROFILE_ROLE
import com.example.kafiesta.constants.UserConst.PROFILE_STATUS
import com.example.kafiesta.databinding.ActivityProfileSettingBinding
import com.example.kafiesta.databinding.LayoutCustomToolbarShopBinding
import com.example.kafiesta.domain.ProfileDomain
import com.example.kafiesta.domain.UserInformationDomain
import com.example.kafiesta.domain.UserShopDomain
import com.example.kafiesta.screens.BaseActivity
import com.example.kafiesta.screens.main.MainViewModel
import com.example.kafiesta.utilities.extensions.showToast
import com.example.kafiesta.utilities.helpers.FileUtils
import com.example.kafiesta.utilities.helpers.SharedPrefs
import com.example.kafiesta.utilities.helpers.getSecurePrefs
import com.example.kafiesta.utilities.initMultiplePermission
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
    private lateinit var toolbarShopBinding: LayoutCustomToolbarShopBinding
    private var mActionBar: ActionBar? = null
    private var imageUrl: String? = null
    private var outputFileUri: Uri? = null
    private var mFile: File? = null
    private var isGetImage = false
    private val style =
        androidx.navigation.ui.ktx.R.style.Base_Theme_MaterialComponents_Light_Dialog
    private val mainViewModel: MainViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(application)
            .create(MainViewModel::class.java)
    }

    // Todo - Test dropdown
//    private var mIAutoCompleteAdapter: ArrayAdapterInstantAuto? = null
//    private var mAutoItems = ArrayList<InstantAutoItem>()

    // Todo - Get the selected image path here
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                RequestCodeTag.REQUEST_CODE_CAMERA -> {
                    val isCamera: Boolean = if (data == null || data.data == null) {
                        true
                    } else {
                        val action = data.action
                        action != null && action == MediaStore.ACTION_IMAGE_CAPTURE
                    }
                    val selectedImageUri: Uri?
                    if (isCamera) {
                        selectedImageUri = outputFileUri
                        if (selectedImageUri == null) return
                        Glide.with(this).load(selectedImageUri).into(binding.imageView)
                    } else {
                        selectedImageUri = data!!.data
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initConfig() {
        initBinding()
        initActionBar()
        initMultiplePermission(this)
        initLiveData()
        initEventListener()
//        initAutoCompleteAdapter()
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
            imageUrl = it!!.user_shop!!.imageURL
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
        toolbarShopBinding.activeInactiveToggleStore.setOnClickListener {
            if (toolbarShopBinding.activeInactiveToggleStore.isChecked) toolbarShopBinding.activeInactiveText.text =
                "Open" else toolbarShopBinding.activeInactiveText.text = "Closed"
        }

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

                val shopStatus = toolbarShopBinding.activeInactiveToggleStore.isChecked
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

                //Todo - Test dropdown
//                if (hasSelected(
//                        binding.autoCompleteSample,
//                        mIAutoCompleteAdapter!!,
//                        true,
//                        R.string.invalid_auto_complete)
//                ) {
//                    return@setSafeOnClickListener
//                }
//
//                val selected: String = mIAutoCompleteAdapter?.getSelected()!!.id

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
                    status = onValidateStatus(shopStatus),
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
        return if (b) "open" else "closed"
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
        val timePiker = TimePickerDialog(this, style, timePickerDialog, openHour, openMinute, false)
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
            TimePickerDialog(this, style, timePickerDialog, closeHour, closeMinute, false)
        timePiker.setTitle("Select Opening Time")
        timePiker.show()
    }

    private fun set24Hours(timeComeFromServer: String): String {
        var time: LocalTime? = null
        var formatter: DateTimeFormatter? = null
        try {
            val parser: DateTimeFormatter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH)
            } else {
                TODO("VERSION.SDK_INT < O")
            }

            formatter = DateTimeFormatter.ofPattern("HH:mm")
            time = LocalTime.parse(timeComeFromServer, parser)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return time!!.format(formatter)
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
        startActivityForResult(intentChooser, RequestCodeTag.REQUEST_CODE_CAMERA)
    }

//    private fun initAutoCompleteAdapter() {
//        getListFromArrayResource(mAutoItems,
//            R.array.status_values,
//            R.array.status_texts)
//        mIAutoCompleteAdapter = ArrayAdapterInstantAuto(this,
//            R.layout.instant_auto_complete_item_simple_text,
//            mAutoItems)
//        initializeAutoComplete(
//            binding.autoCompleteSample,
//            mIAutoCompleteAdapter!!)
//    }

//    private fun getListFromArrayResource(
//        list: ArrayList<InstantAutoItem>,
//        @ArrayRes valueRes: Int,
//        @ArrayRes textRes: Int,
//    ) {
//        val ids = this.resources.getStringArray(valueRes)
//        val texts = this.resources.getStringArray(textRes)
//        for (i in ids.indices) {
//            list.add(InstantAutoItem(ids[i], texts[i]))
//        }
//    }
//
//    private fun initializeAutoComplete(
//        autoComplete: InstantAutoComplete,
//        adapter: ArrayAdapterInstantAuto,
//    ) {
//        autoComplete.threshold = 0 //will start working from first character
//        autoComplete.setShowUnfilteredListWhenClicked(true)
//        autoComplete.setAdapter(adapter)
//        autoComplete.onItemClickListener =
//            AdapterView.OnItemClickListener { parent1: AdapterView<*>, view: View?, position: Int, id: Long ->
//                autoComplete.error = null
//                val item = parent1.getItemAtPosition(position) as InstantAutoItem
//                adapter.setSelected(item)
//
//                Timber.d("ITEM $item.id : $item.text")
//                Timber.d("ADAPTER ITEM ${adapter.getSelected()!!.text}")
//
//            }
//        autoComplete.addTextChangedListener(InstantAutoTextWatcher(adapter))
//    }
}
