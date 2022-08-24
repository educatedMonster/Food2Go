package com.example.kafiesta.screens.product_and_inventory.inventory.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.kafiesta.R
import com.example.kafiesta.databinding.ListItemInventoryBinding
import com.example.kafiesta.domain.InventoryDomain
import com.example.kafiesta.utilities.helpers.RecyclerClick2View

class InventoryAdapter(
    private val context: Context,
    private val onClickCallBack: Any
) :
    RecyclerView.Adapter<ProductViewHolder>() {
    private var list: ArrayList<InventoryDomain> = arrayListOf()
    private lateinit var model: InventoryDomain


    fun addData(model: InventoryDomain) {
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

    fun add(model: InventoryDomain) {
        if (model !in list) {
            list.add(model)
        }
        notifyDataSetChanged()
    }

    fun remove(model: InventoryDomain) {
        list.remove(model)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val withDataBinding: ListItemInventoryBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            ProductViewHolder.LAYOUT,
            parent,
            false
        )
        return ProductViewHolder(withDataBinding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.viewDataBinding.also {
            model = list[position]
            it.model = model
            it.onClickCallBack = onClickCallBack as RecyclerClick2View
            it.view = it.imageViewButtonMore

            holder.viewDataBinding.rcTag.apply {
//                val rand: Int
                val tags = model.tags.split(",")
                val arrTags = tags.toTypedArray()

//                rand = if (arrTags.size <= 9) {
//                    (1..arrTags.size).random()
//                } else {
//                    (1..9).random()
//                }

//                val colors = intArrayOf(
//                    R.drawable.tag_rectangle_round_1,
//                    R.drawable.tag_rectangle_round_2,
//                    R.drawable.tag_rectangle_round_3,
//                    R.drawable.tag_rectangle_round_4,
//                    R.drawable.tag_rectangle_round_5,
//                    R.drawable.tag_rectangle_round_6,
//                    R.drawable.tag_rectangle_round_7,
//                    R.drawable.tag_rectangle_round_8,
//                    R.drawable.tag_rectangle_round_9)

                val tagAdapter = TagAdapter(context, arrTags)
                adapter = tagAdapter
                setHasFixedSize(false)
            }
        }
    }

    override fun getItemCount(): Int = list.size
}

class ProductViewHolder(val viewDataBinding: ListItemInventoryBinding) :
    RecyclerView.ViewHolder(viewDataBinding.root) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.list_item_inventory
    }
}