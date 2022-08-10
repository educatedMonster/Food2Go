package com.example.kafiesta.utilities.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.kafiesta.R
import com.example.kafiesta.databinding.ListItemProductBinding
import com.example.kafiesta.domain.ProductDomaintest
import com.example.kafiesta.utilities.helpers.RecyclerClick

class ProductAdapter (private val onClickCallBack: RecyclerClick): RecyclerView.Adapter<NotificationViewHolder>() {

    private var list: ArrayList<ProductDomaintest> = arrayListOf()

    fun addData(model: ProductDomaintest){
        //to avoid duplication
        if(model !in list){
            list.add(model)
        }
        notifyDataSetChanged()
    }

    fun clearAdapter(){
        list = arrayListOf()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val withDataBinding: ListItemProductBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            NotificationViewHolder.LAYOUT,
            parent,
            false
        )
        return NotificationViewHolder(withDataBinding)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.viewDataBinding.also {
            val model = list[position]
            it.model = model
            it.onClickCallBack = onClickCallBack
        }
    }

    override fun getItemCount(): Int = list.size
}
class NotificationViewHolder(val viewDataBinding: ListItemProductBinding) :
    RecyclerView.ViewHolder(viewDataBinding.root) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.list_item_product
    }

}