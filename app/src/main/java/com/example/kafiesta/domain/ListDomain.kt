package com.example.kafiesta.domain

import java.io.File

data class ListDMaintest(
    val status: String,
    val message: String,
    val result: ResultDomaintest,
)


data class ResultDomaintest(
    val currentPage: Long,
    val data: List<ProductDomaintest>,
    val firstPageURL: String,
    val from: Long,
    val lastPage: Long,
    val lastPageURL: String,
    val links: List<LinkDomaintest>,
    val nextPageURL: String? = null,
    val path: String,
    val perPage: String,
    val prevPageURL: String? = null,
    val to: Long,
    val total: Long,
)


data class ProductDomaintest(
    val id: Long,
    val userID: Long,
    val name: String?,
    val description: String?,
    val imageURL: String? = null,
    val price: Double?,
    val tags: String?,
    val status: String?,
) {
    val priceString = price.toString()
    val statusBool = status!!.matches("active".toRegex())
    val isIdExist = id != 0L
}


data class LinkDomaintest(
    val url: String? = null,
    val label: String,
    val active: Boolean,
)

data class TestUnit(
    val prod : ProductDomain,
    val file : File
)
{
    val priceString = prod.price.toString()
    val statusBool = prod.status!!.matches("active".toRegex())
    val isIdExist = prod.id != 0L
}