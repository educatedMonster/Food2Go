package com.example.kafiesta.network

import com.example.kafiesta.domain.ResultDomain
import com.example.kafiesta.domain.UserResultDomain
import com.google.gson.annotations.SerializedName

data class UserResultBaseNetwork(
    @SerializedName("status")
    val status: String,

    @SerializedName("message")
    val message: String,

    @SerializedName("result")
    val result: Result,
)

data class Result(
    @SerializedName("id")
    val id: Long,

    @SerializedName("first_name")
    val firstName: String,

    @SerializedName("last_name")
    val lastName: String,

    @SerializedName("full_name")
    val fullName: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("remember_token")
    val rememberToken: Any? = null,

    @SerializedName("status")
    val status: String,

    @SerializedName("role")
    val role: String,

    @SerializedName("deleted_at")
    val deletedAt: Any? = null,

    @SerializedName("created_at")
    val createdAt: Any? = null,

    @SerializedName("updated_at")
    val updatedAt: Any? = null,

    @SerializedName("user_informations")
    val userInformations: Any? = null,

    @SerializedName("user_shop")
    val userShop: Any? = null,
)

fun UserResultBaseNetwork.asDomainModel(): UserResultDomain {
    return UserResultDomain(
        status = status,
        message = message,
        result = result.asDomainModel()
    )
}

fun Result.asDomainModel(): ResultDomain {
    return ResultDomain(
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
        userInformations = userInformations,
        userShop = userShop
    )
}
