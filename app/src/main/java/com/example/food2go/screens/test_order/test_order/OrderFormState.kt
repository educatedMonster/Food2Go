package com.example.food2go.screens.test_order.test_order

import com.example.food2go.constants.OrderConst
import com.example.food2go.domain.OrderBaseDomain

data class OrderFormState(
    var orderPosition: Int = 0,
    var orderTitle:String = "",
    var certainOrderStatus: String = "",
    var isLoading: Boolean = false,
    var list: List<OrderBaseDomain> = arrayListOf()
) {
    fun getPagePosition(certainTask: String): Int? {
        return when (certainTask) {
            OrderConst.ORDER_PENDING -> 0
            OrderConst.ORDER_PREPARING -> 1
            OrderConst.ORDER_DELIVERY -> 2
            OrderConst.ORDER_COMPLETED -> 3
            else -> null
        }
    }
}