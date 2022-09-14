package com.example.kafiesta.utilities.tab

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.example.kafiesta.R
import com.example.kafiesta.screens.test.interfaces_test_order.ViewPagerRecyclerAdapter
import com.google.android.material.tabs.TabLayout

class CustomTabLayout : TabLayout {
    private var tabTitles = ArrayList<Int?>()
    private var tabIcons = ArrayList<Int?>()
    private var mType = 0
    private lateinit var mViewPager: ViewPager

    constructor(context: Context?) : super(context!!) {

    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs) {
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!, attrs, defStyleAttr) {
    }

    fun setupWithViewPager(
        viewPager: ViewPager?,
        tabTitles: List<Int>?,
        tabIcons: List<Int>?,
        type: Int = TYPE_WHITE_BG,
    ) {
        super.setupWithViewPager(viewPager)
        if (viewPager != null) {
            mViewPager = viewPager
        }
        mType = type
        if (tabTitles != null) {
            this.tabTitles = ArrayList(tabTitles)
        }
        if (tabIcons != null) {
            this.tabIcons = ArrayList(tabIcons)
        }
        initTab()
    }

    private fun initTab() {
        if (mViewPager.adapter!!.count > TAB_COUNT_FOR_FIXED) {
            tabMode = MODE_SCROLLABLE
        }

        if (mType == TYPE_WHITE_BG) {
            setSelectedTabIndicatorColor(ContextCompat.getColor(context, R.color.colorPrimary))
            setTabTextColors(R.color.colorPrimary, R.color.colorAccent)
        } else if (mType == TYPE_YELLOW_BG) {
            setSelectedTabIndicatorColor(ContextCompat.getColor(context, R.color.colorAccent))
            setTabTextColors(R.color.colorPrimary, R.color.colorAccent)
        }
        for (i in 0 until tabCount) {
            val tabLayout = getTabAt(i)
            if (tabLayout != null) {
                tabLayout.customView = getTabView(i)
                if (i == 0) {
                    onItemSelected(tabLayout, true)
                } else {
                    onItemSelected(tabLayout, false)
                }
            }
        }
        addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: Tab) {
                onItemSelected(tab, true)
            }

            override fun onTabUnselected(tab: Tab) {
                onItemSelected(tab, false)
            }

            override fun onTabReselected(tab: Tab) {}
        })
    }

    private fun getTabView(position: Int): View? {
        val pagerAdapter = mViewPager.adapter
        return if (pagerAdapter is ViewPagerRecyclerAdapter) {
            pagerAdapter.getTabView(position, mType, tabTitles, tabIcons)
        } else null
    }

    private fun onItemSelected(tab: Tab, isSelected: Boolean) {
        val scaleUp = AnimationUtils.loadAnimation(context, R.anim.tab_scale_up)
        val scaleDown = AnimationUtils.loadAnimation(context, R.anim.tab_scale_down)
        if (tab.customView != null) {
            val constraintLayout: ConstraintLayout = tab.customView!!.findViewById(R.id.layoutRoot)
            val textView = tab.customView!!.findViewById<TextView>(R.id.txt_tab)
            val imageView = tab.customView!!.findViewById<ImageView>(R.id.img_tab)
            if (isSelected) {
                constraintLayout.startAnimation(scaleUp)
            } else {
                constraintLayout.startAnimation(scaleDown)
            }
            if (isSelected) {
                if (mType == TYPE_WHITE_BG) {
                    textView.setTextColor(ContextCompat.getColor(context, R.color.colorAccent))
                    imageView.setColorFilter(ContextCompat.getColor(context, R.color.colorAccent))
                } else if (mType == TYPE_YELLOW_BG) {
                    textView.setTextColor(ContextCompat.getColor(context,
                        R.color.color_palette_black))
                    imageView.setColorFilter(ContextCompat.getColor(context,
                        R.color.color_palette_black))
                }
            } else {
                if (mType == TYPE_WHITE_BG) {
                    textView.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
                    imageView.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary))
                } else if (mType == TYPE_YELLOW_BG) {
                    textView.setTextColor(ContextCompat.getColor(context,
                        R.color.color_palette_black))
                    imageView.setColorFilter(ContextCompat.getColor(context,
                        R.color.color_palette_black))
                }
            }
        }
    }

    fun prependToTitle(position: Int, textToAppend: String) {
        if (position > tabTitles.size) {
            return
        }
        for (i in 0 until tabCount) {
            if (position != i) {
                continue
            }
            val tabLayout = getTabAt(i)
            var viewGroup: ViewGroup? = null
            if (tabLayout != null) {
                viewGroup = tabLayout.customView as ViewGroup?
            }
            var tv: TextView? = null
            if (viewGroup != null) {
                tv = viewGroup.findViewById(R.id.txt_tab)
            }
            if (tv != null) {
                var currentTitle = tv.text.toString()
                if (!currentTitle.contains(textToAppend)) {
                    currentTitle += textToAppend
                }
                tv.text = currentTitle
            }
        }
    }

    fun updateTitle(position: Int, title: String?) {
        if (position > tabTitles.size) {
            return
        }
        for (i in 0 until tabCount) {
            if (position != i) {
                continue
            }
            val tabLayout = getTabAt(i)
            var viewGroup: ViewGroup? = null
            if (tabLayout != null) {
                viewGroup = tabLayout.customView as ViewGroup?
            }
            var tv: TextView? = null
            if (viewGroup != null) {
                tv = viewGroup.findViewById(R.id.txt_tab)
            }
            if (tv != null) {
                tv.text = title
            }
        }
    }

    fun setTitleTextStyle(allCaps: Boolean) {
        for (i in 0 until tabCount) {
            val tabLayout = getTabAt(i)
            var viewGroup: ViewGroup? = null
            if (tabLayout != null) {
                viewGroup = tabLayout.customView as ViewGroup?
            }
            var tv: TextView? = null
            if (viewGroup != null) {
                tv = viewGroup.findViewById(R.id.txt_tab)
            }
            if (tv != null) {
                tv.isAllCaps = allCaps
            }
        }
    }

    companion object {
        const val TYPE_WHITE_BG = 0
        const val TYPE_YELLOW_BG = 1
        const val TAB_COUNT_FOR_FIXED = 4
    }
}
