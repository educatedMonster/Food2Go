package com.example.kafiesta.network

import com.example.kafiesta.domain.ProfileDomain
import com.example.kafiesta.domain.DataDomain
import com.example.kafiesta.domain.UserDomain
import com.google.gson.annotations.SerializedName

data class UserBaseNetwork (
    @SerializedName("status")
    val status: String,

    @SerializedName("data")
    val data: Data,

    @SerializedName("message")
    val message: String
)

data class Data(
    @SerializedName("token")
    val token: String,

    @SerializedName("token_type")
    val tokenType: String,

    @SerializedName("expires_in")
    val expiresIn: Long,

    @SerializedName("profile")
    val profile: Profile
)


data class Profile(
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
    val userInformations: Any? = null,
)

fun UserBaseNetwork.asDomainModel(): UserDomain {
    return UserDomain(
        status = status,
        data =  data.asDomainModel(),
        message = message,
    )
}

fun Data.asDomainModel(): DataDomain {
    return DataDomain(
        token = token,
        tokenType = tokenType,
        expiresIn = expiresIn,
        profile = profile.asDomainModel()
    )
}

fun Profile.asDomainModel(): ProfileDomain {
    return ProfileDomain(
        id = id,
        firstName = firstName,
        lastName = lastName,
        email = email,
        status = status,
        role = role,
        userInformations = userInformations,
    )
}