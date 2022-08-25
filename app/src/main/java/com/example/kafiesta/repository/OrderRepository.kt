package com.example.kafiesta.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.kafiesta.constants.UserConst
import com.example.kafiesta.domain.OrderBaseDomain
import com.example.kafiesta.network.AppNetwork
import com.example.kafiesta.network.asDomainModel
import com.example.kafiesta.network.paramsToRequestBody
import com.example.kafiesta.screens.main.fragment.order.OrderStatusEnum
import com.example.kafiesta.utilities.helpers.SharedPrefs
import com.example.kafiesta.utilities.setBearer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import timber.log.Timber

class OrderRepository(
    private val sharedPrefs: SharedPrefs,
) {
    private val token = sharedPrefs.getString(UserConst.TOKEN)!!
    private val userid = sharedPrefs.getString(UserConst.USER_ID)!!

    private val _orderPendingList = MutableLiveData<List<OrderBaseDomain>>()
    val orderPendingList: LiveData<List<OrderBaseDomain>> get() = _orderPendingList

    private val _orderPreparingList = MutableLiveData<List<OrderBaseDomain>>()
    val orderPreparingList: LiveData<List<OrderBaseDomain>> get() = _orderPreparingList

    private val _orderDeliveryList = MutableLiveData<List<OrderBaseDomain>>()
    val orderDeliveryList: LiveData<List<OrderBaseDomain>> get() = _orderDeliveryList

    private val _orderCompletedList = MutableLiveData<List<OrderBaseDomain>>()
    val orderCompletedList: LiveData<List<OrderBaseDomain>> get() = _orderCompletedList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    suspend fun getAllOrderList(
        orderStatusEnum: OrderStatusEnum,
//        length: Long,
//        start: Long,
        search: String,
        merchant_user_id: Long,
        date_from: String,
        date_to: String,
    ) {
        withContext(Dispatchers.IO) {
            try {
                _isLoading.postValue(true)

                val status = when (orderStatusEnum) {
                    OrderStatusEnum.PENDING -> "pending"
                    OrderStatusEnum.PREPARING -> "preparing"
                    OrderStatusEnum.DELIVERY -> "outfordelivery"
                    OrderStatusEnum.COMPLETED -> "completed"
                }

                val params = HashMap<String, Any>()
//                params["length"] = length
//                params["start"] = start
                params["search"] = search
                params["status"] = status
                params["merchant_user_id"] = merchant_user_id
                params["date_from"] = date_from
                params["date_to"] = date_to

                val network = AppNetwork.service.onGetAllOrdersAsync(
                    bearer = setBearer(token),
                    params = paramsToRequestBody(params))
                    .await()

                when (orderStatusEnum) {
                    OrderStatusEnum.PENDING -> _orderPendingList.postValue(network.result?.map { it.asDomainModel() }
                        ?: emptyList())
                    OrderStatusEnum.PREPARING -> _orderPreparingList.postValue(network.result?.map { it.asDomainModel() }
                        ?: emptyList())
                    OrderStatusEnum.DELIVERY -> _orderDeliveryList.postValue(network.result?.map { it.asDomainModel() }
                        ?: emptyList())
                    OrderStatusEnum.COMPLETED -> _orderCompletedList.postValue(network.result?.map { it.asDomainModel() }
                        ?: emptyList())
                }
                _isLoading.postValue(false)

            } catch (e: HttpException) {
                Timber.e(e.message())
                _isLoading.postValue(false)
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
}