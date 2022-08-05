package com.example.kafiesta.domain

import com.google.gson.JsonObject

data class UserDomain(
    val token: String,
    val tokenType: String,
    val expiresIn: Long,
    val profile: ProfileDomain,
)

data class ProfileDomain(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val email: String,
    val status: String,
    val role: String,
    val userInformations: Any? = null,
)