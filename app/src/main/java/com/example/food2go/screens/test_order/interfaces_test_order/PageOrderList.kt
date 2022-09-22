package com.example.food2go.screens.test_order.interfaces_test_order

interface Page {
    fun getTitle(): String
    fun type(pageFactory: PageFactory): Int
    fun getLayoutType(): Int
    fun setPosition(position: Int)
    fun getPosition(): Int
}

interface RecyclerItemListener {

}

interface RefreshOrderListener {
    fun onSwipeRefreshOrder(orderPosition: Int, orderTitle: String)
}