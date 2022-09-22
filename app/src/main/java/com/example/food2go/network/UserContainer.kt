package com.example.food2go.network

import com.example.food2go.domain.*
import com.google.gson.annotations.SerializedName

data class UserBaseNetwork(
    val status: String,
    val message: String,
    val result: UserProfileResponse
)

data class UserProfileResponse(
    val id: Long,

    @SerializedName("first_name")
    val first_name: String,

    @SerializedName("last_name")
    val last_name: String,

    @SerializedName("full_name")
    val fullName: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String? = null,

    @SerializedName("remember_token")
    val rememberToken: Any? = null,

    @SerializedName("status")
    val status: String,

    @SerializedName("role")
    val role: String,

    @SerializedName("user_informations")
    val userInformation: UserInformationResponse,

    @SerializedName("user_shop")
    val userShop: UserShopResponse

    )

data class UserInformationResponse(
    @SerializedName("id")
    val id: Long,

    @SerializedName("user_id")
    val user_id: Long,

    @SerializedName("primary_contact")
    val primary_contact: String,

    @SerializedName("secondary_contact")
    val secondary_contact: String,

    @SerializedName("complete_address")
    val complete_address: String
)

data class UserShopResponse(
    @SerializedName("id")
    val id: Long,

    @SerializedName("user_id")
    val user_id: Long,

    @SerializedName("name")
    val name: String,

    @SerializedName("address")
    val address: String,

    @SerializedName("contact")
    val contact: String,

    @SerializedName("open_hour")
    val openHour: String? = null,

    @SerializedName("close_hour")
    val closeHour: String? = null,

    val status: String,
    val monday: Long,
    val tuesday: Long,
    val wednesday: Long,
    val thursday: Long,
    val friday: Long,
    val saturday: Long,
    val sunday: Long,

    @SerializedName("pm_cod")
    val pmCod: Long,

    @SerializedName("pm_gcash")
    val pmGcash: Long,

    @SerializedName("is_active")
    val isActive: Long,

    @SerializedName("delivery_charge")
    val deliveryCharge: String? = null,

    @SerializedName("image_url")
    val imageURL: String? = null,

    val description: String? = null
)


fun UserBaseNetwork.asDomainModel(): UserBaseDomain {
    return UserBaseDomain(
        status = status,
        result = result.asDomainModel(),
        message = message,
    )
}

fun UserProfileResponse.asDomainModel(): ProfileDomain {
    return ProfileDomain(
        id = id,
        firstName = first_name,
        lastName = last_name,
        fullName = fullName,
        email = email,
        password = password,
        status = status,
        role = role,
        userInformation = userInformation.asDomainModel(),
        user_shop = userShop.asDomainModel()
    )
}

fun UserInformationResponse.asDomainModel(): UserInformationDomain {
    return UserInformationDomain(
        id = id,
        user_id = user_id,
        complete_address = complete_address,
        primary_contact = primary_contact,
        secondary_contact = secondary_contact
    )
}

fun UserShopResponse.asDomainModel(): UserShopDomain {
    return UserShopDomain(
        id = id,
        user_id = user_id,
        name = name,
        address = address,
        contact = contact,
        open_hour = openHour,
        close_hour = closeHour,
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
        is_active = isActive,
        delivery_charge = deliveryCharge,
        imageURL = imageURL,
        description = description
    )
}