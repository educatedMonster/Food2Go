package com.example.food2go.domain

//create product

data class ProductBaseDomain(
    val status: String,
    val message: String,
    val product: ProductDomain,
)

data class ProductDomain(
    val id: Long,
    val userID: Long,
    val name: String,
    val description: String,
    val imageURL: String? = "",
    val price: String,
    val tags: String,
    val status: String
){
    val priceString = price.toString()
    val statusBool = status.matches("active".toRegex())
    val isIdExist = id != 0L
}

