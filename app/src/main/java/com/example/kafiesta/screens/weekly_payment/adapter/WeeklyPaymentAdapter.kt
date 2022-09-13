package com.example.kafiesta.screens.weekly_payment.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.kafiesta.R
import com.example.kafiesta.constants.OrderConst
import com.example.kafiesta.databinding.ListItemOrderBinding
import com.example.kafiesta.databinding.ListItemWeeklyPaymentBinding
import com.example.kafiesta.domain.OrderBaseDomain
import com.example.kafiesta.domain.WeeklyPaymentDomain
import com.example.kafiesta.screens.weekly_payment.WeeklyPaymentViewModel
import com.example.kafiesta.utilities.helpers.RecyclerClick
import com.example.kafiesta.utilities.helpers.formatDateString
import com.example.kafiesta.utilities.helpers.getTimeStampDifference
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class WeeklyPaymentAdapter(
    val context: Context,
    private val onClickCallBack: RecyclerClick,
    private val viewModel: WeeklyPaymentViewModel,
) :
    RecyclerView.Adapter<OrderViewHolder>() {

    private var list: ArrayList<WeeklyPaymentDomain> = arrayListOf()

    fun addData(model: WeeklyPaymentDomain) {
        //to avoid duplication
        if (model !in list) {
            list.add(model)
        }
        notifyDataSetChanged()
    }

    fun addLastItem(item: WeeklyPaymentDomain) {
        list.add(0, item)
        notifyItemInserted(itemCount - 1)
    }

    fun addFirstItem(item: WeeklyPaymentDomain) {
        list.add(0, item)
        notifyItemInserted(0)
    }

    fun clearAdapter() {
        list = arrayListOf()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val withDataBinding: ListItemWeeklyPaymentBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            OrderViewHolder.LAYOUT,
            parent,
            false
        )
        return OrderViewHolder(withDataBinding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.viewDataBinding.also {
            val model = list[position]
            it.model = model
            it.onClickCallBack = onClickCallBack
            it.viewModel = viewModel
        }
    }

    override fun getItemCount(): Int = list.size
}

class OrderViewHolder(val viewDataBinding: ListItemWeeklyPaymentBinding) :
    RecyclerView.ViewHolder(viewDataBinding.root) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.list_item_weekly_payment
    }
}