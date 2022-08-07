package com.example.kafiesta.network

import com.example.kafiesta.domain.ProfileDomain
import com.example.kafiesta.domain.UserInformationsDomain
import com.example.kafiesta.domain.UserResultDomain
import com.example.kafiesta.domain.UserShopDomain
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UserBaseNetwork(
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
    val first_name: String,

    @SerializedName("last_name")
    val last_name: String,

    @SerializedName("full_name")
    val full_name: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("remember_token")
    val remember_token: String? = null,

    @SerializedName("status")
    val status: String,

    @SerializedName("role")
    val role: String,

    @SerializedName("deleted_at")
    val deletedAt: String? = null,

    @SerializedName("created_at")
    val createdAt: String? = null,

    @SerializedName("updated_at")
    val updatedAt: String? = null,

    @SerializedName("user_informations")
    val userInformations: UserInformations? = null,

    @SerializedName("user_shop")
    val user_shop: UserShop? = null,

    ) : Serializable

data class UserInformations(
    @SerializedName("user_id")
    val userID: Long,

    @SerializedName("complete_address")
    val complete_address: String,

    @SerializedName("primary_contact")
    val primary_contact: String,

    @SerializedName("secondary_contact")
    val secondary_contact: String,

    @SerializedName("deleted_at")
    val deletedAt: String? = null,

    @SerializedName("created_at")
    val createdAt: String? = null,

    @SerializedName("updated_at")
    val updatedAt: String? = null,

    ) : Serializable

data class UserShop(
    @SerializedName("id")
    val id: Long,

    @SerializedName("user_id")
    val userID: Long,

    @SerializedName("name")
    val name: String,

    @SerializedName("address")
    val address: String,

    @SerializedName("contact")
    val contact: String,

    @SerializedName("open_hour")
    val open_hour: String? = null,

    @SerializedName("close_hour")
    val close_hour: String? = null,

    @SerializedName("status")
    val status: String,

    @SerializedName("monday")
    val monday: Long,

    @SerializedName("tuesday")
    val tuesday: Long,

    @SerializedName("wednesday")
    val wednesday: Long,

    @SerializedName("thursday")
    val thursday: Long,

    @SerializedName("friday")
    val friday: Long,

    @SerializedName("saturday")
    val saturday: Long,

    @SerializedName("sunday")
    val sunday: Long,

    @SerializedName("pm_gcash")
    val pmGcash: Long,

    @SerializedName("pm_cod")
    val pmCod: Long,

    @SerializedName("is_active")
    val is_active: Long,

    @SerializedName("delivery_charge")
    val deliveryCharge: String,

    @SerializedName("deleted_at")
    val deletedAt: String? = null,

    @SerializedName("created_at")
    val createdAt: String? = null,

    @SerializedName("updated_at")
    val updatedAt: String? = null,
) : Serializable


fun UserBaseNetwork.asDomainModel(): UserResultDomain {
    return UserResultDomain(
        status = status,
        result = result.asDomainModel(),
        message = message,
    )
}

fun Result.asDomainModel(): ProfileDomain {
    return ProfileDomain(
        id = id,
        firstName = first_name,
        lastName = last_name,
        email = email,
        status = status,
        role = role,
        userInformations = userInformations?.asDomainModel(),
        user_shop = user_shop?.asDomainModel()
    )
}

fun UserInformations.asDomainModel(): UserInformationsDomain {
    return UserInformationsDomain(
        complete_address = complete_address,
        primary_contact = primary_contact,
        secondary_contact = secondary_contact
    )
}

fun UserShop.asDomainModel(): UserShopDomain {
    return UserShopDomain(
        name = name,
        address = address,
        contact = contact,
        open_hour = open_hour,
        close_hour = close_hour,
        status = status,
        monday = monday,
        tuesday = tuesday,
        wednesday = wednesday,
        thursday = thursday,
        friday = friday,
        saturday = saturday,
        sunday = sunday,
        pm_gcash = pmGcash,
        pm_cod = pmCod,
        is_active = is_active
    )
}