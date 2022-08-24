package com.example.kafiesta.screens.product_and_inventory.product.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.kafiesta.R
import com.example.kafiesta.databinding.ListItemProductBinding
import com.example.kafiesta.domain.ProductDomaintest
import com.example.kafiesta.screens.product_and_inventory.inventory.adapter.TagAdapter
import com.example.kafiesta.utilities.helpers.RecyclerClick

class ProductAdapter(
    private val context: Context,
    private val onClickCallBack: RecyclerClick,
) :
    RecyclerView.Adapter<ProductViewHolder>() {

    private var list: ArrayList<ProductDomaintest> = arrayListOf()
    private lateinit var  model : ProductDomaintest

    fun addData(model: ProductDomaintest) {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val withDataBinding: ListItemProductBinding = DataBindingUtil.inflate(
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
            it.onClickCallBack = onClickCallBack

            holder.viewDataBinding.rcTag.apply {
                val tags = model.tags.split(",")
                val arrTags = tags.toTypedArray()

//                val rand = if (arrTags.size <= 9) {
//                    (1..arrTags.size).random()
//                } else {
//                    (1..10).random()
//                }
//
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

class ProductViewHolder(val viewDataBinding: ListItemProductBinding) :
    RecyclerView.ViewHolder(viewDataBinding.root) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.list_item_product
    }

}