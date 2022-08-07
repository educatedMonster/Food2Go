package com.example.kafiesta.domain

import java.io.Serializable

data class UserResultDomain(
    val status: String,
    val result: ProfileDomain,
    val message: String,
)

data class ProfileDomain(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val email: String,
    val status: String,
    val role: String,
    val userInformations: UserInformationsDomain? = null,
    val user_shop: UserShopDomain? = null,
) {
    val fullName = "$firstName $lastName"
}

data class UserInformationsDomain(
    val complete_address: String,
    val primary_contact: String,
    val secondary_contact: String,
) : Serializable

data class UserShopDomain(
    val name: String,
    val address: String,
    val contact: String,
    val open_hour: String? = null,
    val close_hour: String? = null,
    val status: String,
    val monday: Long? = 1L,
    val tuesday: Long? = 1L,
    val wednesday: Long? = 1L,
    val thursday: Long? = 1L,
    val friday: Long? = 1L,
    val saturday: Long,
    val sunday: Long,
    val pm_gcash: Long? = 1L,
    val pm_cod: Long? = 1L,
    val is_active: Long? = 1L,
) : Serializable {
    val is_monday: Boolean = monday == 1L
    val is_tuesday: Boolean = tuesday == 1L
    val is_wednesday: Boolean = wednesday == 1L
    val is_thursday: Boolean = thursday == 1L
    val is_friday: Boolean = friday == 1L
    val is_saturday: Boolean = saturday == 1L
    val is_sunday: Boolean = sunday == 1L
    val is_pm_gcash: Boolean = pm_gcash == 1L
    val is_pm_cod: Boolean = pm_cod == 1L
    val isActive: Boolean = is_active == 1L
}