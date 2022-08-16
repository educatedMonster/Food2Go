package com.example.kafiesta.network

import com.example.kafiesta.domain.InventoryBaseDomain
import com.example.kafiesta.domain.InventoryBaseNetworkDomain
import com.example.kafiesta.domain.InventoryDomain
import com.example.kafiesta.domain.InventoryLinkDomain
import com.google.gson.annotations.SerializedName


data class InventoryBaseNetwork(
    val status: String,
    val message: String,
    val result: InventoryBaseResponse,
)


data class InventoryBaseResponse(
    @SerializedName("current_page")
    val currentPage: Long,

    val data: List<InventorResponse>, // no model yet

    @SerializedName("first_page_url")
    val firstPageURL: String,

    val from: String? = null,

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

    val to: String? = null,
    val total: Long,
)

data class InventorResponse(
    val id: Long,
    val userID: Long,
    val name: String,
    val description: String,
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


fun InventorResponse.asDomainModel(): InventoryDomain {
    return InventoryDomain(
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

fun InventoryLinkResponse.asDomainModel(): InventoryLinkDomain {
    return InventoryLinkDomain(
        url = url,
        label = label,
        active = active
    )
}










