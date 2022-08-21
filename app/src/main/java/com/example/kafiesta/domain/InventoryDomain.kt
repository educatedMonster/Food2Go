package com.example.kafiesta.domain


data class InventoryBaseNetworkDomain(
    val status: String,
    val message: String,
    val result: InventoryBaseDomain,
)

data class InventoryBaseDomain(
    val currentPage: Long,
    val data: List<InventoryDomain>, // no model yet
    val firstPageURL: String,
    val from: Long? = null,
    val lastPage: Long,
    val lastPageURL: String,
    val links: List<InventoryLinkDomain>,
    val nextPageURL: String? = null,
    val path: String,
    val perPage: String,
    val prevPageURL: String? = null,
    val to: String? = null,
    val total: Long,
)

data class InventoryDomain(
    val id: Long,
    val productID: Long,
    val userID: Long,
    val quantity: Long,
    val name: String,
    val description: String,
    val imageURL: String,
    val price: String,
    val tags: String,
    val status: String,
) {
    val quantityString = quantity.toString()
}

data class InventoryLinkDomain(
    val url: String? = null,
    val label: String,
    val active: Boolean,
)