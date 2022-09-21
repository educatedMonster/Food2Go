package com.example.kafiesta.screens.image_viewer

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.kafiesta.R
import com.example.kafiesta.constants.DialogTag
import com.example.kafiesta.constants.IntentConst
import com.example.kafiesta.constants.OrderConst
import com.example.kafiesta.constants.UserConst
import com.example.kafiesta.databinding.ActivityImageViewerBinding
import com.example.kafiesta.domain.OrderBaseDomain
import com.example.kafiesta.screens.BaseActivity
import com.example.kafiesta.screens.image_viewer.adapter.Pager2RecyclerAdapter
import com.example.kafiesta.screens.image_viewer.dialog.RejectOrderDialog
import com.example.kafiesta.screens.main.fragment.order.OrderViewModel
import com.example.kafiesta.utilities.extensions.showToast
import com.example.kafiesta.utilities.getDialog
import com.example.kafiesta.utilities.helpers.SharedPrefs
import com.example.kafiesta.utilities.helpers.getSecurePrefs
import com.trackerteer.taskmanagement.utilities.extensions.gone
import com.trackerteer.taskmanagement.utilities.extensions.visible
import kotlin.math.abs

class ImageViewerActivity : BaseActivity() {
    private var userId = 0L
    private var orderId = 0L
    private var customerId = 0L
    private var mActionBar: ActionBar? = null
    override val hideStatusBar: Boolean get() = false
    override val showBackButton: Boolean get() = false

    private lateinit var binding: ActivityImageViewerBinding
    private val orderViewModel: OrderViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory(application)
            .create(OrderViewModel::class.java)
    }

    private lateinit var pagerAdapter: Pager2RecyclerAdapter
    private var model: OrderBaseDomain? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userId =
            SharedPrefs(getSecurePrefs(this)).getString(UserConst.USER_ID)!!.toLong()
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
        initExtras()
        initBinding()
        initActionBar()
        initAdapter()
        initEventListener()
        initLiveData()
    }

    private fun initBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_image_viewer)
        binding.lifecycleOwner = this
        binding.model = model
        binding.orderViewModel = orderViewModel
    }

    private fun initActionBar() {
        setSupportActionBar(binding.toolbar)
        mActionBar = supportActionBar
        if (mActionBar != null) {
            mActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
            mActionBar!!.setDisplayHomeAsUpEnabled(true)
            mActionBar!!.setDisplayShowHomeEnabled(true)
            mActionBar!!.setDisplayUseLogoEnabled(true)
            mActionBar!!.title = "Proof Image"
        } else {
            throw IllegalArgumentException(getString(R.string.error_message_illegal_argument_exception))
        }
    }

    private fun initExtras() {
        val orderId = intent.getLongExtra(IntentConst.ORDER_ID, -1L)
        customerId = intent.getLongExtra(IntentConst.CUSTOMER_ID, -1L)
        orderViewModel.getSpecificOrderId(orderId)
    }

    private fun initAdapter() {
        pagerAdapter = Pager2RecyclerAdapter(this)
        binding.pager2.adapter = pagerAdapter
        binding.pager2.setPageTransformer { page, position ->
            val minScale = 0.65f
            val minAlpha = 0.3f

            when {
                position < -1 -> {  // [-Infinity,-1)
                    // This page is way off-screen to the left.
                    page.alpha = 0f
                }
                position <= 1 -> { // [-1,1]
                    page.scaleX =
                        minScale.coerceAtLeast(1 - abs(position))
                    page.scaleY =
                        minScale.coerceAtLeast(1 - abs(position))
                    page.alpha =
                        minAlpha.coerceAtLeast(1 - abs(position))
                }
                else -> {  // (1,+Infinity]
                    // This page is way off-screen to the right.
                    page.alpha = 0f
                }
            }
        }
        binding.pager2.orientation = ViewPager2.ORIENTATION_HORIZONTAL
    }

    private fun initEventListener() {
        binding.apply {
            btnReject.setOnClickListener {
                showWarningRejectDialog()
            }

            btnAccept.setOnClickListener {
                orderViewModel!!.orderMoveStatus(
                    orderId,
                    customerId,
                    OrderConst.ORDER_PREPARING,
                    ""
                )
            }
        }
    }

    private fun initLiveData() {
        orderViewModel.apply {
            specificOrder.observe(this@ImageViewerActivity) {
                model = it as OrderBaseDomain
                orderId = model!!.order.id

                if(model!!.order.isPending){
                    binding.layoutButtonUpdate.visible()
                }

                if (model!!.order.proofURL != null) {
                    pagerAdapter.addData(model!!.order.proofURL!!)
                }
            }

            orderStatus.observe(this@ImageViewerActivity) {
                (getDialog(this@ImageViewerActivity, DialogTag.DIALOG_REJECT_REMARK) as RejectOrderDialog?)?.dismiss()
                onBackPressed()
                showToast(it)
            }
        }
    }

    fun initRequest() {
        pagerAdapter.clearAdapter()
        if (orderId != 0L) {
            orderViewModel.getSpecificOrderId(orderId)
        }
    }

    private fun showWarningRejectDialog() {
        RejectOrderDialog(
            listener = object : RejectOrderDialog.Listener {
                override fun onRejectOrder(remark: String) {
                    orderViewModel.orderMoveStatus(
                        orderId,
                        customerId,
                        OrderConst.ORDER_REJECTED,
                        remark
                    )
                }
            }
        ).show(supportFragmentManager, DialogTag.DIALOG_REJECT_REMARK)
    }
}