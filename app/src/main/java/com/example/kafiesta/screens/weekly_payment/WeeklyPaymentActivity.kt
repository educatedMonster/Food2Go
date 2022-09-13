package com.example.kafiesta.screens.weekly_payment

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kafiesta.R
import com.example.kafiesta.constants.DialogTag
import com.example.kafiesta.constants.UserConst
import com.example.kafiesta.databinding.ActivityWeeklyPaymentBinding
import com.example.kafiesta.domain.WeeklyPaymentDomain
import com.example.kafiesta.screens.BaseActivity
import com.example.kafiesta.screens.weekly_payment.adapter.WeeklyPaymentAdapter
import com.example.kafiesta.screens.weekly_payment.dialog.WeeklyPaymentDialog
import com.example.kafiesta.utilities.decorator.DividerItemDecoration
import com.example.kafiesta.utilities.getDialog
import com.example.kafiesta.utilities.helpers.RecyclerClick
import com.example.kafiesta.utilities.helpers.SharedPrefs
import com.example.kafiesta.utilities.helpers.getSecurePrefs
import com.trackerteer.taskmanagement.utilities.extensions.gone
import com.trackerteer.taskmanagement.utilities.extensions.visible
import kotlinx.android.synthetic.main.fragment_pending.view.*
import timber.log.Timber
import java.io.File

class WeeklyPaymentActivity : BaseActivity() {
    override val hideStatusBar: Boolean get() = false
    override val showBackButton: Boolean get() = true

    private var userId = 0L
    private lateinit var binding: ActivityWeeklyPaymentBinding
    private lateinit var mAdapter: WeeklyPaymentAdapter
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
        initRequest()
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
            binding.toolbar.title = getString(R.string.title_activity_weekly_payment)
        } else {
            throw IllegalArgumentException(getString(R.string.error_message_illegal_argument_exception))
        }
    }

    private fun initAdapter() {
        mAdapter = WeeklyPaymentAdapter(
            context = this,
            onClickCallBack = RecyclerClick(
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
                }
            ),
            viewModel = weeklyPaymentViewModel
        )
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
            initRequest()
        }
    }

    private fun initLiveData() {
        weeklyPaymentViewModel.apply {
            weeklyPayment.observe(this@WeeklyPaymentActivity) {
                when {
                    it.result.isNotEmpty() -> {
                        binding.layoutEmptyTask.root.gone()
                        it.result.forEach { model ->
                            mAdapter.addData(model)
                        }
                    }
                    mAdapter.itemCount == 0 -> {
                        binding.layoutEmptyTask.root.visible()
                    }
                    else -> {
                        binding.layoutEmptyTask.root.gone()
                    }
                }
            }

            isUploaded.observe(this@WeeklyPaymentActivity) {
                (getDialog(this@WeeklyPaymentActivity,
                    DialogTag.DIALOG_WEEKLY_PAYMENT) as WeeklyPaymentDialog?)?.dismiss()
                mAdapter.clearAdapter()
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