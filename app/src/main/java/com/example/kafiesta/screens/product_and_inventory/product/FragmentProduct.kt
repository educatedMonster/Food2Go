package com.example.kafiesta.screens.product_and_inventory.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.kafiesta.R
import com.example.kafiesta.constants.DialogTag
import com.example.kafiesta.constants.UserConst
import com.example.kafiesta.databinding.FragmentProductBinding
import com.example.kafiesta.domain.ProductDomain
import com.example.kafiesta.domain.ProductDomaintest
import com.example.kafiesta.screens.product_and_inventory.product.adapter.ProductAdapter
import com.example.kafiesta.utilities.decorator.DividerItemDecoration
import com.example.kafiesta.utilities.dialog.ConfigureDialog
import com.example.kafiesta.utilities.dialog.GlobalDialog
import com.example.kafiesta.utilities.dialog.ProductAddDialog
import com.example.kafiesta.utilities.dialog.ProductEditDialog
import com.example.kafiesta.utilities.getDialog
import com.example.kafiesta.utilities.getGlobalDialog
import com.example.kafiesta.utilities.helpers.GlobalDialogClicker
import com.example.kafiesta.utilities.helpers.RecyclerClick
import com.example.kafiesta.utilities.helpers.SharedPrefs
import com.example.kafiesta.utilities.helpers.getSecurePrefs
import com.example.kafiesta.utilities.hideKeyboard
import com.example.kafiesta.utilities.extensions.showToast
import timber.log.Timber
import java.io.File

/**
 * A placeholder fragment containing a simple view.
 */
class FragmentProduct : Fragment() {

    private lateinit var binding: FragmentProductBinding
    private val productViewModel: ProductViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
            .create(ProductViewModel::class.java)
    }
    private lateinit var mAdapter: ProductAdapter
    private var userId = 0L
    private var mLength = 10L
    private var mStart = 0L
    private var mSearch = ""
    private var mCurrentPage = 0L
    private var mLastPage = 0L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return initBinding(inflater, container)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        userId =
            SharedPrefs(getSecurePrefs(requireContext())).getString(UserConst.USER_ID)!!.toLong()
        initConfig()
    }

    override fun onResume() {
        super.onResume()
        initRequest()
    }

    private fun initBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_product,
            container,
            false
        )
        binding.lifecycleOwner = this
        binding.model = productViewModel
        return binding.root
    }

    private fun initConfig() {
        initAdapter()
        initEventListener()
        initLiveData()
    }

    private fun initAdapter() {
        mAdapter = ProductAdapter(
            context = requireContext(), RecyclerClick(
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
                                    activity = requireActivity(),
                                    title = getString(R.string.dialog_product_form_delete_button),
                                    message = "Are you sure you want to delete this product!",
                                    positiveButtonName = getString(R.string.dialog_yes_button),
                                    positiveButtonListener =
                                    GlobalDialogClicker {
                                        getGlobalDialog(requireActivity(), tag)?.dismiss()
                                        productViewModel.deleteProduct(productId)
                                    },
                                    negativeButtonName = getString(R.string.dialog_cancel_button),
                                    neutralButtonListener =
                                    GlobalDialogClicker {
                                        getGlobalDialog(requireActivity(), tag)?.dismiss()
                                    },
                                )
                                GlobalDialog(configureDialog, null).show(requireActivity().supportFragmentManager,
                                    tag)
                            }
                        }).show(requireActivity().supportFragmentManager,
                        DialogTag.DIALOG_FORM_EDIT_PRODUCT)
                }
            ))
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
                    }).show(requireActivity().supportFragmentManager,
                    DialogTag.DIALOG_FORM_INITIAL_PRODUCT)
            }

            recyclerViewProducts.apply {
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
                        initRequestOffset()
                    }
                    super.onScrolled(recyclerView, dx, dy)
                }
            })

            swipeRefreshLayout.setOnRefreshListener {
                initRequest()
            }
        }
    }

    private fun initLiveData() {
        productViewModel.apply {
            productList.observe(viewLifecycleOwner) {
                mLength += it.data.size
                mStart += it.data.size
                mCurrentPage = it.currentPage
                mLastPage = it.lastPage

                for (data in it.data) {
                    mAdapter.addData(data)
                }
            }

            isLoading.observe(viewLifecycleOwner) {
                setLoading(it)
            }

            isUpdated.observe(viewLifecycleOwner) {
                if (it) {
                    showToast("Updated")
                    (getDialog(requireActivity(),
                        DialogTag.DIALOG_FORM_EDIT_PRODUCT) as ProductEditDialog?)?.dismiss()
                    initRequest()
                }

            }

            isDeleted.observe(viewLifecycleOwner) {
                if (it) {
                    showToast("Deleted")
                    hideKeyboard(requireActivity())
                    (getDialog(requireActivity(),
                        DialogTag.DIALOG_FORM_EDIT_PRODUCT) as ProductEditDialog?)?.dismiss()
                    initRequest()

                }
            }

            isProductCreated.observe(viewLifecycleOwner) {
                hideKeyboard(requireActivity())
                (getDialog(
                    requireActivity(),
                    DialogTag.DIALOG_FORM_INITIAL_PRODUCT
                ) as ProductAddDialog?)?.dismiss()
                initRequest()
            }

            isUploaded.observe(viewLifecycleOwner) {
                if (it) {
                    showToast("Product has been added to the list")
                    hideKeyboard(requireActivity())
                    (getDialog(
                        requireActivity(),
                        DialogTag.DIALOG_FORM_INITIAL_PRODUCT
                    ) as ProductAddDialog?)?.dismiss()
                    initRequest()
                }
            }

            isAddedInventory.observe(viewLifecycleOwner) {
                if (it) {
                    showToast("Product has been added to inventory list")
                    hideKeyboard(requireActivity())
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

    override fun onDestroyView() {
        super.onDestroyView()
        binding
    }


}
