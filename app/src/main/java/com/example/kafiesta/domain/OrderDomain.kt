package com.example.kafiesta.domain

import com.google.gson.annotations.SerializedName

data class OrderBaseNetworkDomain (
    val status: String,
    val message: String,
    val result: List<OrderBaseDomain>
)


data class OrderBaseDomain (
    val order: OrderDomain,
    val orderList: List<OrderListDomain>
)


data class OrderDomain (
    val id: Long,
    val customerUserID: Long,
    val merchantUserID: Long,
    val userShopsID: Long,
    val modeOfPayment: String,
    val address: String,
    val contact: String,
    val remarks: String? = null,
    val deliveryCharge: String,
    val convenienceFee: String,
    val proofURL: String? = null,
    val note: String,
    val total: String,
    val status: String,
    val changedAtPreparing: String? = null,
    val changedAtDelivered: String? = null,
    val changedAtCompleted: String? = null,
    val collectedAt: String? = null,
    val deletedAt: String? = null,
    val createdAt: String,
    val updatedAt: String? = null,
    val users: OrderUsersDomain
) {
    val orderId = id.toString()
    private val netTotal = total.toDouble() + deliveryCharge.toDouble() + convenienceFee.toDouble()
    val netTotalString = netTotal.toString()
}


data class OrderUsersDomain (
    val id: Long,
    val firstName: String,
    val lastName: String,
    val fullName: String,
    val email: String,
    val rememberToken: String? = null,
    val status: String,
    val role: String,
    val deletedAt: String? = null,
    val createdAt: String,
    val updatedAt: String,
    val userInformations: OrderUserInformationsDomain
)


data class OrderUserInformationsDomain (
    val id: Long,
    val userID: Long,
    val primaryContact: String,
    val secondaryContact: String,
    val completeAddress: String,
)


data class OrderListDomain (
    val id: Long,
    val ordersID: Long,
    val productID: Long,
    val productName: String,
    val productPrice: String,
    val quantity: Long,
    val subtotal: String,
    val createdAt: String? = null,
    val updatedAt: String? = null
) {
    val quantityString = quantity.toString()
}