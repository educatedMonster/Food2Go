package com.example.kafiesta.screens.product_and_inventory.inventory.dialogs

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Application
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.kafiesta.R
import com.example.kafiesta.constants.DialogTag
import com.example.kafiesta.databinding.DialogBottomProductSearchBinding
import com.example.kafiesta.domain.ProductInventoryDomain
import com.example.kafiesta.screens.product_and_inventory.inventory.InventoryViewModel
import com.example.kafiesta.screens.product_and_inventory.inventory.adapter.ProductSearchAdapter
import com.example.kafiesta.utilities.decorator.DividerItemDecoration
import com.example.kafiesta.utilities.helpers.RecyclerClick
import com.example.kafiesta.utilities.hideKeyboard
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.example.kafiesta.utilities.extensions.showToast
import com.trackerteer.taskmanagement.utilities.extensions.visible


class DialogProductSearch(
    private val userId: Long,
    private val application: Application,
) : BottomSheetDialogFragment() {

    private lateinit var binding: DialogBottomProductSearchBinding
    private lateinit var mActivity: Activity
    private lateinit var mFragment: FragmentActivity
    private lateinit var mAdapter: ProductSearchAdapter

    private var mLength = 10L
    private var mStart = 0L
    private var mSearch = ""
    private var mNewText = ""
    private var mCurrentPage = 0L
    private var mLastPage = 0L

    private val inventoryViewModel: InventoryViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(application)
            .create(InventoryViewModel::class.java)
    }

    override fun getTheme(): Int = R.style.NoMarginsDialog

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )
    }

    @SuppressLint("DefaultLocale")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = AlertDialog.Builder(context)
        val inflater = LayoutInflater.from(context)

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.dialog_bottom_product_search,
            null,
            false
        ) as DialogBottomProductSearchBinding
        binding.lifecycleOwner = this
        binding.model = inventoryViewModel

        val view = binding.root
        dialog.setView(view)

        initConfig()

        val alertDialog = dialog.create()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.window?.attributes!!.windowAnimations = R.style.BottomDialogAnimation
        alertDialog.window?.setGravity(Gravity.BOTTOM)
        return alertDialog
    }

    fun setParentActivity(activity: Activity, fragment: FragmentActivity) {
        mActivity = activity
        mFragment = fragment
    }

    private fun initConfig() {
        initEventListener()
        initAdapter()
        initRequest()
        initLiveData()

    }

    private fun initEventListener() {
        binding.apply {
            textInputEditText.requestFocus()
            textInputEditText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {}
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(query: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    mNewText = query.toString()
                }
            })

            btnSearch.setOnClickListener {
                if (mNewText.isEmpty()) {
                    initRequest()
                } else {
                    searchRequest(mNewText)
                }
            }

            recyclerViewProducts.apply {
                this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        if (!recyclerView.canScrollVertically(1)) {
                            initSearchRequestOffset(mNewText)
                        }
                        super.onScrolled(recyclerView, dx, dy)
                    }
                })
            }
        }
    }

    private fun initAdapter() {
        mAdapter = ProductSearchAdapter(
            context = mActivity,
            onClickCallBack = RecyclerClick(
                click = {
                    DialogInventoryQuantity(
                        userId = userId,
                        model = it as ProductInventoryDomain,
                        listener = object : DialogInventoryQuantity.Listener {
                            override fun onAddInventoryQuantityListener(
                                quantity: String,
                                productId: Long,
                            ) {
                                inventoryViewModel.inventoryAndQuantity(productId, quantity)
                            }
                        }
                    ).show(mFragment.supportFragmentManager, DialogTag.DIALOG_BOTTOM_ADD_INVENTORY_PRODUCT)
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

    private fun initRequest() {
        mAdapter.clearAdapter()
        mCurrentPage = 1L
        mLength = 10L
        mStart = 0L

        inventoryViewModel.getAllProductInventory(
            length = mLength,
            start = mStart,
            search = mSearch)
    }

    private fun initLiveData() {
        inventoryViewModel.apply {
            productInventoryList.observe(binding.lifecycleOwner!!) {
                mLength += it.data.size
                mStart += it.data.size
                mCurrentPage = it.currentPage
                mLastPage = it.lastPage

                if (it.data.isNotEmpty()) {
                    for (data in it.data) {
                        mAdapter.addData(data)
                    }
                } else {
                    binding.layoutEmptyTask.root.visible()
                }
            }

            isAddedInventory.observe(binding.lifecycleOwner!!) {
                showToast("Added to Inventory list and Quantity modified")
                hideKeyboard(requireActivity())
                (com.example.kafiesta.utilities.getDialog(requireActivity(),
                    DialogTag.DIALOG_BOTTOM_ADD_INVENTORY_PRODUCT) as DialogInventoryQuantity?)?.dismiss()
                initRequest()
            }

            isLoading.observe(binding.lifecycleOwner!!) {
//                setLoading(it)
            }
        }
    }

    private fun searchRequest(newSearch: String) {
        mAdapter.clearAdapter()
        mCurrentPage = 1L
        inventoryViewModel.getAllProductInventory(
            length = 10L,
            start = 0L,
            search = newSearch)
    }

    private fun initSearchRequestOffset(newText: String) {
        if (mCurrentPage < mLastPage) {
            mCurrentPage++
            inventoryViewModel.getAllProductInventory(
                length = mLength,
                start = mStart,
                search = newText)
        }
    }
}