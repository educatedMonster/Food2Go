package com.example.kafiesta.screens.main.fragment.order.others.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.kafiesta.R
import com.example.kafiesta.constants.OrderConst.ORDER_COMPLETED
import com.example.kafiesta.databinding.ListItemOrderBinding
import com.example.kafiesta.domain.OrderListBaseDomain
import com.example.kafiesta.utilities.helpers.RecyclerClick
import com.example.kafiesta.utilities.helpers.formatDateString
import com.example.kafiesta.utilities.helpers.getTimeStampDifference
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class OrderAdapter(
    val context: Context,
    private val onClickCallBack: RecyclerClick,
) :
    RecyclerView.Adapter<OrderViewHolder>() {

    private var list: ArrayList<OrderListBaseDomain> = arrayListOf()

    fun addData(model: OrderListBaseDomain) {
        //to avoid duplication
        if (model !in list) {
            list.add(model)
        }
        notifyDataSetChanged()
    }

    fun addItem(item: OrderListBaseDomain) {
        list.add(0, item)
        notifyDataSetChanged()
    }

    fun clearAdapter() {
        list = arrayListOf()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val withDataBinding: ListItemOrderBinding = DataBindingUtil.inflate(
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

            val color: Int = when (model.order.status) {
                ORDER_COMPLETED -> {
                    R.color.colorSecondary
                }
                else -> {
                    R.color.light_gray2
                }
            }
            it.layoutOrder.setBackgroundResource(color)
            //timestamp

            val time = formatDateString(model.order.createdAt!!)
            val formatter: DateTimeFormatter =
                DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH)
            val localDate: LocalDateTime = LocalDateTime.parse(time, formatter)
            val timeInMilliseconds: Long = localDate.atOffset(ZoneOffset.UTC).toInstant().toEpochMilli()



//            val a = getTimeStampDifference(context, timeInMilliseconds)

            it.tvTimestamp.text = getTimeStampDifference(context, timeInMilliseconds)
        }
    }

    override fun getItemCount(): Int = list.size
}

class OrderViewHolder(val viewDataBinding: ListItemOrderBinding) :
    RecyclerView.ViewHolder(viewDataBinding.root) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.list_item_order
    }
}