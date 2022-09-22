package com.example.food2go.screens.product_and_inventory

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.food2go.screens.product_and_inventory.inventory.FragmentInventory
import com.example.food2go.screens.product_and_inventory.product.FragmentProduct

/**
 * A [FragmentStateAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class ProductInventoryManager(
    private val mFragmentList: ArrayList<Fragment>,
    someContext: Context,
    activity: FragmentActivity
) :
    FragmentStateAdapter(activity) {

    val context = someContext.applicationContext ?: someContext

    override fun createFragment(position: Int): Fragment {
        return mFragmentList[position]
    }

    override fun getItemCount(): Int = mFragmentList.size

    fun pageTitle(position: Int): String {
        return when (mFragmentList[position]) {
            is FragmentProduct -> {
                "Product"
            }
            is FragmentInventory -> {
                "Inventory"
            }
            else -> {
                "Unknown"
            }
        }
    }
}




