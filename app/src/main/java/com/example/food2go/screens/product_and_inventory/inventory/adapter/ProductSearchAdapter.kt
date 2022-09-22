package com.example.food2go.screens.product_and_inventory.inventory.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.food2go.R
import com.example.food2go.databinding.ListItemSearchInventoryBinding
import com.example.food2go.domain.ProductInventoryDomain
import com.example.food2go.utilities.helpers.RecyclerClick

class ProductSearchAdapter(
    private val context: Context,
    private val onClickCallBack: RecyclerClick,
) :
    RecyclerView.Adapter<ProductInventoryViewHolder>() {
    private var list: ArrayList<ProductInventoryDomain> = arrayListOf()
    private lateinit var model: ProductInventoryDomain

    fun addData(model: ProductInventoryDomain) {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductInventoryViewHolder {
        val withDataBinding: ListItemSearchInventoryBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            ProductInventoryViewHolder.LAYOUT,
            parent,
            false
        )
        return ProductInventoryViewHolder(withDataBinding)
    }

    override fun onBindViewHolder(holder: ProductInventoryViewHolder, position: Int) {
        holder.viewDataBinding.also {
            model = list[position]
            it.model = model
            it.onClickCallBack = onClickCallBack

            holder.viewDataBinding.tvProduct.setOnClickListener {

            }
        }
    }

    override fun getItemCount(): Int = list.size
}

class ProductInventoryViewHolder(val viewDataBinding: ListItemSearchInventoryBinding) :
    RecyclerView.ViewHolder(viewDataBinding.root) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.list_item_search_inventory
    }
}