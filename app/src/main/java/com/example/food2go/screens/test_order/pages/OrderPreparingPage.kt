package com.example.food2go.screens.test_order.pages

import com.example.food2go.constants.OrderConst
import com.example.food2go.screens.test_order.interfaces_test_order.Page
import com.example.food2go.screens.test_order.interfaces_test_order.PageFactory
import com.example.food2go.screens.test_order.interfaces_test_order.ViewPagerRecyclerAdapter

class OrderPreparingPage : Page {
    private var position = 0
    private val layoutType: Int = ViewPagerRecyclerAdapter.LAYOUT_LINEAR_VERTICAL

    override fun type(pageFactory: PageFactory): Int = pageFactory.type(this)

    override fun getTitle(): String = pageTitle

    override fun getLayoutType(): Int = layoutType

    override fun setPosition(position: Int) {
        this.position = position
    }

    override fun getPosition(): Int {
        return position
    }

    companion object {
        const val pageTitle: String = OrderConst.ORDER_PREPARING
    }
}