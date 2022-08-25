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

    val orderPendingList = repository.orderPendingList
    val orderPreparingList = repository.orderPreparingList
    val orderDeliveryList = repository.orderDeliveryList
    val orderCompletedList = repository.orderCompletedList
    val isLoading = repository.isLoading

    fun getAllOrderList(
        orderStatusEnum: OrderStatusEnum,
//        length: Long,
//        start: Long,
        search: String,
        merchant_user_id: Long,
        date_from: String,
        date_to: String,
    ) {
        viewModelScope.launch {
            try {
                repository.getAllOrderList(
                    orderStatusEnum,
//                    length,
//                    start,
                    search,
                    merchant_user_id,
                    date_from,
                    date_to)
            } catch (network: IOException) {
                Timber.d(network)
            }
        }
    }
}