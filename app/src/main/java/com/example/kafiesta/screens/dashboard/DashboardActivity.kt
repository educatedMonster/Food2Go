package com.example.kafiesta.screens.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.kafiesta.R
import com.example.kafiesta.databinding.ActivityDashboardBinding
import com.example.kafiesta.screens.BaseActivity
import com.example.kafiesta.screens.dashboard.others.DashboardPagerAdapter
import com.example.kafiesta.screens.dashboard.others.DashboardViewModel
import com.example.kafiesta.screens.dashboard.others.Fragment1
import com.example.kafiesta.screens.dashboard.others.Fragment2
import com.example.kafiesta.utilities.extensions.showToast
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DashboardActivity : BaseActivity() {

    private lateinit var binding: ActivityDashboardBinding
    // TODO: Use the ViewModel
    private val dashboardViewModel: DashboardViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(application)
            .create(DashboardViewModel::class.java)
    }

    private var mActionBar: ActionBar? = null
    override val hideStatusBar: Boolean get() = false
    override val showBackButton: Boolean get() = false

    private val mFragmentList: ArrayList<Fragment> =
        arrayListOf(
            Fragment1(),
            Fragment2()
        )
    private lateinit var mDashboardPagerAdapter: DashboardPagerAdapter
    private lateinit var mViewPager2: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initConfig()
    }

    override fun onResume() {
        super.onResume()
        initRequest()
    }

    /**
     * Call all the functions here that needed to be initialized first
     */

    private fun initConfig() {
        initBinding()
        initActionBar()
        initViewPager()
        initTabLayout()
        initLiveData()
    }

    private fun initRequest() {

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initBinding(){
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard)
        binding.lifecycleOwner = this
        binding.dashboardViewModel = dashboardViewModel
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

    private fun initViewPager() {
        //Initialized viewpager adapter
        mDashboardPagerAdapter = DashboardPagerAdapter(mFragmentList, this, this)

        //Add adapter to view pager
        mViewPager2 = binding.viewPager
        mViewPager2.adapter = mDashboardPagerAdapter
    }

    private fun initTabLayout() {
        //Add viewpager to tab layout
        val tabLayout: TabLayout = binding.tabs

        TabLayoutMediator(tabLayout, mViewPager2) { tab, position ->
            tab.text = mDashboardPagerAdapter.pageTitle(position)
        }.attach()

        for (i in 0 until mFragmentList.count()) {
            val tabLinearLayout = LayoutInflater.from(this).inflate(
                R.layout.layout_tab_item,
                null
            ) as ConstraintLayout

            val tabContent = tabLinearLayout.findViewById<View>(R.id.text_view_tab_item_name) as TextView
            tabContent.text = mDashboardPagerAdapter.pageTitle(i)
            tabLayout.getTabAt(i)?.customView = tabContent
        }

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
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
    }

    private fun initLiveData() {
        //
    }
}