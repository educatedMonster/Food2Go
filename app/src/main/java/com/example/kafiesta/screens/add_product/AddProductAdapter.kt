package com.example.kafiesta.screens.add_product

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.example.kafiesta.R
import com.example.kafiesta.databinding.ActivityProductBinding
import com.example.kafiesta.utilities.helpers.RecyclerClick

class AddProductAdapter(listener: RecyclerClick) : RecyclerView.Adapter<AddProductViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddProductViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: AddProductViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}

class AddProductViewHolder(viewBinding: ActivityProductBinding) :
    RecyclerView.ViewHolder(viewBinding.root) {
        companion object{
            @LayoutRes
            val LAYOUT = R.layout.list_item_product
        }

}
