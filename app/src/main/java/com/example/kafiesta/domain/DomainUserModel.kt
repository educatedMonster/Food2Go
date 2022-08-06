package com.example.kafiesta.domain

data class UserDomain(
    val status: String,
    val data: DataDomain,
    val message: String,
)

data class DataDomain(
    val token: String,
    val tokenType: String,
    val expiresIn: Long,
    val profile: ProfileDomain
)

data class ProfileDomain(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val email: String,
    val status: String,
    val role: String,
    val userInformations: Any? = null
)