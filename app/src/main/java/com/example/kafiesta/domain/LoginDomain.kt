package com.example.kafiesta.domain

import java.io.Serializable

data class LoginUserDomain(
    val status: String,
    val data: LoginDataDomain,
    val message: String,
)

data class LoginDataDomain(
    val token: String,
    val tokenType: String,
    val expiresIn: Long,
    val profile: LoginProfileDomain,
)

data class LoginProfileDomain(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val email: String,
    val status: String,
    val role: String,
    val userInformations: LoginInformationsDomain,
) : Serializable {
    val fullName = "$firstName $lastName"
}


data class LoginInformationsDomain(
    val id: Long,
    val userID: Long,
    val primaryContact: String,
    val secondaryContact: String,
    val completeAddress: String,
) : Serializable