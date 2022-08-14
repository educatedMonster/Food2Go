package com.example.kafiesta.screens.add_product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.kafiesta.R
import com.example.kafiesta.databinding.ListItemProductBinding
import com.example.kafiesta.domain.ProductDomaintest
import com.example.kafiesta.utilities.helpers.RecyclerClick

class AddProductAdapter(
    private val onClickCallBack: RecyclerClick,
) : RecyclerView.Adapter<AddProductViewHolder>() {


    private var list: ArrayList<ProductDomaintest> = arrayListOf()

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddProductViewHolder {
        val withDataBinding: ListItemProductBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            AddProductViewHolder.LAYOUT,
            parent,
            false
        )
        return AddProductViewHolder(withDataBinding)
    }

    override fun onBindViewHolder(holder: AddProductViewHolder, position: Int) {
        holder.viewBinding.also {
            val model = list[position]
            it.model = model
            it.onClickCallBack = onClickCallBack
        }
    }

    override fun getItemCount(): Int = list.size

}

class AddProductViewHolder(val viewBinding: ListItemProductBinding) :
    RecyclerView.ViewHolder(viewBinding.root) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.list_item_product
    }

}
