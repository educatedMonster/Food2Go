package com.example.kafiesta.network

import com.example.kafiesta.domain.WeeklyPaymentBaseNetworkDomain
import com.example.kafiesta.domain.WeeklyPaymentDomain
import com.google.gson.annotations.SerializedName

data class WeeklyPaymentBaseNetwork(
    val status: String,
    val message: String,
    val result: List<WeeklyPaymentBaseResponse>,
)

data class WeeklyPaymentBaseResponse(
    val id: Long,
    val amount: String,

    @SerializedName("date_from")
    val dateFrom: String,

    @SerializedName("date_to")
    val dateTo: String,

    val status: String,

    @SerializedName("merchant_name")
    val merchantName: String,

    @SerializedName("merchant_id")
    val merchantID: Long,

    @SerializedName("admin_id")
    val adminID: String? = null,

    @SerializedName("merchant_agreed_at")
    val merchantAgreedAt: String? = null,

    @SerializedName("admin_agreed_at")
    val adminAgreedAt: String? = null,

    @SerializedName("proof_url")
    val proofURL: String? = null,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("updated_at")
    val updatedAt: String,

    @SerializedName("deleted_at")
    val deletedAt: String? = null,
)

fun WeeklyPaymentBaseNetwork.asDomainModel(): WeeklyPaymentBaseNetworkDomain {
    return WeeklyPaymentBaseNetworkDomain(
        status = status,
        message = message,
        result = result.map { it.asDomainModel() }
    )
}

fun WeeklyPaymentBaseResponse.asDomainModel(): WeeklyPaymentDomain {
    return WeeklyPaymentDomain(
        id = id,
        amount = amount,
        dateFrom = dateFrom,
        dateTo = dateTo,
        status = status,
        merchantName = merchantName,
        merchantID = merchantID,
        adminID = adminID,
        merchantAgreedAt = merchantAgreedAt,
        adminAgreedAt = adminAgreedAt,
        proofURL = proofURL,
        createdAt = createdAt,
        updatedAt = updatedAt,
        deletedAt = deletedAt
    )
}

