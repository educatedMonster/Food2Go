package com.example.kafiesta.screens.main.fragment.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.kafiesta.R
import com.example.kafiesta.databinding.OrderFragmentBinding
import com.example.kafiesta.screens.main.fragment.order.others.fragments.FragmentCompleted
import com.example.kafiesta.screens.main.fragment.order.others.fragments.FragmentDelivery
import com.example.kafiesta.screens.main.fragment.order.others.fragments.FragmentPending
import com.example.kafiesta.screens.main.fragment.order.others.fragments.FragmentPrepare
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import timber.log.Timber


class OrderFragment : Fragment() {

    private lateinit var binding: OrderFragmentBinding

    // TODO: Use the ViewModel
    private val orderViewModel: OrderViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
            .create(OrderViewModel::class.java)
    }

    private val mFragmentList: ArrayList<Fragment> =
        arrayListOf(
            FragmentPending(),
            FragmentPrepare(),
            FragmentDelivery(),
            FragmentCompleted()
        )
    private lateinit var mOrderFragmentManager: OrderFragmentManager
    private lateinit var mViewPager2: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return initDataBinding(inflater, container)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initConfig()
    }

    private fun initDataBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.order_fragment,
            container,
            false
        )
        binding.lifecycleOwner = this
        binding.orderViewModel = orderViewModel

        return binding.root
    }

    /**
     * Call all the functions here that needed to be initialized first
     */

    private fun initConfig() {
        initViewPager()
        initTabLayout()
    }

    private fun initViewPager() {
        //Initialized viewpager adapter
        mOrderFragmentManager =
            OrderFragmentManager(mFragmentList, requireContext(), requireActivity())
        //Add adapter to view pager
        mViewPager2 = binding.viewPager
        mViewPager2.adapter = mOrderFragmentManager
    }

    private fun initTabLayout() {
        //Add viewpager to tab layout
        val tabLayout: TabLayout = binding.tabs
        lateinit var tabContent: TextView
        lateinit var tabImage: ImageView
        lateinit var tabLinearLayout: ConstraintLayout

        TabLayoutMediator(tabLayout, mViewPager2) { tab, position ->
            tab.text = mOrderFragmentManager.pageTitle(position)
        }.attach()

        for (i in 0 until mFragmentList.count()) {
            tabLinearLayout = LayoutInflater.from(requireContext())
                .inflate(R.layout.layout_tab_item, null) as ConstraintLayout
            tabContent =
                tabLinearLayout.findViewById<View>(R.id.text_view_tab_item_name) as TextView
            tabImage = tabLinearLayout.findViewById<View>(R.id.text_view_tab_item_img) as ImageView

//            tabContent.text = mOrderFragmentManager.pageTitle(i)
            tabImage.setImageResource(mOrderFragmentManager.pageImage(i))
            tabLayout.getTabAt(i)?.customView = tabLinearLayout
        }

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                Timber.d("onTabSelected ${tab?.text}")
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                Timber.d("onTabUnselected ${tab?.text}")
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                Timber.d("onTabReselected ${tab?.text}")
            }
        })
    }

    fun initRequest() {
        (mFragmentList[0] as FragmentPending).initRequest()
    }
}

enum class OrderStatusEnum {
    PENDING,
    PREPARING,
    DELIVERY,
    COMPLETED
}
