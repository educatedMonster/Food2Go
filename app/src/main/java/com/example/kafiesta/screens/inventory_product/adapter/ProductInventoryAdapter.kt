package com.example.kafiesta.screens.inventory_product.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.kafiesta.R
import com.example.kafiesta.constants.ServerConst
import com.example.kafiesta.databinding.ListItemProductInventoryBinding
import com.example.kafiesta.domain.ProductInventoryDomain
import com.example.kafiesta.utilities.helpers.RecyclerClick
import timber.log.Timber
import java.util.concurrent.Executors

class ProductInventoryAdapter(
    private val context: Context,
    private val onClickCallBack: RecyclerClick,
) :
    RecyclerView.Adapter<ProductViewHolder>() {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val withDataBinding: ListItemProductInventoryBinding = DataBindingUtil.inflate(
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
                val rand: Int
                val tags = model.tags.split(",")
                val arrTags = tags.toTypedArray()

                rand = if (arrTags.size <= 9) {
                    (1..arrTags.size).random()
                } else {
                    (1..10).random()
                }

                val colors = intArrayOf(
                    R.drawable.tag_rectangle_round_1,
                    R.drawable.tag_rectangle_round_2,
                    R.drawable.tag_rectangle_round_3,
                    R.drawable.tag_rectangle_round_4,
                    R.drawable.tag_rectangle_round_5,
                    R.drawable.tag_rectangle_round_6,
                    R.drawable.tag_rectangle_round_7,
                    R.drawable.tag_rectangle_round_8,
                    R.drawable.tag_rectangle_round_9)

                val tagAdapter = TagAdapter(context, colors[rand], arrTags)
                adapter = tagAdapter
                setHasFixedSize(false)
            }
        }
    }

    override fun getItemCount(): Int = list.size
}

class ProductViewHolder(val viewDataBinding: ListItemProductInventoryBinding) :
    RecyclerView.ViewHolder(viewDataBinding.root) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.list_item_product_inventory
    }
}