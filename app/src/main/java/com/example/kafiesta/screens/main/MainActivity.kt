package com.example.kafiesta.screens.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.kafiesta.KaFiestaApplication
import com.example.kafiesta.R
import com.example.kafiesta.databinding.ActivityMainBinding
import com.example.kafiesta.databinding.LayoutCustomToolbarBinding
import com.example.kafiesta.screens.BaseActivity
import com.example.kafiesta.screens.main.fragment.home.HomeFragment
import com.example.kafiesta.screens.main.fragment.myshop.MyShopFragment
import com.example.kafiesta.screens.main.fragment.order.OrderFragment
import com.example.kafiesta.utilities.extensions.showToast
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.trackerteer.taskmanagement.utilities.extensions.gone
import com.trackerteer.taskmanagement.utilities.extensions.visible

class MainActivity : BaseActivity(),
    BottomNavigationView.OnNavigationItemSelectedListener {

    override val hideStatusBar: Boolean
        get() = false
    override val showBackButton: Boolean
        get() = false

    private lateinit var binding: ActivityMainBinding

    private val mFragmentList: ArrayList<Fragment> = arrayListOf(
        MyShopFragment(),
        HomeFragment(),
        OrderFragment(),
    )
    private val mFragmentManager: FragmentManager = this.supportFragmentManager
    private val mainContainer = R.id.nav_host_fragment
    private var mCurrentInView = Fragment()
    private var mActionBar: ActionBar? = null
    private lateinit var mToolbarBinding: LayoutCustomToolbarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initConfig()
    }

    /**
     * Call all the functions here that needed to be initialized first
     */
    private fun initConfig() {
        initBinding()
        initActionBar()
        initBottomNavigationView()
    }

    private fun initBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this

    }

    private fun initActionBar() {
        mActionBar = supportActionBar
        if (mActionBar != null) {
            mActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
//            mActionBar!!.setDisplayHomeAsUpEnabled(true)
//            mActionBar!!.setHomeButtonEnabled(true)
            mToolbarBinding = DataBindingUtil.inflate(
                LayoutInflater.from(this),
                R.layout.layout_custom_toolbar,
                null,
                false
            )
            mToolbarBinding.lifecycleOwner = this
//            mToolbarBinding.mainViewModel = mainViewModel
            mActionBar!!.customView = mToolbarBinding.root
        } else {
            throw IllegalArgumentException(getString(R.string.error_message_illegal_argument_exception))
        }
    }

    private fun initBottomNavigationView() {
        val bottomNavigationView = binding.bottomNavigationView

        for (i in 0 until mFragmentList.size) {
            mFragmentManager.beginTransaction()
                .add(mainContainer, mFragmentList[i], mFragmentList[i]::class.java.simpleName)
                .hide(mFragmentList[i])
                .commit()
        }

        mFragmentManager.beginTransaction().show(mFragmentList[1]).commit()
        setFragmentView(mFragmentList[1])
        bottomNavigationView.selectedItemId = R.id.nav_home
        bottomNavigationView.setOnNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            /**
             * Bottom Navigation View
             */
            R.id.navi_my_shop -> {
                mFragmentManager.beginTransaction()
                    .hide(mCurrentInView)
                    .show(mFragmentList[0])
                    .commit()

                if (mCurrentInView != mFragmentList[0]) {
                    (mFragmentList[0] as MyShopFragment).initRequest()
                }

                setFragmentView(mFragmentList[0])
            }
            R.id.nav_home -> {
                mFragmentManager.beginTransaction()
                    .hide(mCurrentInView)
                    .show(mFragmentList[1])
                    .commit()

//                refreshHomeFragment()
                if (mCurrentInView != mFragmentList[1]) {
                    (mFragmentList[1] as HomeFragment).initRequest()
                }

                setFragmentView(mFragmentList[1])

            }
            R.id.nav_order -> {
                mFragmentManager.beginTransaction()
                    .hide(mCurrentInView)
                    .show(mFragmentList[2])
                    .commit()

                if (mCurrentInView != mFragmentList[2]) {
                    (mFragmentList[2] as OrderFragment).initRequest()
                }

            }
        }
        return true
    }

    private fun setFragmentView(fragment: Fragment){
        mCurrentInView = fragment
        val title = when (fragment){
            is MyShopFragment -> {
                getString(R.string.navigation_title_my_shop)
            }
            is HomeFragment -> {
                getString(R.string.navigation_title_home)
            }
            is OrderFragment -> {
                getString(R.string.navigation_title_order)
            }
            else -> {
                getString(R.string.navigation_title_error)
            }
        }

        setActionBarTitle(title)
//        onResumeRequestMainViewModel()
        setFocus(mCurrentInView == mFragmentList[1])
    }

    private fun setActionBarTitle(title: String) {
        mToolbarBinding.apply {
            if (title.isEmpty()) {
                textViewTitle.gone()
            } else {
                textViewTitle.visible()
                textViewTitle.text = title
            }
        }
    }

    private fun setFocus(set: Boolean) {
        KaFiestaApplication.taskActivityIsOpen = set
    }

    private fun refreshHomeFragment() {
        (mFragmentList[1] as HomeFragment).initRequest()
    }

}