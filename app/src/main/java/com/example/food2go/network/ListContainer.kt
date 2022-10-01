package com.example.food2go.network

import com.example.food2go.domain.LinkDomaintest
import com.example.food2go.domain.ListDMaintest
import com.example.food2go.domain.ProductDomaintest
import com.example.food2go.domain.ResultDomaintest
import com.google.gson.annotations.SerializedName

data class ListNetworkTest (
    val status: String,
    val message: String,
    val result: ResultTest
)

data class ResultTest (
    @SerializedName("current_page")
    val currentPage: Long,
    val data: List<ProductTest>,
    @SerializedName("first_page_url")
    val firstPageURL: String,
    val from: Long,
    @SerializedName("last_page")
    val lastPage: Long,
    @SerializedName("last_page_url")
    val lastPageURL: String,
    val links: List<Link>,
    @SerializedName("next_page_url")
    val nextPageURL: String? = null,
    val path: String,
    @SerializedName("per_page")
    val perPage: String,
    @SerializedName("prev_page_url")
    val prevPageURL: String? = null,
    val to: Long,
    val total: Long
)

data class ProductTest (
    @SerializedName("id")
    val id: Long,

    @SerializedName("user_id")
    val userID: Long,

    val name: String?,
    val description: String?,

    @SerializedName("image_url")
    val imageURL: String? = null,

    val price: String,
    val tags: String,
    val status: String
) {
    val statusBool = status.matches("active".toRegex())
    val isIdExist = id != 0L
}

data class Link (
    val url: String? = null,
    val label: String,
    val active: Boolean
)

fun ListNetworkTest.asDomainMode() : ListDMaintest {
    return ListDMaintest(
        status = status,
        message = message,
        result = result.asDomainModel()
    )
}

fun ResultTest.asDomainModel(): ResultDomaintest {
    return ResultDomaintest(
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

fun ProductTest.asDomainModel(): ProductDomaintest {
    return ProductDomaintest(
        id = id,
        userID = userID,
        name = name,
        description = description,
        imageURL = imageURL,
        price = price,
        tags = tags,
        status = status,
    )

}

fun Link.asDomainModel(): LinkDomaintest {
    return LinkDomaintest(
        url = url,
        label = label,
        active = active
    )

}
