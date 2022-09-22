package com.example.food2go.network

import com.example.food2go.domain.LoginDataDomain
import com.example.food2go.domain.LoginInformationsDomain
import com.example.food2go.domain.LoginProfileDomain
import com.example.food2go.domain.LoginBaseDomain
import com.google.gson.annotations.SerializedName

data class LoginBaseNetwork(
    val status: String,
    val data: LoginResponse,
    val message: String,
)

data class LoginResponse(
    val token: String,

    @SerializedName("token_type")
    val tokenType: String,

    @SerializedName("expires_in")
    val expiresIn: Long,

    val profile: LoginProfile,
)


data class LoginProfile(
    @SerializedName("id")
    val userID: Long,

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
    val userInformations: LoginUserInformations?= null
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
    val completeAddress: String
    )

fun LoginBaseNetwork.asDomainModel(): LoginBaseDomain {
    return LoginBaseDomain(
        status = status,
        data = data.asDomainModel(),
        message = message,
    )
}

fun LoginResponse.asDomainModel(): LoginDataDomain {
    return LoginDataDomain(
        token = token,
        tokenType = tokenType,
        expiresIn = expiresIn,
        profile = profile.asDomainModel()
    )
}

fun LoginProfile.asDomainModel(): LoginProfileDomain {
    return LoginProfileDomain(
        userId = userID,
        firstName = firstName,
        lastName = lastName,
        email = email,
        status = status,
        role = role,
        userInformations = userInformations?.asDomainModel()
    )
}

fun LoginUserInformations.asDomainModel(): LoginInformationsDomain {
    return LoginInformationsDomain(
        infoId = id,
        userID = userID,
        primaryContact = primaryContact,
        secondaryContact = secondaryContact,
        completeAddress = completeAddress
    )
}


