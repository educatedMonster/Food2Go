package com.example.food2go.domain

data class ReportBaseNetworkDomain(
    val status: String,
    val message: String,
    val result: ReportBaseResponseDomain,
)

data class ReportBaseResponseDomain(
    val totalOrders: Long,
    val totalSales: String,
    val totalDeliveryCharge: String,
    val totalConvenienceFee: String,
    val itemsSold: Map<String, Long>
) {
   val orders = totalOrders.toString()
}

data class ItemSold (
    var item: String? = "",
    var quantity: Long = 0
) {
    val quantityString = quantity.toString()
}