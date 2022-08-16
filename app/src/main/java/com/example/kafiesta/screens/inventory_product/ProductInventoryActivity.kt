package com.example.kafiesta.screens.inventory_product

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.kafiesta.R
import com.example.kafiesta.constants.DialogTag
import com.example.kafiesta.constants.UserConst
import com.example.kafiesta.databinding.ActivityProductInventoryBinding
import com.example.kafiesta.domain.ProductInventoryDomain
import com.example.kafiesta.screens.BaseActivity
import com.example.kafiesta.screens.inventory_product.adapter.ProductInventoryAdapter
import com.example.kafiesta.screens.inventory_product.bottom_dialog.BottomSheetFragment
import com.example.kafiesta.utilities.decorator.DividerItemDecoration
import com.example.kafiesta.utilities.helpers.RecyclerClick
import com.example.kafiesta.utilities.helpers.SharedPrefs
import com.example.kafiesta.utilities.helpers.getSecurePrefs
import timber.log.Timber

class ProductInventoryActivity : BaseActivity() {

    private var userId = 0L
    private var mLength = 10L
    private var mStart = 0L
    private var mSearch = ""
    private var mCurrentPage = 0L
    private var mLastPage = 0L

    private lateinit var binding: ActivityProductInventoryBinding
    private lateinit var mAdapter: ProductInventoryAdapter
    private var mActionBar: ActionBar? = null

    private val productInventoryViewModel: ProductInventoryViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(application)
            .create(ProductInventoryViewModel::class.java)
    }

    override val hideStatusBar: Boolean get() = false
    override val showBackButton: Boolean get() = true

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
        binding.model = productInventoryViewModel
    }

    private fun initAdapter() {
        mAdapter = ProductInventoryAdapter(
            context = this, RecyclerClick(
            click = {
                BottomSheetFragment(
                    userId = userId,
                    model = it as ProductInventoryDomain,
                    listener = object : BottomSheetFragment.Listener {
                        override fun onAddQuantityListener(quantity: Long, productId: Long) {
                            productInventoryViewModel.modifyQuantity(quantity, productId)
                        }
                    }).show(supportFragmentManager, DialogTag.DIALOG_FORM_EDIT_PRODUCT)
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
            mActionBar!!.setDisplayHomeAsUpEnabled(true)
            mActionBar!!.setDisplayShowHomeEnabled(true)
            mActionBar!!.title = getString(R.string.title_nav_drawer_inventory)
        } else {
            throw IllegalArgumentException(getString(R.string.error_message_illegal_argument_exception))
        }
    }

    private fun initEventListener() {
        binding.apply {
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
        productInventoryViewModel.apply {
            productInventoryList.observe(this@ProductInventoryActivity) {
                mLength += it.result.data.size
                mStart += it.result.data.size
                mCurrentPage = it.result.currentPage
                mLastPage = it.result.lastPage

                for (data in it.result.data) {
                    mAdapter.addData(data)
                }
            }

            isLoading.observe(this@ProductInventoryActivity) {
                setLoading(it)
            }
        }
    }

    private fun initRequest() {
        mAdapter.clearAdapter()
        mCurrentPage = 1L
        mLength = 10L
        mStart = 0L

        productInventoryViewModel.getAllProductInventory(
            length = mLength,
            start = mStart,
            search = mSearch)
    }

    private fun initRequestOffset() {
        if (mCurrentPage < mLastPage) {
            mCurrentPage++
            productInventoryViewModel.getAllProductInventory(
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