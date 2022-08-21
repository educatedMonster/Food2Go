package com.example.kafiesta.screens.main.fragment.order.others

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.kafiesta.R

/**
 * A [FragmentStateAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class OrderPagerAdapter(
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
            is FragmentPending -> {
                "Pending"
            }
            is FragmentPrepare -> {
                "Prepare"
            }
            is FragmentDelivery -> {
                "Delivery"
            }
            is FragmentCompleted -> {
                "Completed"
            }
            else -> {
                "Unknown"
            }
        }
    }

    fun pageImage(position: Int): Int {
        return when (mFragmentList[position]) {
            is FragmentPending -> {
                R.drawable.ic_order_pending
            }
            is FragmentPrepare -> {
                R.drawable.ic_order_prepare
            }
            is FragmentDelivery -> {
                R.drawable.ic_order_delivery
            }
            is FragmentCompleted -> {
                R.drawable.ic_order_completed
            }
            else -> {
                R.drawable.ic_add_24
            }
        }
    }
}




