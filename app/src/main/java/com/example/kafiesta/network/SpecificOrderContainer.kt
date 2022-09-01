package com.example.kafiesta.network

import com.example.kafiesta.domain.SpecificBaseOrderDomain
import com.example.kafiesta.domain.SpecificOrderDomain
import com.example.kafiesta.domain.SpecificOrderListDomain
import com.example.kafiesta.domain.SpecificOrderNetworkDomain
import com.google.gson.annotations.SerializedName

data class SpecificOrderBaseNetwork (
    val status: String,
    val message: String,
    val result: SpecificOrderBaseResponse
)

data class SpecificOrderBaseResponse (
    val order: SpecificOrderResponse,

    @SerializedName("order_list")
    val orderList: List<SpecificOrderListResponse>
)

data class SpecificOrderResponse (
    val id: Long,

    @SerializedName("customer_user_id")
    val customerUserID: Long,

    @SerializedName("collected_at")
    val collectedAt: String? = null,

    @SerializedName("merchant_user_id")
    val merchantUserID: Long,

    @SerializedName("user_shops_id")
    val userShopsID: Long,

    @SerializedName("mode_of_payment")
    val modeOfPayment: String,

    val address: String,
    val contact: String,
    val remarks: String? = null,

    @SerializedName("delivery_charge")
    val deliveryCharge: String,

    @SerializedName("convenience_fee")
    val convenienceFee: String,

    @SerializedName("proof_url")
    val proofURL: String? = null,

    val note: String,
    val total: String,
    val status: String,

    @SerializedName("changed_at_preparing")
    val changedAtPreparing: String? = null,

    @SerializedName("changed_at_delivered")
    val changedAtDelivered: String? = null,

    @SerializedName("changed_at_completed")
    val changedAtCompleted: String? = null,

    @SerializedName("deleted_at")
    val deletedAt: String? = null,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("updated_at")
    val updatedAt: String
)

data class SpecificOrderListResponse (
    val id: Long,

    @SerializedName("orders_id")
    val ordersID: Long,

    @SerializedName("product_id")
    val productID: Long,

    @SerializedName("product_name")
    val productName: String,

    @SerializedName("product_price")
    val productPrice: String,

    val quantity: Long,
    val subtotal: String,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("updated_at")
    val updatedAt: String
)

fun SpecificOrderBaseNetwork.asDomainModel(): SpecificOrderNetworkDomain {
    return  SpecificOrderNetworkDomain(
        status = status,
        message = message,
        result = result.asDomainModel()
    )
}

fun SpecificOrderBaseResponse.asDomainModel(): SpecificBaseOrderDomain {
    return  SpecificBaseOrderDomain(
        order = order.asDomainModel(),
        orderList = orderList.map { it.asDomainModel() }
    )
}

fun SpecificOrderResponse.asDomainModel(): SpecificOrderDomain {
    return  SpecificOrderDomain(
        id = id,
        customerUserID = customerUserID,
        collectedAt = collectedAt,
        merchantUserID = merchantUserID,
        userShopsID = userShopsID,
        modeOfPayment = modeOfPayment,
        address = address,
        contact = contact,
        remarks = remarks,
        deliveryCharge = deliveryCharge,
        convenienceFee = convenienceFee,
        proofURL = proofURL,
        note = note,
        total = total,
        status = status,
        changedAtPreparing = changedAtPreparing,
        changedAtDelivered = changedAtDelivered,
        changedAtCompleted = changedAtCompleted,
        deletedAt = deletedAt,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun SpecificOrderListResponse.asDomainModel(): SpecificOrderListDomain {
    return  SpecificOrderListDomain(
        id = id,
        ordersID = ordersID,
        productID = productID,
        productName = productName,
        productPrice = productPrice,
        quantity = quantity,
        subtotal = subtotal,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}









