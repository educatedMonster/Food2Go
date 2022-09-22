package com.example.food2go.screens.main.fragment.order.others.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.food2go.R
import com.example.food2go.databinding.ListItemOrderDetailsBinding
import com.example.food2go.domain.OrderListDomain

class OrderDetailsAdapter(
    private val context: Context
) :
    RecyclerView.Adapter<OrderDetailsViewHolder>() {

    private var list: ArrayList<OrderListDomain> = arrayListOf()

    fun addData(model: OrderListDomain) {
        //to avoid duplication
        if (model !in list) {
            list.add(model)
        }
        notifyDataSetChanged()
    }

    fun clearAdapter() {
        list = arrayListOf()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderDetailsViewHolder {
        val withDataBinding: ListItemOrderDetailsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            OrderDetailsViewHolder.LAYOUT,
            parent,
            false
        )
        return OrderDetailsViewHolder(withDataBinding)
    }

    override fun onBindViewHolder(holder: OrderDetailsViewHolder, position: Int) {
        holder.viewDataBinding.also {
            val model = list[position]
            it.model = model
        }
    }

    override fun getItemCount(): Int = list.size
}

class OrderDetailsViewHolder(val viewDataBinding: ListItemOrderDetailsBinding) :
    RecyclerView.ViewHolder(viewDataBinding.root) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.list_item_order_details
    }
}