package com.example.kafiesta.screens.add_product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.kafiesta.R
import com.example.kafiesta.constants.ProductConst
import com.example.kafiesta.databinding.ActivityProductBinding
import com.example.kafiesta.databinding.DialogLayoutAddProductBinding
import com.example.kafiesta.domain.ProductDomaintest
import com.example.kafiesta.domain.Producttest
import com.example.kafiesta.domain.TestUnit
import com.example.kafiesta.utilities.helpers.ProductRecyclerClick

class AddProductAdapter(
    private val onClickCallBack: ProductRecyclerClick,
) : RecyclerView.Adapter<AddProductViewHolder>() {


    private var testUnit: TestUnit? =null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddProductViewHolder {
        val withDataBinding: DialogLayoutAddProductBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            AddProductViewHolder.LAYOUT,
            parent,
            false
        )
        return AddProductViewHolder(withDataBinding)
    }

    override fun onBindViewHolder(holder: AddProductViewHolder, position: Int) {
        holder.viewBinding.also{

        }
    }

    override fun getItemCount(): Int = 1

}

class AddProductViewHolder(val viewBinding: DialogLayoutAddProductBinding) :
    RecyclerView.ViewHolder(viewBinding.root) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.dialog_layout_add_product
    }

}
