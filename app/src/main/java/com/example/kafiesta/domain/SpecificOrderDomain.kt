package com.example.kafiesta.domain

data class SpecificOrderNetworkDomain (
    val status: String,
    val message: String,
    val result: SpecificBaseOrderDomain
)

data class SpecificBaseOrderDomain (
    val order: SpecificOrderDomain,
    val orderList: List<SpecificOrderListDomain>
)

data class SpecificOrderDomain (
    val id: Long,
    val customerUserID: Long,
    val collectedAt: String? = null,
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
    val deletedAt: String? = null,
    val createdAt: String,
    val updatedAt: String
)

data class SpecificOrderListDomain (
    val id: Long,
    val ordersID: Long,
    val productID: Long,
    val productName: String,
    val productPrice: String,
    val quantity: Long,
    val subtotal: String,
    val createdAt: String,
    val updatedAt: String
)