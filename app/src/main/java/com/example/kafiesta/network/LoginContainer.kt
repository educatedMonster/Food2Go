package com.example.kafiesta.network

import com.example.kafiesta.domain.LoginDataDomain
import com.example.kafiesta.domain.LoginInformationsDomain
import com.example.kafiesta.domain.LoginProfileDomain
import com.example.kafiesta.domain.LoginUserDomain
import com.google.gson.annotations.SerializedName

data class LoginBaseNetwork(
    @SerializedName("status")
    val status: String,

    @SerializedName("data")
    val data: LoginData,

    @SerializedName("message")
    val message: String,
)

data class LoginData(
    @SerializedName("token")
    val token: String,

    @SerializedName("token_type")
    val tokenType: String,

    @SerializedName("expires_in")
    val expiresIn: Long,

    @SerializedName("profile")
    val profile: LoginProfile,
)


data class LoginProfile(
    @SerializedName("id")
    val id: Long,

    @SerializedName("first_name")
    val firstName: String,

    @SerializedName("last_name")
    val lastName: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("status")
    val status: String,

    @SerializedName("role")
    val role: String,

    @SerializedName("user_informations")
    val userInformations: LoginUserInformations,
)

data class LoginUserInformations(
    @SerializedName("id")
    val id: Long,

    @SerializedName("user_id")
    val userID: Long,

    @SerializedName("primary_contact")
    val primaryContact: String,

    @SerializedName("secondary_contact")
    val secondaryContact: String,

    @SerializedName("complete_address")
    val completeAddress: String,

    )


fun LoginBaseNetwork.asDomainModel(): LoginUserDomain {
    return LoginUserDomain(
        status = status,
        data = data.asDomainModel(),
        message = message,
    )
}

fun LoginData.asDomainModel(): LoginDataDomain {
    return LoginDataDomain(
        token = token,
        tokenType = tokenType,
        expiresIn = expiresIn,
        profile = profile.asDomainModel()
    )
}

fun LoginProfile.asDomainModel(): LoginProfileDomain {
    return LoginProfileDomain(
        id = id,
        firstName = firstName,
        lastName = lastName,
        email = email,
        status = status,
        role = role,
        userInformations = userInformations.asDomainModel()
    )
}

fun LoginUserInformations.asDomainModel(): LoginInformationsDomain {
    return LoginInformationsDomain(
        id = id,
        userID = userID,
        primaryContact = primaryContact,
        secondaryContact = secondaryContact,
        completeAddress = completeAddress
    )
}


