package com.example.food2go.screens.product_and_inventory.inventory.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.food2go.R
import com.example.food2go.databinding.ListItemTagBinding
import com.example.food2go.utilities.extensions.showToast

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
            holder.viewDataBinding.layoutTag.setOnClickListener {
                context.showToast(tagString)
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
