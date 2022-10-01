package com.example.food2go.screens.weekly_payment

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import androidx.activity.result.ActivityResult
import androidx.appcompat.app.ActionBar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.food2go.R
import com.example.food2go.constants.DialogTag
import com.example.food2go.constants.PusherConst
import com.example.food2go.constants.UserConst
import com.example.food2go.databinding.ActivityWeeklyPaymentBinding
import com.example.food2go.databinding.DialogWeeklyPaymentUrlBinding
import com.example.food2go.domain.WeeklyPaymentDomain
import com.example.food2go.screens.BaseActivity
import com.example.food2go.screens.weekly_payment.adapter.WeeklyPaymentAdapter
import com.example.food2go.screens.weekly_payment.dialog.WeeklyPaymentDialog
import com.example.food2go.utilities.decorator.DividerItemDecoration
import com.example.food2go.utilities.getDialog
import com.example.food2go.utilities.helpers.SharedPrefs
import com.example.food2go.utilities.helpers.WeeklyPaymentRecyclerClick
import com.example.food2go.utilities.helpers.getSecurePrefs
import com.example.food2go.utilities.loadItemImage
import com.trackerteer.taskmanagement.utilities.extensions.gone
import com.trackerteer.taskmanagement.utilities.extensions.visible
import kotlinx.android.synthetic.main.fragment_pending.view.*
import timber.log.Timber
import java.io.File
import java.util.ArrayList

class WeeklyPaymentActivity : BaseActivity() {
    override val hideStatusBar: Boolean get() = false
    override val showBackButton: Boolean get() = true

    private var userId = 0L
    private lateinit var binding: ActivityWeeklyPaymentBinding
    private var mAdapter: WeeklyPaymentAdapter? = null
    private var mActionBar: ActionBar? = null
    private val weeklyPaymentViewModel: WeeklyPaymentViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(application)
            .create(WeeklyPaymentViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userId =
            SharedPrefs(getSecurePrefs(this)).getString(UserConst.USER_ID)!!.toLong()
        initConfig()
    }

    private fun initConfig() {
        initExtras()
        initBinding()
        initActionBar()
        initAdapter()
        initViews()
        initLiveData()
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

    private fun initExtras() {
        val merchantId = intent.getLongExtra(PusherConst.MERCHANT_ID, 0L)
        weeklyPaymentViewModel.getWeeklyPayment(merchantId)
    }

    private fun initBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_weekly_payment)
        binding.lifecycleOwner = this
        binding.weeklyPaymentViewModel = weeklyPaymentViewModel
    }

    private fun initActionBar() {
        setSupportActionBar(binding.toolbar)
        mActionBar = supportActionBar
        if (mActionBar != null) {
            mActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
            mActionBar!!.setDisplayHomeAsUpEnabled(true)
            mActionBar!!.setDisplayShowHomeEnabled(true)
            mActionBar!!.setDisplayUseLogoEnabled(true)
            mActionBar!!.title = getString(R.string.title_activity_weekly_payment)
        } else {
            throw IllegalArgumentException(getString(R.string.error_message_illegal_argument_exception))
        }
    }

    private fun initAdapter() {
        mAdapter = WeeklyPaymentAdapter(
            context = this,
            onClickCallBack = WeeklyPaymentRecyclerClick(
                click = {
                    val payment = it as WeeklyPaymentDomain
                    val dialog = WeeklyPaymentDialog(
                        listener = object : WeeklyPaymentDialog.Listener {
                            override fun onWeeklyPaymentListener(
                                payment: WeeklyPaymentDomain,
                                file: File,
                            ) {
                                weeklyPaymentViewModel.uploadWeeklyPaymentImage(payment.id, file)
                            }
                        },
                        payment = payment

                    )
                    dialog.show(supportFragmentManager, DialogTag.DIALOG_WEEKLY_PAYMENT)
                },
                proofURL = {
                    showProofDialog(it as WeeklyPaymentDomain)
                }
            ),
            viewModel = weeklyPaymentViewModel
        )
    }

    private fun showProofDialog(payment: WeeklyPaymentDomain) {
        val alertDialog = AlertDialog.Builder(this)
        val binding = DataBindingUtil.inflate<DialogWeeklyPaymentUrlBinding>(
            LayoutInflater.from(this),
            R.layout.dialog_weekly_payment_url,
            null,
            false
        )

        alertDialog.setView(binding.root)
        val dialog = alertDialog.create()

        binding.textTitle.text = payment.weekBalance
        loadItemImage(binding.imageProof, payment.proofURL!!)

        binding.buttonClose.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun initViews() {
        binding.root.recycler_view.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    R.drawable.list_divider_decoration
                )
            )
        }.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (!recyclerView.canScrollVertically(1)) {

                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })

        binding.swipeRefreshLayout.setOnRefreshListener {
            mAdapter!!.clearAdapter()
            initRequest()
        }
    }

    private fun initLiveData() {
        weeklyPaymentViewModel.apply {
            weeklyPayment.observe(this@WeeklyPaymentActivity) {
                when {
                    it.result.isNotEmpty() -> {
                        binding.layoutEmpty.root.gone()
                        mAdapter!!.addData(it.result as ArrayList<WeeklyPaymentDomain>)

                    }
                    mAdapter!!.itemCount == 0 || it.result.isEmpty() -> {
                        binding.layoutEmpty.root.visible()
                    }
                    else -> {
                        binding.layoutEmpty.root.gone()
                    }
                }
            }

            isUploaded.observe(this@WeeklyPaymentActivity) {
                (getDialog(this@WeeklyPaymentActivity,
                    DialogTag.DIALOG_WEEKLY_PAYMENT) as WeeklyPaymentDialog?)?.dismiss()
                mAdapter!!.clearAdapter()
                initRequest()
            }

            isLoading.observe(this@WeeklyPaymentActivity) {
                setLoading(it)
            }
        }
    }

    fun initRequest() {
        weeklyPaymentViewModel.getWeeklyPayment(userId)
    }

    private fun setLoading(set: Boolean) {
        try {
            binding.apply {
                swipeRefreshLayout.isRefreshing = set
                if (set) {
                    shimmerViewContainer.startShimmer()
                    shimmerViewContainer.visible()
                    recyclerView.gone()
                } else {
                    shimmerViewContainer.stopShimmer()
                    shimmerViewContainer.gone()
                    recyclerView.visible()
                }
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
    }
}