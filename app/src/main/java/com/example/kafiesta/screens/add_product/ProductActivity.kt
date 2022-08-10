package com.example.kafiesta.screens.add_product

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.kafiesta.R
import com.example.kafiesta.constants.DialogTag
import com.example.kafiesta.constants.UserConst
import com.example.kafiesta.databinding.ActivityProductBinding
import com.example.kafiesta.domain.ProductDomain
import com.example.kafiesta.screens.BaseActivity
import com.example.kafiesta.utilities.adapter.ProductAdapter
import com.example.kafiesta.utilities.decorator.DividerItemDecoration
import com.example.kafiesta.utilities.dialog.AddProductDialog
import com.example.kafiesta.utilities.extensions.showToast
import com.example.kafiesta.utilities.helpers.RecyclerClick
import com.example.kafiesta.utilities.helpers.SharedPrefs
import com.example.kafiesta.utilities.helpers.getSecurePrefs
import timber.log.Timber
import java.io.File

class ProductActivity : BaseActivity() {

    override val hideStatusBar: Boolean get() = false
    override val showBackButton: Boolean get() = true
    private var userId = 0L
    private var mLength = 10L
    private var mStart = 0L
    private var mSearch = ""
    private var mCurrentPage = 0L
    private var mLastPage = 0L
    private lateinit var binding: ActivityProductBinding
    private var mActionBar: ActionBar? = null

    private lateinit var mAdapter: ProductAdapter
    private val productViewModel: ProductViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(application)
            .create(ProductViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userId = SharedPrefs(getSecurePrefs(this)).getString(UserConst.USER_ID)?.toLong() ?: 0L
        initConfig()
    }

    override fun onResume() {
        super.onResume()
        initReloadRequest()
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
        initAdapter()
        initEventListener()
        initLiveData()
        initReloadRequest()
    }

    private fun initReloadRequest() {
        mCurrentPage = 1L
        mLength = 10L
        mStart = 0L
        mAdapter.clearAdapter()
        productViewModel.getAllProducts(
            length = mLength,
            start = mStart,
            search = mSearch)
    }

    private fun initAdapter() {
        mAdapter = ProductAdapter(
            onClickCallBack = RecyclerClick {

            })

        binding.recyclerViewProducts.apply {
            adapter = mAdapter
            addItemDecoration(
                DividerItemDecoration(
                    this@ProductActivity,
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
            mActionBar!!.title = getString(R.string.title_nav_drawer_products)
        } else {
            throw IllegalArgumentException(getString(R.string.error_message_illegal_argument_exception))
        }
    }

    private fun initEventListener() {
        binding.apply {
            // Todo - Add product
            this.fabProductAdd.setOnClickListener {
                AddProductDialog(userId, object : AddProductDialog.Listener {
                    override fun onAddProductListener(
                        product: ProductDomain,
                        selectedFile: File,
                    ) {
                        productViewModel.addProduct(this@ProductActivity, product, selectedFile)
                    }
                }).show(supportFragmentManager, DialogTag.DIALOG_ADD_PRODUCT)
            }

            this.recyclerViewProducts.apply {
                this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        if (!recyclerView.canScrollVertically(1)) {
                            fetch()
                        }
                        super.onScrolled(recyclerView, dx, dy)
                    }
                })
            }

            this.swipeRefreshLayout.setOnRefreshListener {
                initReloadRequest()
            }
        }
    }

    private fun initBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product)
        binding.lifecycleOwner = this
        binding.model = productViewModel
    }

    private fun initLiveData() {
        productViewModel.apply {
            this.productList.observe(this@ProductActivity) {
                mLength += it.data.size.toLong()
                mStart += it.data.size.toLong()

                mCurrentPage = it.currentPage
                mLastPage = it.lastPage

                for (model in it.data) {
                    mAdapter.addData(model)
                }
            }

            this.isLoading.observe(this@ProductActivity) {
                setLoading(it)
            }

            this.isProductCreated.observe(this@ProductActivity) {
                showToast("$it.name has been created \n ${it.imageURL}")
            }

            this.isUploaded.observe(this@ProductActivity) {
                if (it) {
                    showToast("Uploaded")
                }
            }
        }
    }

    private fun fetch() {
        if (mCurrentPage < mLastPage) {
            mCurrentPage++
            productViewModel.getAllProducts(
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
