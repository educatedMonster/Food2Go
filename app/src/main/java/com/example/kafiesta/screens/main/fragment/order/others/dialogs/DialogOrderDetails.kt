package com.example.kafiesta.screens.main.fragment.order.others.dialogs

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.kafiesta.R
import com.example.kafiesta.constants.OrderConst
import com.example.kafiesta.databinding.DialogLayoutOrderDetailsBinding
import com.example.kafiesta.domain.OrderListBaseDomain
import com.example.kafiesta.screens.main.fragment.order.OrderViewModel
import com.example.kafiesta.screens.main.fragment.order.others.adapter.OrderDetailsAdapter
import com.example.kafiesta.utilities.decorator.DividerItemDecoration
import com.example.kafiesta.utilities.helpers.OrderRecyclerClick

class DialogOrderDetails(
    private val status: String,
    private val model: OrderListBaseDomain?,
    private val onClickCallBack: OrderRecyclerClick,
    private val activity: Activity
) : DialogFragment() {

    private lateinit var binding: DialogLayoutOrderDetailsBinding
    private val orderViewModel: OrderViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
            .create(OrderViewModel::class.java)
    }

    private lateinit var mAdapter: OrderDetailsAdapter

    @SuppressLint("DefaultLocale")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = AlertDialog.Builder(context)
        val inflater = LayoutInflater.from(context)

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.dialog_layout_order_details,
            null,
            false
        ) as DialogLayoutOrderDetailsBinding
        binding.lifecycleOwner = this
        binding.model = model
        binding.orderViewModel = orderViewModel
        binding.onClickCallBack = onClickCallBack
        val view = binding.root
        dialog.setView(view)


        val color: Int = when (status) {
            OrderConst.ORDER_COMPLETED -> {
                R.color.colorSecondary
            }
            else -> {
                R.color.light_gray2
            }
        }

        binding.layoutOrder.setBackgroundResource(color)

        initConfig()

        val alertDialog = dialog.create()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.setCancelable(true)
        alertDialog.setCanceledOnTouchOutside(true)
        return alertDialog
    }

    private fun initConfig() {
        initEventListener()
        initAdapter()
//        initRequest()
        initData() // for the mean time
    }

    private fun initEventListener() {
        binding.apply {
            rcOrders.apply {
                this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        if (!recyclerView.canScrollVertically(1)) {
//                            initSearchRequestOffset(mNewText)
                        }
                        super.onScrolled(recyclerView, dx, dy)
                    }
                })
            }
        }
    }

    private fun initAdapter() {
        mAdapter = OrderDetailsAdapter(context = activity)

        binding.rcOrders.apply {
            adapter = mAdapter
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    R.drawable.list_divider_decoration
                )
            )
        }
    }

    private fun initData() {
        for (data in model!!.orderList) {
            mAdapter.addData(data)
        }
    }
}