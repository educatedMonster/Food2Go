package com.example.kafiesta.domain

import com.example.kafiesta.constants.OrderConst

data class WeeklyPaymentBaseNetworkDomain(
    val status: String,
    val message: String,
    val result: List<WeeklyPaymentDomain>,
)

data class WeeklyPaymentDomain(
    val id: Long,
    val amount: String,
    val dateFrom: String,
    val dateTo: String,
    val status: String,
    val merchantName: String,
    val merchantID: Long,
    val adminID: String? = null,
    val merchantAgreedAt: String? = null,
    val adminAgreedAt: String? = null,
    val proofURL: String? = null,
    val createdAt: String,
    val updatedAt: String,
    val deletedAt: String? = null,
) {
    val weekBalance = "$dateFrom to $dateTo"
    val isPending: Boolean = status.matches("pending".toRegex())
    val isCompleted: Boolean = status.matches("completed".toRegex())
    val paymentId: String = id.toString()
    val isNotPending = !isPending

//    pag ka click ng settle, yung status of dapat is waiting for admin to verified
//    if status is 'settled' display waiting for admin to verify
//    if status is 'completed' display settled

    val paymentStatus: String = if(status.matches("completed".toRegex())) "Settled" else if(status.matches("settled".toRegex())) "Waiting for admin to verify" else  "Pending"


}

