package com.example.kafiesta.screens.product_and_inventory.inventory.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.kafiesta.R
import com.example.kafiesta.databinding.ListItemTagBinding
import com.example.kafiesta.utilities.extensions.showToast

class TagAdapter(
    private val context: Context,
    private val list: Array<String>,
) : RecyclerView.Adapter<TagViewHolder>() {

    private lateinit var tagString: String

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder {
        val withDataBinding: ListItemTagBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            TagViewHolder.LAYOUT,
            parent,
            false
        )

        return TagViewHolder(withDataBinding)
    }

    override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
        holder.viewDataBinding.also {
            tagString = list[position]
            it.tagString = tagString
            val a: String = it.tagString as String
            holder.viewDataBinding.layoutTag.setBackgroundResource(R.drawable.tag_rectangle_round_2)
            holder.viewDataBinding.layoutTag.setOnClickListener {
                context.showToast(a)
            }
        }
    }

    override fun getItemCount(): Int = this.list.size
}

class TagViewHolder(val viewDataBinding: ListItemTagBinding) :
    RecyclerView.ViewHolder(viewDataBinding.root) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.list_item_tag
    }
}
