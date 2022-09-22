package com.example.food2go.network

import com.example.food2go.domain.ProductBaseDomain
import com.example.food2go.domain.ProductDomain
import com.google.gson.annotations.SerializedName

data class ProductBaseNetwork(
    @SerializedName("status")
    val status: String,

    @SerializedName("message")
    val message: String,

    @SerializedName("result")
    val result: ProductResponse,
)

data class ProductResponse (
    val id: Long,

    @SerializedName("user_id")
    val userID: Long,

    val name: String,
    val description: String,
    val price: String,
    val status: String,
    val tags: String,
    val imageURL: String,
)

fun ProductBaseNetwork.asDomainModel(): ProductBaseDomain {
    return ProductBaseDomain(
        status = status,
        message = message,
        product = result.asDomainModel(),
    )
}

fun ProductResponse.asDomainModel(): ProductDomain {
    return ProductDomain(
        id = id,
        userID = userID,
        name = name,
        description = description,
        price = price,
        status = status,
        tags = tags,
        imageURL = imageURL
    )
}