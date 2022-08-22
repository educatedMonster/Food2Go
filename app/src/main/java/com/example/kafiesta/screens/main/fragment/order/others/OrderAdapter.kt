package com.example.kafiesta.screens.main.fragment.order.others

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.kafiesta.R
import com.example.kafiesta.databinding.ListItemOrderBinding
import com.example.kafiesta.domain.OrderBaseDomain
import com.example.kafiesta.utilities.helpers.OrderRecyclerClick
import com.example.kafiesta.utilities.helpers.RecyclerClick

class OrderAdapter(
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

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.viewDataBinding.also {
            val model = list[position]
            it.model = model
            it.onClickCallBack = onClickCallBack
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