package com.example.kafiesta.screens.dashboard

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.kafiesta.R
import com.example.kafiesta.databinding.ActivityDashboardBinding
import com.example.kafiesta.screens.dashboard.others.DashboardPagerAdapter
import com.example.kafiesta.screens.BaseActivity
import com.example.kafiesta.screens.dashboard.others.Placeholder2Fragment
import com.example.kafiesta.screens.dashboard.others.PlaceholderFragment
import com.example.kafiesta.utilities.extensions.showToast
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DashboardActivity : BaseActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private var mActionBar: ActionBar? = null
    override val hideStatusBar: Boolean get() = false
    override val showBackButton: Boolean get() = false

    private val mFragmentList: ArrayList<Fragment> =
        arrayListOf(
            PlaceholderFragment(),
            Placeholder2Fragment(),
        )
    private lateinit var mDashboardPagerAdapter: DashboardPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Initialized viewpager adapter
        mDashboardPagerAdapter = DashboardPagerAdapter(mFragmentList, this, this)

        //Add adapter to view pager
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = mDashboardPagerAdapter

        //Add viewpager to tab layout
        val tabs: TabLayout = binding.tabs

        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = mDashboardPagerAdapter.pageTitle(position)
        }.attach()


        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                showToast("onTabSelected ${tab?.text}")
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                showToast("onTabUnselected ${tab?.text}")
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                showToast("onTabReselected ${tab?.text}")
            }
        })


        initConfig()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initConfig() {

        initActionBar()
    }

    private fun initActionBar() {
        setSupportActionBar(binding.toolbar)
        mActionBar = supportActionBar
        if (mActionBar != null) {
            mActionBar!!.setDisplayHomeAsUpEnabled(true)
            mActionBar!!.setDisplayShowHomeEnabled(true)
            mActionBar!!.title = getString(R.string.title_activity_dashboard)
        } else {
            throw IllegalArgumentException(getString(R.string.error_message_illegal_argument_exception))
        }
    }
}