package com.example.kafiesta.screens.image_viewer.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.kafiesta.R
import com.example.kafiesta.databinding.LayoutItemPager2PageBinding

class Pager2RecyclerAdapter(
    val context: Context,
) :
    RecyclerView.Adapter<Pager2ViewHolder>() {

    private var list: ArrayList<String> = arrayListOf()

    fun addData(model: String) {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Pager2ViewHolder {
        val withDataBinding: LayoutItemPager2PageBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            Pager2ViewHolder.LAYOUT,
            parent,
            false
        )
        return Pager2ViewHolder(withDataBinding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: Pager2ViewHolder, position: Int) {
        holder.viewDataBinding.also {
            val model = list[position]
            it.model = model
        }
    }

    override fun getItemCount(): Int = list.size

}

class Pager2ViewHolder(val viewDataBinding: LayoutItemPager2PageBinding) :
    RecyclerView.ViewHolder(viewDataBinding.root) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_item_pager2_page
    }
}