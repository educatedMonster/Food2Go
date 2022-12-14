package com.example.food2go.network

import com.example.food2go.domain.InventoryBaseDomain
import com.example.food2go.domain.InventoryBaseNetworkDomain
import com.example.food2go.domain.InventoryDomain
import com.example.food2go.domain.InventoryLinkDomain
import com.google.gson.annotations.SerializedName

data class InventoryBaseNetwork(
    val status: String,
    val message: String,
    val result: InventoryBaseResponse,
)

data class InventoryBaseResponse(
    @SerializedName("current_page")
    val currentPage: Long,

    val data: List<InventoryResponse>,

    @SerializedName("first_page_url")
    val firstPageURL: String,

    val from: Long,

    @SerializedName("last_page")
    val lastPage: Long,

    @SerializedName("last_page_url")
    val lastPageURL: String,

    val links: List<InventoryLinkResponse>,

    @SerializedName("next_page_url")
    val nextPageURL: String? = null,

    val path: String,

    @SerializedName("per_page")
    val perPage: String,

    @SerializedName("prev_page_url")
    val prevPageURL: String? = null,

    val to: String,
    val total: Long,
)


data class InventoryResponse(
    val id: Long,

    @SerializedName("product_id")
    val productID: Long,

    @SerializedName("user_id")
    val userID: Long,

    val quantity: Long,
    val name: String,
    val description: String,

    @SerializedName("image_url")
    val imageURL: String,

    val price: String,
    val tags: String,
    val status: String,
)

data class InventoryLinkResponse(
    val url: String? = null,
    val label: String,
    val active: Boolean,
)

fun InventoryBaseNetwork.asDomainModel(): InventoryBaseNetworkDomain {
    return InventoryBaseNetworkDomain(
        status = status,
        message = message,
        result = result.asDomainModel()
    )
}

fun InventoryBaseResponse.asDomainModel(): InventoryBaseDomain {
    return InventoryBaseDomain(
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
        total = total,
    )
}


fun InventoryResponse.asDomainModel(): InventoryDomain {
    return InventoryDomain(
        id = id,
        productID = productID,
        userID = userID,
        name = name,
        description = description,
        imageURL = imageURL,
        price = price,
        tags = tags,
        status = status,
        quantity = quantity
    )
}

fun InventoryLinkResponse.asDomainModel(): InventoryLinkDomain {
    return InventoryLinkDomain(
        url = url,
        label = label,
        active = active
    )
}










