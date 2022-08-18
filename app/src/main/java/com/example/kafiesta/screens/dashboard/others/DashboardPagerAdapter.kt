package com.example.kafiesta.screens.dashboard.others

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class DashboardPagerAdapter(
    private val mFragmentList: ArrayList<Fragment>,
    private val context: Context,
    private val activity: FragmentActivity
) :
    FragmentStateAdapter(activity) {

    override fun createFragment(position: Int): Fragment {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return mFragmentList[position]
    }

    // Show total pages.
    override fun getItemCount(): Int = mFragmentList.size

    fun pageTitle(position: Int): String {
        return when (mFragmentList[position]) {
            is PlaceholderFragment -> {
                "PlaceholderFragment"
            }
            is Placeholder2Fragment -> {
                "Placeholder2Fragment"
            }
            else -> {
                "Unknown"
            }
        }
    }
}




