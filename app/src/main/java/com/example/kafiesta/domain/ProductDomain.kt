package com.example.kafiesta.domain

import java.io.File

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
    val imageURL: File? = null,
    val price: Double,
    val tags: String,
    val status: String,
){
    val priceString = price.toString()
}


