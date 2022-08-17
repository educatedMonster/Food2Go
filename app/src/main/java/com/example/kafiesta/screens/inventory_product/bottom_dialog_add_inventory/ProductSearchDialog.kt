package com.example.kafiesta.screens.inventory_product.bottom_dialog_add_inventory

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Application
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.example.kafiesta.R
import com.example.kafiesta.constants.DialogTag
import com.example.kafiesta.databinding.DialogBottomProductSearchBinding
import com.example.kafiesta.domain.InventoryDomain
import com.example.kafiesta.screens.add_product.bottom_dialog.DialogModifyQuantity
import com.example.kafiesta.screens.inventory_product.InventoryViewModel
import com.example.kafiesta.screens.inventory_product.adapter.ProductSearchAdapter
import com.example.kafiesta.utilities.decorator.DividerItemDecoration
import com.example.kafiesta.utilities.helpers.RecyclerClick
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.trackerteer.taskmanagement.utilities.extensions.visible
import kotlinx.android.synthetic.main.dialog_layout_add_product.view.*

class ProductSearchDialog(
    private val userId: Long,
    private val listener: Listener,
    private val application: Application,
) : BottomSheetDialogFragment() {

    interface Listener {
        fun onAddInventoryWithQuantityListener()
    }

    private lateinit var binding: DialogBottomProductSearchBinding
    private lateinit var mActivity: Activity
    private lateinit var mFragment: FragmentActivity
    private lateinit var mAdapter: ProductSearchAdapter

    private var mLength = 10L
    private var mStart = 0L
    private var mSearch = ""
    private var mCurrentPage = 0L
    private var mLastPage = 0L

    private val inventoryViewModel: InventoryViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(application)
            .create(InventoryViewModel::class.java)
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
        alertDialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.window?.attributes!!.windowAnimations = R.style.BottomDialogAnimation
        alertDialog.window?.setGravity(Gravity.BOTTOM)
        return alertDialog
    }

    fun setParentActivity(activity: Activity, fragment: FragmentActivity) {
        mActivity = activity
        mFragment = fragment
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

            isLoading.observe(binding.lifecycleOwner!!) {
//                setLoading(it)
            }
        }
    }

    private fun initConfig() {
        initAdapter()
        initEventListener()
        initRequest()
        initLiveData()
    }

    private fun initAdapter() {
        mAdapter = ProductSearchAdapter(
            context = mActivity,
            onClickCallBack = RecyclerClick(
                click = {
                    // add to inventory and add quantity
                    // TODO - create a separate dialog coz this returns ProductInventoryDomain
                    DialogModifyQuantity(
                        userId = userId,
                        model = it as InventoryDomain,
                        listener = object : DialogModifyQuantity.Listener {
                            override fun onAddQuantityListener(quantity: String, productId: Long) {
                                inventoryViewModel.inventoryAndQuantity(productId, quantity)
                            }
                        }
                    ).show(mFragment.supportFragmentManager, DialogTag.DIALOG_FORM_EDIT_PRODUCT)
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

    private fun initEventListener() {
        binding.apply {
            btnSearch.setOnClickListener {
                val search = binding.svSearch.text.toString()
                mAdapter.clearAdapter()
                inventoryViewModel.getAllProductInventory(
                    length = mLength,
                    start = mStart,
                    search = search)
            }
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
}