package com.example.food2go.domain

data class LoginBaseDomain(
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
    val userId: Long,
    val firstName: String,
    val lastName: String,
    val email: String,
    val status: String,
    val role: String,
    val userInformations: LoginInformationsDomain? = null,
) {
    val fullName = "$firstName $lastName"
}


data class LoginInformationsDomain(
    val infoId: Long,
    val userID: Long,
    val primaryContact: String,
    val secondaryContact: String,
    val completeAddress: String,
)