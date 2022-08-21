package com.example.kafiesta.screens.main.fragment.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.kafiesta.R
import com.example.kafiesta.databinding.OrderFragmentBinding
import com.example.kafiesta.screens.main.fragment.order.others.*
import com.example.kafiesta.utilities.extensions.showToast
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

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
    private lateinit var mOrderPagerAdapter: OrderPagerAdapter
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

    override fun onResume() {
        super.onResume()
        initRequest()
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
        initLiveData()
    }

    private fun initRequest() {

    }

    private fun initViewPager() {
        //Initialized viewpager adapter
        mOrderPagerAdapter = OrderPagerAdapter(mFragmentList, requireContext(), requireActivity())
        //Add adapter to view pager
        mViewPager2 = binding.viewPager
        mViewPager2.adapter = mOrderPagerAdapter
    }

    private fun initTabLayout() {
        //Add viewpager to tab layout
        val tabLayout: TabLayout = binding.tabs
        lateinit var tabContent: TextView
        lateinit var tabImage: ImageView
        lateinit var tabLinearLayout: ConstraintLayout

        TabLayoutMediator(tabLayout, mViewPager2) { tab, position ->
            tab.text = mOrderPagerAdapter.pageTitle(position)
        }.attach()

        for (i in 0 until mFragmentList.count()) {
            tabLinearLayout = LayoutInflater.from(requireContext()).inflate(R.layout.layout_tab_item, null) as ConstraintLayout
            tabContent = tabLinearLayout.findViewById<View>(R.id.text_view_tab_item_name) as TextView
            tabImage = tabLinearLayout.findViewById<View>(R.id.text_view_tab_item_img) as ImageView

//            tabContent.text = mOrderPagerAdapter.pageTitle(i)
            tabImage.setImageResource(mOrderPagerAdapter.pageImage(i))
            tabLayout.getTabAt(i)?.customView = tabLinearLayout
        }

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                requireContext().showToast("onTabSelected ${tab?.text}")
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                requireContext().showToast("onTabUnselected ${tab?.text}")
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                requireContext().showToast("onTabReselected ${tab?.text}")
            }
        })
    }

    private fun initLiveData() {
        //
    }

    companion object {
        fun newInstance() = OrderFragment()
    }

}