package com.example.kafiesta.screens.product_and_inventory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.kafiesta.R
import com.example.kafiesta.constants.DialogTag
import com.example.kafiesta.constants.UserConst
import com.example.kafiesta.databinding.ActivityProductAndInventoryBinding
import com.example.kafiesta.databinding.LayoutCustomToolbarInventoryBinding
import com.example.kafiesta.screens.BaseActivity
import com.example.kafiesta.screens.product_and_inventory.product.FragmentProduct
import com.example.kafiesta.screens.product_and_inventory.inventory.FragmentInventory
import com.example.kafiesta.screens.product_and_inventory.inventory.InventoryViewModel
import com.example.kafiesta.screens.product_and_inventory.inventory.dialogs.DialogProductSearch
import com.example.kafiesta.utilities.helpers.SharedPrefs
import com.example.kafiesta.utilities.helpers.getSecurePrefs
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.trackerteer.taskmanagement.utilities.extensions.gone
import com.trackerteer.taskmanagement.utilities.extensions.setSafeOnClickListener
import com.trackerteer.taskmanagement.utilities.extensions.visible
import timber.log.Timber

class ProductAndInventoryActivity : BaseActivity() {

    private lateinit var binding: ActivityProductAndInventoryBinding
    private lateinit var toolbarAddBinding: LayoutCustomToolbarInventoryBinding

    // TODO: Use the ViewModel
    private val inventoryViewModel: InventoryViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(application)
            .create(InventoryViewModel::class.java)
    }

    private var userId = 0L
    private var mActionBar: ActionBar? = null
    override val hideStatusBar: Boolean get() = false
    override val showBackButton: Boolean get() = false

    private val mFragmentList: ArrayList<Fragment> =
        arrayListOf(
            FragmentProduct(),
            FragmentInventory()
        )
    private lateinit var mProductInventoryManager: ProductInventoryManager
    private lateinit var mViewPager2: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userId = SharedPrefs(getSecurePrefs(this)).getString(UserConst.USER_ID)!!.toLong()
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
        initEventListener()
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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_and_inventory)
        binding.lifecycleOwner = this
        binding.model = inventoryViewModel
    }

    private fun initActionBar() {
        setSupportActionBar(binding.toolbar)
        mActionBar = supportActionBar
        if (mActionBar != null) {
            mActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
            mActionBar!!.setDisplayHomeAsUpEnabled(true)
            mActionBar!!.setDisplayShowHomeEnabled(true)
            mActionBar!!.setDisplayUseLogoEnabled(true)
            toolbarAddBinding = DataBindingUtil.inflate(
                LayoutInflater.from(this),
                R.layout.layout_custom_toolbar_inventory,
                null,
                false
            )
            toolbarAddBinding.textViewTitle.text = getString(R.string.title_nav_drawer_inventory)
            toolbarAddBinding.lifecycleOwner = this
            toolbarAddBinding.inventoryViewModel = inventoryViewModel
            mActionBar!!.customView = toolbarAddBinding.root
        } else {
            throw IllegalArgumentException(getString(R.string.error_message_illegal_argument_exception))
        }
    }

    private fun initViewPager() {
        //Initialized viewpager adapter
        mProductInventoryManager = ProductInventoryManager(mFragmentList, this, this)

        //Add adapter to view pager
        mViewPager2 = binding.viewPager
        mViewPager2.adapter = mProductInventoryManager
    }

    private fun initTabLayout() {
        toolbarAddBinding.imgMore.gone()

        //Add viewpager to tab layout
        val tabLayout: TabLayout = binding.tabs

        TabLayoutMediator(tabLayout, mViewPager2) { tab, position ->
            tab.text = mProductInventoryManager.pageTitle(position)
        }.attach()

        for (i in 0 until mFragmentList.count()) {
            val tabLinearLayout = LayoutInflater.from(this).inflate(
                R.layout.layout_tab_item,
                null
            ) as ConstraintLayout

            val tabContent = tabLinearLayout.findViewById<View>(R.id.text_view_tab_item_name) as TextView
            tabContent.text = mProductInventoryManager.pageTitle(i)
            tabLayout.getTabAt(i)?.customView = tabContent
        }

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                Timber.d("onTabSelected ${tab?.text}")
                if(tab?.text!!.matches("Products".toRegex())){
                    toolbarAddBinding.imgMore.gone()
                } else if(tab.text!!.matches("Inventory".toRegex())){
                    toolbarAddBinding.imgMore.visible()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                Timber.d("onTabUnselected ${tab?.text}")
                if(tab?.text!!.matches("Inventory".toRegex())){
                    toolbarAddBinding.imgMore.gone()
                } else if(tab.text!!.matches("Products".toRegex())){
                    toolbarAddBinding.imgMore.visible()
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                Timber.d("onTabReselected ${tab?.text}")
                if(tab?.text!!.matches("Inventory".toRegex())){
                    toolbarAddBinding.imgMore.visible()
                } else if(tab.text!!.matches("Products".toRegex())){
                    toolbarAddBinding.imgMore.gone()
                }
            }
        })
    }

    private fun initEventListener() {
        binding.apply {
            toolbarAddBinding.imgMore.setSafeOnClickListener {
                val dialog = DialogProductSearch(
                    userId = userId,
                    application = application)
                dialog.setParentActivity(this@ProductAndInventoryActivity, this@ProductAndInventoryActivity)
                dialog.show(supportFragmentManager,
                    DialogTag.DIALOG_BOTTOM_SEARCH_INVENTORY)
            }
        }
    }

    private fun initLiveData() {
        //
    }
}