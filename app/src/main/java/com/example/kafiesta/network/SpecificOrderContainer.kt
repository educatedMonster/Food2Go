package com.example.kafiesta.network

import com.example.kafiesta.domain.*
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
    val updatedAt: String,

    val users: SpecificOrderUsersResponse
)

data class SpecificOrderUsersResponse (
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
    val createdAt: String,

    @SerializedName("updated_at")
    val updatedAt: String,

    @SerializedName("user_informations")
    val userInformations: SpecificOrderUserInformationsResponse
)

data class SpecificOrderUserInformationsResponse (
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
        updatedAt = updatedAt,
        users = users.asDomainModel()
    )
}

fun SpecificOrderUsersResponse.asDomainModel(): SpecificOrderUsersDomain {
    return  SpecificOrderUsersDomain(
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
        userInformations = userInformations.asDomainModel(),
    )
}

fun SpecificOrderUserInformationsResponse.asDomainModel(): SpecificOrderUserInformationsDomain {
    return  SpecificOrderUserInformationsDomain(
        id = id,
        userID = userID,
        primaryContact = primaryContact,
        secondaryContact = secondaryContact,
        completeAddress = completeAddress,
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









