package com.example.food2go.domain

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

data class UserBaseDomain(
    val status: String,
    val message: String,
    val result: ProfileDomain,
)

data class ProfileDomain(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val fullName: String,
    val email: String,
    val password: String? = null,
    val status: String,
    val role: String,
    val userInformation: UserInformationDomain? = null,
    val user_shop: UserShopDomain? = null,
) {
    val uidString = "UID-$id"
}

data class UserInformationDomain(
    val id: Long,
    val user_id: Long,
    val complete_address: String,
    val primary_contact: String,
    val secondary_contact: String,
)

data class UserShopDomain(
    val id: Long,
    val user_id: Long,
    val name: String,
    val address: String,
    val contact: String,
    val open_hour: String? = null,
    val close_hour: String? = null,
    val status: String,
    val monday: Long,
    val tuesday: Long,
    val wednesday: Long,
    val thursday: Long,
    val friday: Long,
    val saturday: Long,
    val sunday: Long,
    val pm_gcash: Long,
    val pm_cod: Long,
    val is_active: Long,
    val delivery_charge: String? = null,
    val imageURL: String? = null,
    val description: String? = null,
) {
    val is_monday: Boolean = monday == 1L
    val is_Status: Boolean = status == "open"
    val is_tuesday: Boolean = tuesday == 1L
    val is_wednesday: Boolean = wednesday == 1L
    val is_thursday: Boolean = thursday == 1L
    val is_friday: Boolean = friday == 1L
    val is_saturday: Boolean = saturday == 1L
    val is_sunday: Boolean = sunday == 1L
    val is_pm_gcash: Boolean = pm_gcash == 1L
    val is_pm_cod: Boolean = pm_cod == 1L
    val is_active_inactive: Boolean = is_active == 1L

    // display the current state for switch
    val isStatusString = if (is_Status) {
        "Open"
    } else {
        "Closed"
    }

    val isActiveInactiveString = if (is_active_inactive) {
        "Active"
    } else {
        "Inactive"
    }

    val timeOpenString = timeAMPM(open_hour!!)
    val timeCloseString = timeAMPM(close_hour!!)
}

// displaying "0:00 PM"
fun timeAMPM(time: String): String{
    var displayTime = ""
    try {
        val sdf = SimpleDateFormat("H:mm a")
        val dateObj: Date = sdf.parse(time)
        displayTime = SimpleDateFormat("H:mm a").format(dateObj)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return displayTime
}