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
import com.example.kafiesta.domain.ProductDomaintest
import com.example.kafiesta.domain.ResultDomaintest
import com.example.kafiesta.screens.BaseActivity
import com.example.kafiesta.utilities.adapter_diffutil.SimpleDiffUtilAdapter
import com.example.kafiesta.utilities.decorator.DividerItemDecoration
import com.example.kafiesta.utilities.dialog.ProductDialog
import com.example.kafiesta.utilities.extensions.showToast
import com.example.kafiesta.utilities.getDialog
import com.example.kafiesta.utilities.helpers.RecyclerClick
import com.example.kafiesta.utilities.helpers.SharedPrefs
import com.example.kafiesta.utilities.helpers.getSecurePrefs
import com.example.kafiesta.utilities.hideKeyboard
import com.trackerteer.taskmanagement.utilities.extensions.gone
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

    private lateinit var mAdapter: SimpleDiffUtilAdapter
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
        productViewModel.getAllProducts(
            length = mLength,
            start = mStart,
            search = mSearch)
    }

    private fun initAdapter() {
        mAdapter = SimpleDiffUtilAdapter(R.layout.list_item_product, RecyclerClick(
            click = {
                ProductDialog(
                    userId = userId,
                    listener = object : ProductDialog.Listener {
                        override fun onAddProductListener(product: ProductDomain, file: File) {
                            productViewModel.addProduct(product, file)
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
            mActionBar!!.title = getString(R.string.title_nav_drawer_products)
        } else {
            throw IllegalArgumentException(getString(R.string.error_message_illegal_argument_exception))
        }
    }

    private fun initEventListener() {
        binding.apply {
            // Todo - Add product
            fabProductAdd.setOnClickListener {
                ProductDialog(
                    userId = userId,
                    listener = object : ProductDialog.Listener {
                        override fun onAddProductListener(product: ProductDomain, file: File) {
                            productViewModel.addProduct(product, file)
                        }
                    }).show(supportFragmentManager, DialogTag.DIALOG_FORM_INITIAL_PRODUCT)
            }

            swipeRefreshLayout.setOnRefreshListener {
                initReloadRequest()
            }

            recyclerViewProducts.apply {
                this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        if (!recyclerView.canScrollVertically(1)) {
                            fetch()
                        }
                        super.onScrolled(recyclerView, dx, dy)
                    }
                })
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
            productList.observe(this@ProductActivity) {
                val listProduct = ArrayList<ProductDomaintest>()


                mLength += it.data.size
                mStart += it.data.size

                mCurrentPage = it.currentPage
                mLastPage = it.lastPage

                for (model in it.data) {
                    listProduct.add(model)
                }

                mAdapter.submitList(listProduct as List<ProductDomaintest>)
            }

            isLoading.observe(this@ProductActivity) {
                setLoading(it)
            }

            isUpdated.observe(this@ProductActivity) {
                initReloadRequest()
                setLoading(it)
            }

            isDeleted.observe(this@ProductActivity) {
                if (it) {
                    initReloadRequest()
                    setLoading(it)
                    showToast("Deleted")
                }
            }

            isProductCreated.observe(this@ProductActivity) {
                setLoading(true)
                showToast("${it.name} has been created.")
            }

            isUploaded.observe(this@ProductActivity) {
                if (it) {
                    setLoading(it)
                    initReloadRequest()
                    showToast("Product has been added to the list")
                    binding.fabProductMenu.gone()
                    hideKeyboard(this@ProductActivity)
                    (getDialog(
                        this@ProductActivity,
                        DialogTag.DIALOG_FORM_INITIAL_PRODUCT
                    ) as ProductDialog?)?.dismiss()
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


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}
