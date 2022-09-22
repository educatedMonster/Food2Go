package com.example.food2go.domain

data class ProductInventoryBaseNetworkDomain(
    val status: String,
    val message: String,
    val result: ProductInventoryBaseDomain,
)

data class ProductInventoryBaseDomain(
    val currentPage: Long,
    val data: List<ProductInventoryDomain>,
    val firstPageURL: String,
    val from: Long,
    val lastPage: Long,
    val lastPageURL: String,
    val links: List<LinkProductInventoryDomain>,
    val nextPageURL: String? = null,
    val path: String,
    val perPage: String,
    val prevPageURL: String? = null,
    val to: Long,
    val total: Long,
) {
    val isNotEmptyData = data.isNotEmpty()
}

    data class ProductInventoryDomain(
    val id: Long,
    val userID: Long,
    val name: String,
    val description: String,
    val imageURL: String,
    val price: String,
    val tags: String,
    val status: String,
)

data class LinkProductInventoryDomain(
    val url: String? = null,
    val label: String,
    val active: Boolean,
)
