package com.example.kafiesta.screens.main

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.example.kafiesta.KaFiestaApplication
import com.example.kafiesta.R
import com.example.kafiesta.constants.DialogTag
import com.example.kafiesta.constants.UserConst
import com.example.kafiesta.databinding.ActivityMainBinding
import com.example.kafiesta.databinding.LayoutCustomNavHeaderBinding
import com.example.kafiesta.databinding.LayoutCustomToolbarDashboardBinding
import com.example.kafiesta.screens.BaseActivity
import com.example.kafiesta.screens.add_product.ProductActivity
import com.example.kafiesta.screens.inventory_product.InventoryActivity
import com.example.kafiesta.screens.main.fragment.home.HomeFragment
import com.example.kafiesta.screens.main.fragment.myshop.MyShopFragment
import com.example.kafiesta.screens.main.fragment.order.OrderFragment
import com.example.kafiesta.screens.profile.ProfileSettingActivity
import com.example.kafiesta.utilities.dialog.ConfigureDialog
import com.example.kafiesta.utilities.dialog.GlobalDialog
import com.example.kafiesta.utilities.extensions.showToast
import com.example.kafiesta.utilities.helpers.GlobalDialogClicker
import com.example.kafiesta.utilities.helpers.SharedPrefs
import com.example.kafiesta.utilities.helpers.getSecurePrefs
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.trackerteer.taskmanagement.utilities.extensions.gone
import com.trackerteer.taskmanagement.utilities.extensions.visible

class MainActivity : BaseActivity(),
    BottomNavigationView.OnNavigationItemSelectedListener,
    NavigationView.OnNavigationItemSelectedListener {

    override val hideStatusBar: Boolean get() = false
    override val showBackButton: Boolean get() = false

    private var userId = 0L
    private var infoId = 0L
    private var shopId = 0L
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    private val mFragmentList: ArrayList<Fragment> = arrayListOf(
        MyShopFragment(),
        HomeFragment(),
        OrderFragment(),
    )
    private val mFragmentManager: FragmentManager = this.supportFragmentManager
    private val mainContainer = R.id.nav_host_fragment
    private var mCurrentInView = Fragment()
    private var mActionBar: ActionBar? = null
    private lateinit var mToolbarBinding: LayoutCustomToolbarDashboardBinding
    private lateinit var mToggle: ActionBarDrawerToggle
    private lateinit var mLayoutCustomNavHeaderBinding: LayoutCustomNavHeaderBinding
    private var mGlobalDialog: GlobalDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userId = SharedPrefs(getSecurePrefs(this)).getString(UserConst.USER_ID)?.toLong() ?: 0L
        infoId = SharedPrefs(getSecurePrefs(this)).getString(UserConst.INFO_ID)?.toLong() ?: 0L
        shopId = SharedPrefs(getSecurePrefs(this)).getString(UserConst.SHOP_ID)?.toLong() ?: 0L
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
        initBottomNavigationView()
        initDrawerNavigationView()
        initLiveData()
    }

    private fun initRequest() {
        requestMainViewModel()
    }

    private fun requestMainViewModel() {
        if (this::mainViewModel.isInitialized) {
            mainViewModel.getMe()
        }
    }

    private fun initBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this

        mainViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
            .create(MainViewModel::class.java)
    }

    private fun initActionBar() {
        setSupportActionBar(binding.toolbar)
        mActionBar = supportActionBar
        if (mActionBar != null) {
            mActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
            mActionBar!!.setHomeAsUpIndicator(R.drawable.ic_menu)
            mActionBar!!.setDisplayHomeAsUpEnabled(true)
            mActionBar!!.setHomeButtonEnabled(true)
            mToolbarBinding = DataBindingUtil.inflate(
                LayoutInflater.from(this),
                R.layout.layout_custom_toolbar_dashboard,
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
        for (i in 0 until mFragmentList.size) {
            mFragmentManager.beginTransaction()
                .add(mainContainer, mFragmentList[i], mFragmentList[i]::class.java.simpleName)
                .hide(mFragmentList[i])
                .commit()
        }

        mFragmentManager.beginTransaction().show(mFragmentList[1]).commit()
        setFragmentView(mFragmentList[1])
        binding.bottomNavigationView.selectedItemId = R.id.nav_home
        binding.bottomNavigationView.setOnNavigationItemSelectedListener(this)
    }

    private fun initDrawerNavigationView() {
        mLayoutCustomNavHeaderBinding = DataBindingUtil.inflate(
            LayoutInflater.from(this),
            R.layout.layout_custom_nav_header,
            binding.drawerNavigationView,
            false
        )
        mLayoutCustomNavHeaderBinding.lifecycleOwner = this
        mLayoutCustomNavHeaderBinding.mainViewModel = mainViewModel

        mToggle = ActionBarDrawerToggle(
            this,
            binding.drawableLayout,
            binding.toolbar,
            R.string.nav_bar_open,
            R.string.nav_bar_close
        )
        mToggle.syncState()
        binding.drawableLayout.addDrawerListener(mToggle)
        binding.drawerNavigationView.addHeaderView(mLayoutCustomNavHeaderBinding.root)
        binding.drawerNavigationView.setNavigationItemSelectedListener(this)

    }

    private fun initLiveData() {
        mainViewModel.mainFormState.observe(this) {
            val tag = DialogTag.DIALOG_MAIN_LOGOUT_FORM_STATE
            if ((it.onLogoutRequest) && (it.isLoggingOut)) {
                val configureDialog = ConfigureDialog(
                    activity = this,
                    title = getString(R.string.main_activity_preparing_logout)
                )
                mGlobalDialog = GlobalDialog(configureDialog)
                mGlobalDialog?.show(supportFragmentManager, tag)
            } else if ((it.onLogoutRequest) && (!it.isLoggingOut)) {
                Handler(Looper.getMainLooper()).postDelayed({
                    mGlobalDialog?.dismiss()
                    proceedToLogout()
                }, 1000)
            }
        }

        mainViewModel.profile.observe(this) {

        }

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

                if (mCurrentInView != mFragmentList[1]) {
//                    (mFragmentList[1] as HomeFragment).initRequest()
                    refreshHomeFragment()
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
                setFragmentView(mFragmentList[2])
            }


            /**
             * Drawer Navigation View
             */
            R.id.nav_dashboard -> {
                showToast(getString(R.string.title_nav_drawer_dashboard))
//                proceedToActivity(SettingActivity::class.java)
//                setFocus(false)
            }
            R.id.nav_my_shop -> {
                proceedToActivity(ProfileSettingActivity::class.java)
                setFocus(false)
            }
            R.id.nav_product -> {
                proceedToActivity(ProductActivity::class.java)
                setFocus(false)
            }
            R.id.nav_inventory -> {
                proceedToActivity(InventoryActivity::class.java)
                setFocus(false)
            }
            R.id.nav_logout -> {
                val tag = DialogTag.DIALOG_MAIN_LOGOUT
                val configureDialog = ConfigureDialog(
                    activity = this,
                    title = getString(R.string.main_activity_title_logout),
                    message = getString(R.string.main_activity_are_you_sure_you_want_to_logout),
                    positiveButtonName = getString(R.string.main_activity_button_yes),
                    negativeButtonName = getString(R.string.main_activity_button_no),
                    positiveButtonListener = GlobalDialogClicker {
                        mGlobalDialog?.dismiss()
                        mainViewModel.logout()
                    },
                    negativeButtonListener = GlobalDialogClicker {
                        mGlobalDialog?.dismiss()
                    }
                )
                mGlobalDialog = GlobalDialog(configureDialog)
                mGlobalDialog?.show(supportFragmentManager, tag)
            }
        }
        binding.drawableLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun setFragmentView(fragment: Fragment) {
        mCurrentInView = fragment
        val title = when (fragment) {
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
//        requestMainViewModel()
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

    override fun onBackPressed() {
        if (binding.drawableLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawableLayout.closeDrawer(GravityCompat.START)
        } else {
            super.moveTaskToBack(true)
        }
    }

}