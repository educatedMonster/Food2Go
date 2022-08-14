package com.example.kafiesta.screens.add_product

import android.os.Bundle
import android.view.MenuItem
import android.view.View
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
import com.example.kafiesta.screens.BaseActivity
import com.example.kafiesta.utilities.adapter.ProductAdapter
import com.example.kafiesta.utilities.decorator.DividerItemDecoration
import com.example.kafiesta.utilities.dialog.ConfigureDialog
import com.example.kafiesta.utilities.dialog.GlobalDialog
import com.example.kafiesta.utilities.dialog.ProductAddDialog
import com.example.kafiesta.utilities.dialog.ProductEditDialog
import com.example.kafiesta.utilities.extensions.showToast
import com.example.kafiesta.utilities.getDialog
import com.example.kafiesta.utilities.getGlobalDialog
import com.example.kafiesta.utilities.helpers.GlobalDialogClicker
import com.example.kafiesta.utilities.helpers.RecyclerClick
import com.example.kafiesta.utilities.helpers.SharedPrefs
import com.example.kafiesta.utilities.helpers.getSecurePrefs
import com.example.kafiesta.utilities.hideKeyboard
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

    private fun initAdapter() {
        mAdapter = ProductAdapter(RecyclerClick(
            click = {
                ProductEditDialog(
                    userId = userId,
                    product = it as ProductDomaintest,
                    listener = object : ProductEditDialog.Listener {
                        override fun onEditProductListener(
                            product: ProductDomaintest,
                            file: File?,
                        ) {
                            productViewModel.editProduct(product, file)
                        }

                        override fun onDeleteListener(productId: Long) {
                            val tag = DialogTag.DIALOG_DELETE_PRODUCT
                            val configureDialog = ConfigureDialog(
                                activity = this@ProductActivity,
                                title = getString(R.string.dialog_product_form_delete_button),
                                message = "Are uou sure you want to delete this product!",
                                positiveButtonName = getString(R.string.dialog_yes_button),
                                positiveButtonListener =
                                GlobalDialogClicker {
                                    getGlobalDialog(this@ProductActivity, tag)?.dismiss()
                                    productViewModel.deleteProduct(productId)
                                },
                                negativeButtonName = getString(R.string.dialog_cancel_button),
                                neutralButtonListener =
                                GlobalDialogClicker {
                                    getGlobalDialog(this@ProductActivity, tag)?.dismiss()
                                },
                            )
                            GlobalDialog(configureDialog).show(supportFragmentManager, tag)
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
                binding.fabProductMenu.collapse()
                ProductAddDialog(
                    userId = userId,
                    listener = object : ProductAddDialog.Listener {
                        override fun onAddProductListener(product: ProductDomain, file: File) {
                            productViewModel.addProduct(product, file)
                        }
                    }).show(supportFragmentManager, DialogTag.DIALOG_FORM_INITIAL_PRODUCT)
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

    private fun initBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product)
        binding.lifecycleOwner = this
        binding.model = productViewModel
    }

    private fun initLiveData() {
        productViewModel.apply {
            productList.observe(this@ProductActivity) {
                mLength += it.data.size
                mStart += it.data.size
                mCurrentPage = it.currentPage
                mLastPage = it.lastPage

                if (it.isNotEmptyData) {
                    for (data in it.data) {
                        mAdapter.addData(data)
                    }
                } else {
                    binding.layoutEmptyTask.root.visibility = View.VISIBLE
                }
            }

            isLoading.observe(this@ProductActivity) {
                setLoading(it)
            }

            isUpdated.observe(this@ProductActivity) {
                if (it) {
                    showToast("Updated")
                    (getDialog(this@ProductActivity,
                        DialogTag.DIALOG_FORM_EDIT_PRODUCT) as ProductEditDialog?)?.dismiss()
                    initRequest()
                }

            }

            isDeleted.observe(this@ProductActivity) {
                if (it) {
                    showToast("Deleted")
                    hideKeyboard(this@ProductActivity)
                    (getDialog(this@ProductActivity,
                        DialogTag.DIALOG_FORM_EDIT_PRODUCT) as ProductEditDialog?)?.dismiss()
                    initRequest()

                }
            }

            isProductCreated.observe(this@ProductActivity) {
                hideKeyboard(this@ProductActivity)
                (getDialog(
                    this@ProductActivity,
                    DialogTag.DIALOG_FORM_INITIAL_PRODUCT
                ) as ProductAddDialog?)?.dismiss()
                initRequest()
            }

            isUploaded.observe(this@ProductActivity) {
                if (it) {
                    showToast("Product has been added to the list")
                    hideKeyboard(this@ProductActivity)
                    (getDialog(
                        this@ProductActivity,
                        DialogTag.DIALOG_FORM_INITIAL_PRODUCT
                    ) as ProductAddDialog?)?.dismiss()
                    initRequest()
                }
            }
        }
    }

    private fun initRequest() {
        mAdapter.clearAdapter()
        mCurrentPage = 1L
        mLength = 10L
        mStart = 0L

        productViewModel.getAllProducts(
            length = mLength,
            start = mStart,
            search = mSearch)
    }

    private fun initRequestOffset() {
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
