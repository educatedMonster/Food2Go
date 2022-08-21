package com.example.kafiesta.screens.dashboard.others

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * A [FragmentStateAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class DashboardPagerAdapter(
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
            is Fragment2 -> {
                "Fragment2"
            }
            is Fragment1 -> {
                "Fragment1"
            }
            else -> {
                "Unknown"
            }
        }
    }
}




