package com.example.kafiesta.screens.test.interfaces_test_order

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.*
import androidx.viewpager.widget.PagerAdapter
import com.example.kafiesta.R
import com.example.kafiesta.databinding.TabPageRecyclerBinding
import com.example.kafiesta.domain.OrderBaseDomain
import com.example.kafiesta.utilities.helpers.RecyclerClick
import com.example.kafiesta.utilities.tab.CustomTabLayout.Companion.TAB_COUNT_FOR_FIXED
import com.example.kafiesta.utilities.tab.CustomTabLayout.Companion.TYPE_WHITE_BG
import com.example.kafiesta.utilities.tab.CustomTabLayout.Companion.TYPE_YELLOW_BG
import com.trackerteer.taskmanagement.utilities.extensions.gone
import com.trackerteer.taskmanagement.utilities.extensions.visible
import timber.log.Timber
import kotlin.math.roundToInt

class ViewPagerRecyclerAdapter(
    private val context: Context,
    private val pageList: List<Page>,
    private val onClickCallBack: RecyclerClick,
) : PagerAdapter() {

    private lateinit var binding: TabPageRecyclerBinding

    private var mRefreshOrderListener: RefreshOrderListener? = null

    private val adapterList: ArrayList<TestOrderAdapter> = arrayListOf()
    private var emptyLayoutList: ArrayList<ConstraintLayout> = arrayListOf()

    fun setRefreshOrderListener(refreshOrderListener: RefreshOrderListener) {
        mRefreshOrderListener = refreshOrderListener
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return pageList[position].getTitle()
    }

    override fun getCount(): Int {
        return pageList.size
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View)
    }

    fun updateDataList(
        pagePosition: Int,
        newList: ArrayList<OrderBaseDomain>,
        status: String,
    ) {
        adapterList[pagePosition].updateList(newList, status)
        showHideEmptyLayout(pagePosition, newList)
        binding.progressBottom.gone()
    }

    fun getDataItem(pagePosition: Int, recyclerPosition: Int): OrderBaseDomain{
        return adapterList[pagePosition].getDataItem(recyclerPosition)
    }

    fun addData(pagePosition: Int, order: OrderBaseDomain) {
        adapterList[pagePosition].addData(order)
    }

    fun addLastItem(pagePosition: Int, order: OrderBaseDomain) {
        adapterList[pagePosition].addLastItem(order)
    }


    fun addDataItem(pagePosition: Int, order: OrderBaseDomain) {
        adapterList[pagePosition].addDataItem(order)
    }

    fun updateDataItem(position: Int, order: OrderBaseDomain, recyclerPosition: Int) {
        adapterList[position].updateDataItem(recyclerPosition, order)
    }

    fun removeItem(pagerPosition: Int, adapterPosition: Int) {
        adapterList[pagerPosition].removeItem(adapterPosition)
    }

    fun removeItemRange(pagerPosition: Int, startAdapterPosition: Int, endAdapterPosition: Int) {
        adapterList[pagerPosition].removeItemRange(startAdapterPosition, endAdapterPosition)
    }

    fun clearData(pagePosition: Int) {
        adapterList[pagePosition].clearDataList()
    }

    fun getDataCount(pagePosition: Int): Int {
        return adapterList[pagePosition].itemCount
    }

    fun clearAll(status: String) {
        adapterList.forEachIndexed { index, it ->
            it.updateList(arrayListOf(), status)
            showHideEmptyLayout(index, arrayListOf())
        }
    }

    private fun showHideEmptyLayout(pagePosition: Int, newList: ArrayList<OrderBaseDomain>) {
        val list = adapterList[pagePosition].itemCount
        if (list == 0) {
            emptyLayoutList[pagePosition].visible()
        } else {
            emptyLayoutList[pagePosition].gone()
        }
    }

    fun addDataList(pagePosition: Int, dataList: ArrayList<OrderBaseDomain>) {
        adapterList[pagePosition].addDataList(dataList)
        showHideEmptyLayout(pagePosition, dataList)
        binding.progressBottom.gone()
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val viewModelAdapter = TestOrderAdapter(context, onClickCallBack)
        val linearLayoutManager = LinearLayoutManager(context)

        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.tab_page_recycler,
            container,
            false
        )

        val page = pageList[position]
        val pageTitle = page.getTitle()

        binding.apply {
            shimmerViewContainer.apply {
                tag = pageTitle + "shimmer"
                startShimmer()
            }

            emptyLayoutList.add(layoutEmptyTask).apply {
                try {
                    emptyLayoutList[position].tag = pageTitle + "empty"
                } catch (e: IndexOutOfBoundsException) {
                    Timber.e(e)
                }
            }

            //Add set tag here so that we can access it on taskfragment
            swipeRefreshLayout.apply {
                tag = pageTitle + "swipe"
                setOnRefreshListener {
                    mRefreshOrderListener?.onSwipeRefreshOrder(position, pageTitle)
                }
            }

            recyclerView.apply {
                layoutManager = linearLayoutManager
                adapter = viewModelAdapter
                isNestedScrollingEnabled
                tag = pageTitle + "recycler"
            }

            progressBottom.tag = pageTitle + "progress_bottom"

            shimmerViewContainer.apply {
                gone()
                stopShimmer()
            }
        }

        val linearHorizontalLayoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        val linearVerticalLayoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        val mGridTwoColLayoutManager = GridLayoutManager(context,
            calculateNosOfColumn(context, 180),
            RecyclerView.VERTICAL,
            false)

        when (page.getLayoutType()) {
            LAYOUT_LINEAR_VERTICAL -> binding.recyclerView.layoutManager =
                linearVerticalLayoutManager
            LAYOUT_GRID_TWO_COL -> binding.recyclerView.layoutManager = mGridTwoColLayoutManager
            LAYOUT_LINEAR_HORIZONTAL -> {
                val snapHelper: SnapHelper = PagerSnapHelper()
                snapHelper.attachToRecyclerView(binding.recyclerView)
                binding.recyclerView.layoutManager = linearHorizontalLayoutManager
            }
        }

        adapterList.add(viewModelAdapter)

        container.addView(binding.root)
        return binding.root
    }

    fun getAdaptersSize(): Int {
        return adapterList.size
    }

    fun getTabView(position: Int, type: Int, tabTitles: List<Int?>, tabIcons: List<Int?>): View? {
        val mLayoutInflater = LayoutInflater.from(context)

        val view: View = if (tabTitles.size > TAB_COUNT_FOR_FIXED) {
            mLayoutInflater.inflate(R.layout.layout_custom_tab_item_scrollable, null)
        } else {
            mLayoutInflater.inflate(R.layout.layout_custom_tab_item, null)
        }

        val tvTitle = view.findViewById<TextView>(R.id.txt_tab)
        val imgView = view.findViewById<ImageView>(R.id.img_tab)

        if (type == TYPE_WHITE_BG) {
            tvTitle.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
            imgView.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary))
        } else if (type == TYPE_YELLOW_BG) {
            tvTitle.setTextColor(ContextCompat.getColor(context, R.color.color_white))
            imgView.setColorFilter(ContextCompat.getColor(context, R.color.color_white))
        }

        if (tabTitles.isNotEmpty()) {
            tvTitle.text = context.getString(tabTitles[position]!!)
        } else {
            tvTitle.visibility = View.GONE
        }

        if (tabIcons.isNotEmpty()) {
            imgView.setImageResource(tabIcons[position]!!)
        } else {
            imgView.visibility = View.GONE
        }

        return view
    }

    fun moveToLastItem() {
        binding.recyclerView.postDelayed({
            binding.recyclerView.scrollToPosition(binding.recyclerView.adapter!!.itemCount - 1)
        }, 1000)
    }

    fun moveToFirstItem() {
        binding.recyclerView.postDelayed({
            binding.recyclerView.scrollToPosition(0)
        }, 1000)
    }

    companion object {
        const val LAYOUT_LINEAR_VERTICAL = 0
        const val LAYOUT_LINEAR_HORIZONTAL = 1
        const val LAYOUT_GRID_TWO_COL = 2

        fun calculateNosOfColumn(context: Context, width: Int): Int {
            val displayMetrics = context.resources.displayMetrics
            val dpWidth = displayMetrics.widthPixels / displayMetrics.density
            return (dpWidth / width).roundToInt()
        }
    }
}