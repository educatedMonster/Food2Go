package com.example.kafiesta.screens.test.pages

import com.example.kafiesta.constants.OrderConst
import com.example.kafiesta.screens.test.interfaces_test_order.Page
import com.example.kafiesta.screens.test.interfaces_test_order.PageFactory
import com.example.kafiesta.screens.test.interfaces_test_order.ViewPagerRecyclerAdapter

class OrderPendingPage: Page {
    private var position = 0
    private val layoutType: Int = ViewPagerRecyclerAdapter.LAYOUT_GRID_TWO_COL

    override fun type(pageFactory: PageFactory): Int = pageFactory.type(this)

    override fun getTitle(): String = pageTitle

    override fun getLayoutType(): Int = layoutType

    override fun setPosition(position: Int) {
        this.position = position
    }

    override fun getPosition(): Int {
        return this.position
    }

    companion object {
        const val pageTitle: String = OrderConst.ORDER_PENDING
    }
}