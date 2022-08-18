package com.example.kafiesta.screens.inventory_product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.kafiesta.R
import com.example.kafiesta.constants.DialogTag
import com.example.kafiesta.constants.UserConst
import com.example.kafiesta.databinding.ActivityProductInventoryBinding
import com.example.kafiesta.databinding.LayoutCustomToolbarInventoryBinding
import com.example.kafiesta.domain.InventoryDomain
import com.example.kafiesta.screens.BaseActivity
import com.example.kafiesta.screens.add_product.bottom_dialog.DialogModifyQuantity
import com.example.kafiesta.screens.inventory_product.adapter.InventoryAdapter
import com.example.kafiesta.screens.inventory_product.bottom_dialog_add_inventory.ProductSearchDialog
import com.example.kafiesta.utilities.decorator.DividerItemDecoration
import com.example.kafiesta.utilities.extensions.showToast
import com.example.kafiesta.utilities.getDialog
import com.example.kafiesta.utilities.helpers.RecyclerClick
import com.example.kafiesta.utilities.helpers.SharedPrefs
import com.example.kafiesta.utilities.helpers.getSecurePrefs
import com.example.kafiesta.utilities.hideKeyboard
import timber.log.Timber

class InventoryActivity : BaseActivity() {
    override val hideStatusBar: Boolean get() = false
    override val showBackButton: Boolean get() = true

    private var userId = 0L
    private var mLength = 10L
    private var mStart = 0L
    private var mSearch = ""
    private var mCurrentPage = 0L
    private var mLastPage = 0L
    private lateinit var binding: ActivityProductInventoryBinding
    private lateinit var toolbarAddBinding: LayoutCustomToolbarInventoryBinding
    private lateinit var mAdapter: InventoryAdapter
    private var mActionBar: ActionBar? = null

    private val inventoryViewModel: InventoryViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(application)
            .create(InventoryViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userId = SharedPrefs(getSecurePrefs(this)).getString(UserConst.USER_ID)!!.toLong()
        initConfig()
    }

    private fun initConfig() {
        initBinding()
        initActionBar()
        initAdapter()
        initEventListener()
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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_inventory)
        binding.lifecycleOwner = this
        binding.model = inventoryViewModel
    }

    private fun initAdapter() {
        mAdapter = InventoryAdapter(
            context = this, RecyclerClick(
                click = {
                    DialogModifyQuantity(
                        userId = userId,
                        model = it as InventoryDomain,
                        listener = object : DialogModifyQuantity.Listener {
                            override fun onAddQuantityListener(quantity: String, productId: Long) {
                                inventoryViewModel.modifyQuantity(quantity, productId)
                            }
                        }
                    ).show(supportFragmentManager, DialogTag.DIALOG_BOTTOM_QUANTITY)
                }
            ))

        binding.recyclerViewProducts.apply {
            adapter = mAdapter
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    R.drawable.list_divider_decoration
                )
            )
        }
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
                R.layout.layout_custom_toolbar_inventory,
                null,
                false
            )
            toolbarAddBinding.textViewTitle.text = getString(R.string.title_nav_drawer_inventory)
            toolbarAddBinding.lifecycleOwner = this
            toolbarAddBinding.inventoryViewModel = inventoryViewModel
            mActionBar!!.customView = toolbarAddBinding.root
        } else {
            throw IllegalArgumentException(getString(R.string.error_message_illegal_argument_exception))
        }
    }

    private fun initEventListener() {
        binding.apply {
            toolbarAddBinding.imgMore.setOnClickListener {
                val dialog = ProductSearchDialog(
                    userId = userId,
                    application = application)
                dialog.setParentActivity(this@InventoryActivity, this@InventoryActivity)
                dialog.show(supportFragmentManager,
                    DialogTag.DIALOG_BOTTOM_SEARCH_INVENTORY)
            }

            swipeRefreshLayout.setOnRefreshListener {
                initRequest()
            }

            recyclerViewProducts.apply {
                this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        if (!recyclerView.canScrollVertically(1)) {
                            initRequestOffset()
                        }
                        super.onScrolled(recyclerView, dx, dy)
                    }
                })
            }
        }
    }

    private fun initLiveData() {
        inventoryViewModel.apply {
            inventoryList.observe(this@InventoryActivity) {
                mLength += it.data.size
                mStart += it.data.size
                mCurrentPage = it.currentPage
                mLastPage = it.lastPage

                for (data in it.data) {
                    mAdapter.addData(data)
                }
            }

            isAddedInventory.observe(this@InventoryActivity) {
                setLoading(it)
                (getDialog(this@InventoryActivity,
                    DialogTag.DIALOG_BOTTOM_QUANTITY) as DialogModifyQuantity?)?.dismiss()
                initRequest()

            }

            isModifyQuantity.observe(this@InventoryActivity) {
                showToast("Quantity modified")
                hideKeyboard(this@InventoryActivity)
                (getDialog(this@InventoryActivity,
                    DialogTag.DIALOG_BOTTOM_QUANTITY) as DialogModifyQuantity?)?.dismiss()
                initRequest()
            }

            isLoading.observe(this@InventoryActivity) {
                setLoading(it)
            }
        }
    }

    private fun initRequest() {
        mAdapter.clearAdapter()
        mCurrentPage = 1L
        mLength = 10L
        mStart = 0L

        inventoryViewModel.getAllInventory(
            length = mLength,
            start = mStart,
            search = mSearch)
    }

    private fun initRequestOffset() {
        if (mCurrentPage < mLastPage) {
            mCurrentPage++
            inventoryViewModel.getAllInventory(
                length = mLength,
                start = mStart,
                search = mSearch)
        }
    }

    private fun setLoading(set: Boolean) {
        try {
            binding.apply {
                swipeRefreshLayout.isRefreshing = set
                if (set) {
                    //is Loading
//                    textViewMarkAll.isEnabled = false
//                    shimmerViewContainer.startShimmer()
//                    shimmerViewContainer.visible()
//                    recyclerView.gone()
//                    linearLayoutProgressOffset.gone()
//                } else {
                    //not loading
//                    textViewMarkAll.isEnabled = true
//                    shimmerViewContainer.stopShimmer()
//                    shimmerViewContainer.gone()
//                    recyclerView.visible()
//                    linearLayoutProgressOffset.visible()
                }
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

}