package com.example.kafiesta.domain

data class UserResultDomain(
    val status: String,
    val message: String,
    val result: ResultDomain,
)

data class ResultDomain(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val fullName: String,
    val email: String,
    val rememberToken: Any? = null,
    val status: String,
    val role: String,
    val deletedAt: Any? = null,
    val createdAt: Any? = null,
    val updatedAt: Any? = null,
    val userInformations: Any? = null,
    val userShop: Any? = null
)