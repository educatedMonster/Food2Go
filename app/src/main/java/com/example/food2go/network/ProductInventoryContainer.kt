package com.example.food2go.network

import com.example.food2go.domain.LinkProductInventoryDomain
import com.example.food2go.domain.ProductInventoryBaseDomain
import com.example.food2go.domain.ProductInventoryBaseNetworkDomain
import com.example.food2go.domain.ProductInventoryDomain
import com.google.gson.annotations.SerializedName


//https://kafiesta-api.osc-fr1.scalingo.io/v1/product/getProductsForInventory

data class ProductInventoryBaseNetwork(
    val status: String,
    val message: String,
    val result: ProductInventoryBaseResponse,
)


data class ProductInventoryBaseResponse(
    @SerializedName("current_page")
    val currentPage: Long,

    val data: List<ProductInventoryResponse>,

    @SerializedName("first_page_url")
    val firstPageURL: String,

    val from: Long,

    @SerializedName("last_page")
    val lastPage: Long,

    @SerializedName("last_page_url")
    val lastPageURL: String,

    val links: List<ProductLinkInventoryResponse>,

    @SerializedName("next_page_url")
    val nextPageURL: String? = null,

    val path: String,

    @SerializedName("per_page")
    val perPage: String,

    @SerializedName("prev_page_url")
    val prevPageURL: String? = null,

    val to: Long,
    val total: Long,
)


data class ProductInventoryResponse(
    val id: Long,

    @SerializedName("user_id")
    val userID: Long,

    val name: String,
    val description: String,

    @SerializedName("image_url")
    val imageURL: String,

    val price: String,
    val tags: String,
    val status: String,
)

data class ProductLinkInventoryResponse(
    val url: String? = null,
    val label: String,
    val active: Boolean,
)

fun ProductInventoryBaseNetwork.asDomainModel(): ProductInventoryBaseNetworkDomain {
    return ProductInventoryBaseNetworkDomain(
        status = status,
        message = message,
        result = result.asDomainModel()
    )
}

fun ProductInventoryBaseResponse.asDomainModel(): ProductInventoryBaseDomain {
    return ProductInventoryBaseDomain(
        currentPage = currentPage,
        data = data.map { it.asDomainModel() },
        firstPageURL = firstPageURL,
        from = from,
        lastPage = lastPage,
        lastPageURL = lastPageURL,
        links = links.map { it.asDomainModel() },
        nextPageURL = nextPageURL,
        path = path,
        perPage = perPage,
        prevPageURL = prevPageURL,
        to = to,
        total = total
    )
}

fun ProductInventoryResponse.asDomainModel(): ProductInventoryDomain {
    return ProductInventoryDomain(
        id = id,
        userID = userID,
        name = name,
        description = description,
        imageURL = imageURL,
        price = price,
        tags = tags,
        status = status
    )
}

fun ProductLinkInventoryResponse.asDomainModel(): LinkProductInventoryDomain {
    return LinkProductInventoryDomain(
        url = url,
        label = label,
        active = active
    )
}
















