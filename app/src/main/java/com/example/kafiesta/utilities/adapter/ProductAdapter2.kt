package com.example.kafiesta.utilities.adapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.kafiesta.R
import com.example.kafiesta.databinding.ListItemProductBinding
import com.example.kafiesta.domain.ProductDomaintest
import com.example.kafiesta.utilities.helpers.RecyclerClick
import timber.log.Timber
import java.util.concurrent.Executors

class ProductAdapter(
    private val onClickCallBack: RecyclerClick) :
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


    fun getModel() : ProductDomaintest{
        return model
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

            val executor = Executors.newSingleThreadExecutor()
            val handler = Handler(Looper.getMainLooper())
            var image: Bitmap?
            executor.execute {
                try {
                    val imageUrl = model.imageURL
                        ?: "https://pbs.twimg.com/profile_images/1048086829143011329/W8R1grIh_400x400.jpg"
                    val `in` = java.net.URL(imageUrl).openStream()
                    image = BitmapFactory.decodeStream(`in`)
                    handler.post {
                        it.imageViewImage.setImageBitmap(image)
                    }
                } catch (e: Exception) {
                    Timber.d(e)
                }
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