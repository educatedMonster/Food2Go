package com.example.food2go.screens.main.fragment.order.others.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.food2go.R
import com.example.food2go.constants.OrderConst
import com.example.food2go.databinding.ListItemOrderBinding
import com.example.food2go.domain.OrderBaseDomain
import com.example.food2go.utilities.helpers.RecyclerClick
import com.example.food2go.utilities.helpers.formatDateString
import com.example.food2go.utilities.helpers.getTimeStampDifference
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class OrderAdapter(
    val context: Context,
    private val onClickCallBack: RecyclerClick,
) :
    RecyclerView.Adapter<OrderViewHolder>() {

    private var list: ArrayList<OrderBaseDomain> = arrayListOf()

    fun addData(model: OrderBaseDomain) {
        //to avoid duplication
        if (model !in list) {
            list.add(model)
        }
        notifyDataSetChanged()
    }

    fun addLastItem(item: OrderBaseDomain) {
        list.add(itemCount, item)
        notifyItemInserted(itemCount)
    }

    fun addFirstItem(item: OrderBaseDomain) {
        list.add(0, item)
        notifyItemInserted(0)
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
                OrderConst.ORDER_COMPLETED -> {
                    R.color.color_palette_completed
                }
                else -> {
                    R.color.color_palette_pending
                }
            }
            it.layoutOrder.setBackgroundResource(color)
            //timestamp

            val createdAt = model.order.createdAt
            val changedAtPreparing = model.order.changedAtPreparing
            val changedAtDelivered = model.order.changedAtDelivered
            val changedAtCompleted = model.order.changedAtCompleted

            var time: String? = null
            when {
                model.order.status.matches(OrderConst.ORDER_PENDING.toRegex()) -> {
                    time = formatDateString(createdAt!!)
                }
                model.order.status.matches(OrderConst.ORDER_PREPARING.toRegex()) -> {
                    val a = changedAtPreparing!!.split(" ")
                    time = formatDateString(a[0] + "T" + a[1] + ".000000Z")
                }
                model.order.status.matches(OrderConst.ORDER_DELIVERY.toRegex()) -> {
                    val a = changedAtDelivered!!.split(" ")
                    time = formatDateString(a[0] + "T" + a[1] + ".000000Z")
                }
                model.order.status.matches(OrderConst.ORDER_COMPLETED.toRegex()) -> {
                    val a = changedAtCompleted!!.split(" ")
                    time = formatDateString(a[0] + "T" + a[1] + ".000000Z")
                }
            }

            if (model.order.status.matches(OrderConst.ORDER_COMPLETED.toRegex())) {
                it.tvTimestamp.text = time
            } else {
                val formatter: DateTimeFormatter =
                    DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH)
                val localDate: LocalDateTime = LocalDateTime.parse(time, formatter)

                val timeInMilliseconds: Long =
                    if (model.order.status.matches(OrderConst.ORDER_PENDING.toRegex())) {
                        localDate.atZone(ZoneId.of("UTC")).toInstant().toEpochMilli()
                    } else {
                        localDate.atZone(ZoneId.of("Asia/Manila")).toInstant().toEpochMilli()
                    }

                it.tvTimestamp.text = getTimeStampDifference(context, timeInMilliseconds)
            }
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