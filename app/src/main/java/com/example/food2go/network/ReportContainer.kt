package com.example.food2go.network

import com.example.food2go.domain.ReportBaseNetworkDomain
import com.example.food2go.domain.ReportBaseResponseDomain
import com.google.gson.annotations.SerializedName

data class ReportBaseNetwork(
    val status: String,
    val message: String,
    val result: ReportBaseResponse,
)

data class ReportBaseResponse(
    @SerializedName("total_orders")
    val totalOrders: Long,

    @SerializedName("total_sales")
    val totalSales: String,

    @SerializedName("total_delivery_charge")
    val totalDeliveryCharge: String,

    @SerializedName("total_convenience_fee")
    val totalConvenienceFee: String,

    @SerializedName("items_sold")
    val itemsSold: Map<String, Long>
)

fun ReportBaseNetwork.asDomainModel(): ReportBaseNetworkDomain {
    return ReportBaseNetworkDomain(
        status = status,
        message = message,
        result = result.asDomainModel()
    )
}

fun ReportBaseResponse.asDomainModel(): ReportBaseResponseDomain {
    return ReportBaseResponseDomain(
        totalOrders = totalOrders,
        totalSales = totalSales,
        totalDeliveryCharge = totalDeliveryCharge,
        totalConvenienceFee = totalConvenienceFee,
        itemsSold = itemsSold
    )
}
