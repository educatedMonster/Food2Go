package com.example.kafiesta.screens.main.fragment.order

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.kafiesta.repository.OrderRepository
import com.example.kafiesta.utilities.helpers.SharedPrefs
import com.example.kafiesta.utilities.helpers.getSecurePrefs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException

class OrderViewModel(application: Application) : AndroidViewModel(application) {
    // TODO: Implement the ViewModel
    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    private val repository = OrderRepository(SharedPrefs(getSecurePrefs(application)))

    val certainOrderFormState = repository.certainOrderFormState
    val orderList = repository.orderList
    val orderPendingList = repository.orderListPending
    val orderPreparingList = repository.orderListPreparing
    val orderDeliveryList = repository.orderListDelivery
    val orderCompletedList = repository.orderListCompleted
    val specificOrder = repository.specificOrder
    val orderStatus = repository.orderStatus
    val isLoading = repository.isLoading

    fun getAllOrderList(
        orderStatusEnum: OrderStatusEnum,
        search: String,
        merchant_user_id: Long,
        date_from: String,
        date_to: String,
    ) {
        viewModelScope.launch {
            try {
                repository.getAllOrderList(
                    orderStatusEnum,
                    search,
                    merchant_user_id,
                    date_from,
                    date_to)
            } catch (network: IOException) {
                Timber.d(network)
            }
        }
    }

    fun orderMoveStatus(
        order_id: Long,
        status: String,
        remarks: String?,
    ) {
        viewModelScope.launch {
            try {
                repository.onOrderMoveStatus(
                    order_id,
                    status,
                    remarks)
            } catch (network: IOException) {
                Timber.d(network)
            }
        }
    }

    fun getSpecificOrderId(order_id: Long) {
        viewModelScope.launch {
            try {
                repository.onGetSpecificOrderId(
                    order_id)
            } catch (network: IOException) {
                Timber.d(network)
            }
        }
    }

    fun fetchCertainOrderStatus(
        orderPosition: Int,
        orderTitle: String,
        orderStatusEnum: OrderStatusEnum,
        search: String,
        merchant_user_id: Long,
        date_from: String,
        date_to: String){
        viewModelScope.launch {
            try {
                repository.getCertainOrderStatus(
                    orderPosition,
                    orderTitle,
                    orderStatusEnum,
                    search,
                    merchant_user_id,
                    date_from,
                    date_to)
            }catch (network: IOException){
                Timber.d(network)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}