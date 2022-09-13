package com.example.kafiesta.network

import com.example.kafiesta.domain.*
import com.google.gson.annotations.SerializedName


data class OrderListBaseNetwork (
    val status: String,
    val message: String,
    val result: List<OrderListBaseResponse>? = null
)

data class SpecificOrderBaseNetwork (
    val status: String,
    val message: String,
    val result: OrderListBaseResponse
)

data class OrderListBaseResponse (
    val order: OrderResponse,

    @SerializedName("order_list")
    val orderList: List<OrderListResponse>
)


data class OrderResponse (
    val id: Long,

    @SerializedName("customer_user_id")
    val customerUserID: Long,

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

    val note: String? = null,
    val total: Long? = null,
    val status: String,

    @SerializedName("changed_at_preparing")
    val changedAtPreparing: String? = null,

    @SerializedName("changed_at_delivered")
    val changedAtDelivered: String? = null,

    @SerializedName("changed_at_completed")
    val changedAtCompleted: String? = null,

    @SerializedName("collected_at")
    val collectedAt: String? = null,

    @SerializedName("deleted_at")
    val deletedAt: String? = null,

    @SerializedName("created_at")
    val createdAt: String? = null,

    @SerializedName("updated_at")
    val updatedAt: String? = null,

    val users: OrderUsersResponse? = null,
)


data class OrderUsersResponse (
    val id: Long,

    @SerializedName("first_name")
    val firstName: String,

    @SerializedName("last_name")
    val lastName: String,

    @SerializedName("full_name")
    val fullName: String,

    val email: String,

    @SerializedName("remember_token")
    val rememberToken: String? = null,

    val status: String,
    val role: String,

    @SerializedName("deleted_at")
    val deletedAt: String? = null,

    @SerializedName("created_at")
    val createdAt: String? = null,

    @SerializedName("updated_at")
    val updatedAt: String? = null,

    @SerializedName("user_informations")
    val userInformations: OrderUserInformationsResponse? = null,
)


data class OrderUserInformationsResponse (
    val id: Long,

    @SerializedName("user_id")
    val userID: Long,

    @SerializedName("primary_contact")
    val primaryContact: String,

    @SerializedName("secondary_contact")
    val secondaryContact: String,

    @SerializedName("complete_address")
    val completeAddress: String,

    @SerializedName("deleted_at")
    val deletedAt: String? = null,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("updated_at")
    val updatedAt: String
)


data class OrderListResponse (
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
    val createdAt: String? = null,

    @SerializedName("updated_at")
    val updatedAt: String? = null
)

fun OrderListBaseNetwork.asDomainModel(): OrderListNetworkDomain {
    return OrderListNetworkDomain (
        status = status,
        message = message,
        result = result?.map { it.asDomainModel() }
    )
}

fun SpecificOrderBaseNetwork.asDomainModel(): SpecificOrderNetworkDomain {
    return  SpecificOrderNetworkDomain(
        status = status,
        message = message,
        result = result.asDomainModel()
    )
}

fun OrderListBaseResponse.asDomainModel(): OrderBaseDomain {
    return OrderBaseDomain (
        order = order.asDomainModel(),
        orderList = orderList.map { it.asDomainModel() }
    )
}

fun OrderResponse.asDomainModel(): OrderDomain {
    return OrderDomain (
        id = id,
        customerUserID = customerUserID,
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
        collectedAt = collectedAt,
        deletedAt = deletedAt,
        createdAt = createdAt,
        updatedAt = updatedAt,
        users = users?.asDomainModel(),
    )
}

fun OrderUsersResponse.asDomainModel(): OrderUsersDomain {
    return OrderUsersDomain (
        id = id,
        firstName = firstName,
        lastName = lastName,
        fullName = fullName,
        email = email,
        rememberToken = rememberToken,
        status = status,
        role = role,
        deletedAt = deletedAt,
        createdAt = createdAt,
        updatedAt = updatedAt,
        userInformations = userInformations?.asDomainModel(),
    )
}

fun OrderUserInformationsResponse.asDomainModel(): OrderUserInformationsDomain {
    return OrderUserInformationsDomain (
        id = id,
        userID = userID,
        primaryContact = primaryContact,
        secondaryContact = secondaryContact,
        completeAddress = completeAddress,
    )
}

fun OrderListResponse.asDomainModel(): OrderListDomain {
    return OrderListDomain (
        id = id,
        ordersID = ordersID,
        productID = productID,
        productName = productName,
        productPrice = productPrice,
        quantity = quantity,
        subtotal = subtotal,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}




