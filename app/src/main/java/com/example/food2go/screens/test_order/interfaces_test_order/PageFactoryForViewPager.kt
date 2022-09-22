package com.example.food2go.screens.test_order.interfaces_test_order

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.food2go.R
import com.example.food2go.screens.test_order.pages.OrderCompletedPage
import com.example.food2go.screens.test_order.pages.OrderDeliveryPage
import com.example.food2go.screens.test_order.pages.OrderPendingPage
import com.example.food2go.screens.test_order.pages.OrderPreparingPage

class PageFactoryForViewPager(
    private var mContext: Context,
) : PageFactory {

    companion object {
        const val PAGE_ORDER_PENDING = 1000
        const val PAGE_ORDER_PREPARING = 1001
        const val PAGE_ORDER_DELIVERY = 1002
        const val PAGE_ORDER_COMPLETED = 1003
    }

    override fun type(orderPendingPage: OrderPendingPage): Int {
        return PAGE_ORDER_PENDING
    }

    override fun type(orderPreparingPage: OrderPreparingPage): Int {
        return PAGE_ORDER_PREPARING
    }

    override fun type(orderDeliveryPage: OrderDeliveryPage): Int {
        return PAGE_ORDER_DELIVERY
    }

    override fun type(orderCompletedPage: OrderCompletedPage): Int {
        return PAGE_ORDER_COMPLETED
    }

    override fun createViewGroup(
        type: Int,
        inflater: LayoutInflater,
        parent: ViewGroup,
    ): ViewGroup {
        var page: ViewGroup? = null

        when (type) {
            PAGE_ORDER_PENDING,
            PAGE_ORDER_PREPARING,
            PAGE_ORDER_DELIVERY,
            PAGE_ORDER_COMPLETED,
            -> page = inflater.inflate(R.layout.common_tab_page, parent, false) as ViewGroup
        }

        if (page == null) {
            throw TypeNotSupportedException.create(String.format("Page: %d", type))
        }

        return page
    }

    fun getContext(): Context {
        return mContext
    }

    fun setContext(context: Context) {
        mContext = context
    }
}

class TypeNotSupportedException(message: String) : RuntimeException(message) {
    companion object {
        fun create(message: String): TypeNotSupportedException {
            return TypeNotSupportedException(message)
        }
    }
}
