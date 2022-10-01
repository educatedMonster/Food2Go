package com.example.food2go.screens.report

import android.app.Activity
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.RadioButton
import androidx.activity.result.ActivityResult
import androidx.appcompat.app.ActionBar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.food2go.R
import com.example.food2go.constants.UserConst
import com.example.food2go.databinding.ActivityReportBinding
import com.example.food2go.databinding.LayoutCustomToolbarSearchBinding
import com.example.food2go.domain.ItemSold
import com.example.food2go.domain.ReportBaseNetworkDomain
import com.example.food2go.screens.BaseActivity
import com.example.food2go.screens.report.adapter.SoldOrderAdapter
import com.example.food2go.utilities.decorator.DividerItemDecoration
import com.example.food2go.utilities.extensions.showToast
import com.example.food2go.utilities.helpers.SharedPrefs
import com.example.food2go.utilities.helpers.getSecurePrefs
import com.example.food2go.utilities.shakeView
import com.trackerteer.taskmanagement.utilities.extensions.gone
import com.trackerteer.taskmanagement.utilities.extensions.setSafeOnClickListener
import com.trackerteer.taskmanagement.utilities.extensions.visible
import timber.log.Timber
import java.util.*


class ReportActivity : BaseActivity() {

    override val hideStatusBar: Boolean get() = false
    override val showBackButton: Boolean get() = false

    private var userId = 0L
    private var mActionBar: ActionBar? = null
    private var report: ReportBaseNetworkDomain? = null
    private var dateFrom: String? = null
    private var dateTo: String? = null
    private var isButtonSalesClick: Boolean = false
    private lateinit var mAdapter: SoldOrderAdapter

    private lateinit var binding: ActivityReportBinding
    private lateinit var toolbarAddBinding: LayoutCustomToolbarSearchBinding
    private val reportViewModel: ReportViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory(application)
            .create(ReportViewModel::class.java)
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

    override fun shouldRegisterForActivityResult(): Boolean {
        return true // this will override the BaseActivity method and we can use onActivityResult
    }

    override fun onActivityResult(requestCode: Int, result: ActivityResult) {
        if (result.resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                // ToDo : requestCode here

            }
        }
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
        initLiveData()
        initEventListener()
        initAdapter()
    }

    private fun initBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_report)
        binding.lifecycleOwner = this
        binding.reportViewModel = reportViewModel
    }

    private fun initActionBar() {
        setSupportActionBar(binding.toolbar)
        mActionBar = supportActionBar
        if (mActionBar != null) {
            mActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
            mActionBar!!.setDisplayHomeAsUpEnabled(true)
            mActionBar!!.setDisplayShowHomeEnabled(true)
            mActionBar!!.setDisplayUseLogoEnabled(true)
            toolbarAddBinding = DataBindingUtil.inflate(
                LayoutInflater.from(this),
                R.layout.layout_custom_toolbar_search,
                null,
                false
            )
            toolbarAddBinding.textViewTitle.text = getString(R.string.title_nav_drawer_report)
            toolbarAddBinding.lifecycleOwner = this
            mActionBar!!.customView = toolbarAddBinding.root
        } else {
            throw IllegalArgumentException(getString(R.string.error_message_illegal_argument_exception))
        }
    }

    private fun initLiveData() {
        reportViewModel.isLoading.observe(this) {
            setLoading(it)
        }

        reportViewModel.salesReport.observe(this) {
            binding.report = it
            report = it
            report!!.result.itemsSold.map { item ->
                mAdapter.addData(ItemSold(item.key, item.value))
                Log.d("ITEMS OLD", "${item.key} -  ${item.value}")
            }
        }

        reportViewModel.eodReport.observe(this) {
            binding.report = it
            report = it
            report!!.result.itemsSold.map { item ->
                mAdapter.addData(ItemSold(item.key, item.value))
                Log.d("ITEMS OLD", "${item.key} -  ${item.value}")
            }
        }
    }

    private fun initEventListener() {
        // This overrides the radiogroup onCheckListener
        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            // This will get the radiobutton that has changed in its check state
            val checkedRadioButton = group.findViewById<View>(checkedId) as RadioButton
            // This puts the value (true/false) into the variable
            val isChecked = checkedRadioButton.isChecked
            val tag = checkedRadioButton.tag
//            // If the radiobutton that has changed in check state is now checked...
            if (isChecked && tag.equals("eod")) {
                showToast("Checked: ${checkedRadioButton.text}")
                binding.layoutDate.gone()
                isButtonSalesClick = false
                dateFrom = null
                dateTo = null
                binding.textInputDateFrom.text = null
                binding.textInputDateTo.text = null
                binding.textInputDateFrom.hint = getString(R.string.activity_report_date_from)
                binding.textInputDateTo.hint = getString(R.string.activity_report_date_to)
            } else {
                showToast("Checked: ${checkedRadioButton.text}")
                binding.layoutDate.visible()
                isButtonSalesClick = true
            }
        }

        toolbarAddBinding.imgMore.setSafeOnClickListener {
            mAdapter.clearAdapter()
            if (isButtonSalesClick) {
                if (dateFrom == null) {
                    shakeView(binding.textInputDateFrom, 10, 5)
                    showToast("Select Date From!")
                    return@setSafeOnClickListener
                }
                if (dateTo == null) {
                    shakeView(binding.textInputDateTo, 10, 5)
                    showToast("Select Date To!")
                    return@setSafeOnClickListener
                }
                reportViewModel.getSalesReport(
                    dateFrom!!,
                    dateTo!!
                )
            } else {
                reportViewModel.getEODReport()
            }
        }
    }

    private fun setLoading(set: Boolean) {
        try {
            if (set) {
//                binding.shimmerViewContainer.startShimmer()
//                binding.shimmerViewContainer.visible()
//                binding.constraintLayoutHeaderContent.gone()
            } else {
//                binding.shimmerViewContainer.stopShimmer()
//                binding.shimmerViewContainer.gone()
//                binding.constraintLayoutHeaderContent.visible()
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    private fun initAdapter() {
        mAdapter = SoldOrderAdapter(context = this)

        binding.rcReport.apply {
            adapter = mAdapter
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    R.drawable.list_divider_decoration
                )
            )
        }
    }

    fun popupDatePicker(dateView: View) {
        // Get Current Date
        val c: Calendar = Calendar.getInstance()
        val mYear = c.get(Calendar.YEAR)
        val mMonth = c.get(Calendar.MONTH)
        val mDay = c.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog: DatePickerDialog
        if (dateView.id == R.id.text_input_date_from) {
            datePickerDialog = DatePickerDialog(this, R.style.DialogTheme,
                { _, year, monthOfYear, dayOfMonth ->
//                "2022-09-20"
                    val zeroMonthOfYear = String.format("%02d", monthOfYear + 1)
                    val zeroDayOfMonth = String.format("%02d", dayOfMonth)
                    dateFrom = "$year-$zeroMonthOfYear-$zeroDayOfMonth"
                    binding.textInputDateFrom.text = dateFrom
                },
                mYear,
                mMonth,
                mDay)
        } else {
            datePickerDialog = DatePickerDialog(this, R.style.DialogTheme,
                { _, year, monthOfYear, dayOfMonth ->
//                "2022-09-20"
                    val zeroMonthOfYear = String.format("%02d", monthOfYear + 1)
                    val zeroDayOfMonth = String.format("%02d", dayOfMonth)
                    dateTo = "$year-$zeroMonthOfYear-$zeroDayOfMonth"
                    binding.textInputDateTo.text = dateTo
                },
                mYear,
                mMonth,
                mDay)
        }
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()
    }
}