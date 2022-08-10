package com.example.kafiesta.domain

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
    val name: String,
    val description: String,
    val imageURL: String? = null,
    val price: String,
    val tags: String? = null,
    val status: String,
) {
}


data class LinkDomaintest(
    val url: String? = null,
    val label: String,
    val active: Boolean,
)