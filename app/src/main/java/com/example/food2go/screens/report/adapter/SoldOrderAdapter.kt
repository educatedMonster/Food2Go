package com.example.food2go.screens.report.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.food2go.R
import com.example.food2go.databinding.ListItemOrderDetailsBinding
import com.example.food2go.databinding.ListItemOrderSoldBinding
import com.example.food2go.domain.ItemSold
import com.example.food2go.domain.OrderListDomain

class SoldOrderAdapter(
    private val context: Context
) :
    RecyclerView.Adapter<SoldOrderViewHolder>() {

    private var list: ArrayList<ItemSold> = arrayListOf()

    fun addData(model: ItemSold) {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoldOrderViewHolder {
        val withDataBinding: ListItemOrderSoldBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            SoldOrderViewHolder.LAYOUT,
            parent,
            false
        )
        return SoldOrderViewHolder(withDataBinding)
    }

    override fun onBindViewHolder(holder: SoldOrderViewHolder, position: Int) {
        holder.viewDataBinding.also {
            val model = list[position]
            it.model = model
        }
    }

    override fun getItemCount(): Int = list.size
}

class SoldOrderViewHolder(val viewDataBinding: ListItemOrderSoldBinding) :
    RecyclerView.ViewHolder(viewDataBinding.root) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.list_item_order_sold
    }
}