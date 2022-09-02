package com.example.kafiesta.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.kafiesta.constants.OrderConst.ORDER_COMPLETED
import com.example.kafiesta.constants.OrderConst.ORDER_DELIVERY
import com.example.kafiesta.constants.OrderConst.ORDER_PENDING
import com.example.kafiesta.constants.OrderConst.ORDER_PREPARING
import com.example.kafiesta.constants.UserConst
import com.example.kafiesta.domain.OrderListBaseDomain
import com.example.kafiesta.domain.SpecificBaseOrderDomain
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

    private val _orderPendingList = MutableLiveData<List<OrderListBaseDomain>>()
    val orderListPendingList: LiveData<List<OrderListBaseDomain>> get() = _orderPendingList

    private val _orderPreparingList = MutableLiveData<List<OrderListBaseDomain>>()
    val orderListPreparingList: LiveData<List<OrderListBaseDomain>> get() = _orderPreparingList

    private val _orderDeliveryList = MutableLiveData<List<OrderListBaseDomain>>()
    val orderListDeliveryList: LiveData<List<OrderListBaseDomain>> get() = _orderDeliveryList

    private val _orderCompletedList = MutableLiveData<List<OrderListBaseDomain>>()
    val orderListCompletedList: LiveData<List<OrderListBaseDomain>> get() = _orderCompletedList

    private val _specificOrder = MutableLiveData<SpecificBaseOrderDomain>()
    val specificOrder: LiveData<SpecificBaseOrderDomain> get() = _specificOrder

    private val _orderStatus = MutableLiveData<String>()
    val orderStatus: LiveData<String> get() = _orderStatus

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
                    OrderStatusEnum.PENDING -> ORDER_PENDING
                    OrderStatusEnum.PREPARING -> ORDER_PREPARING
                    OrderStatusEnum.DELIVERY -> ORDER_DELIVERY
                    OrderStatusEnum.COMPLETED -> ORDER_COMPLETED
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

    suspend fun onOrderMoveStatus(
        order_id: Long,
        status: String,
        remarks: String?,
    ) {
        withContext(Dispatchers.IO) {
            try {
                _isLoading.postValue(true)
                val params = HashMap<String, Any>()
                params["order_id"] = order_id
                params["status"] = status
                params["remarks"] = remarks!!

                val network = AppNetwork.service.onOrderMoveStatusAsync(
                    bearer = setBearer(token),
                    params = paramsToRequestBody(params))
                    .await()
                _orderStatus.postValue(network.message)
                _isLoading.postValue(false)
            } catch (e: HttpException) {
                Timber.e(e.message())
                _isLoading.postValue(false)
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    suspend fun onGetSpecificOrderId(order_id: Long) {
        withContext(Dispatchers.IO) {
            try {
                _isLoading.postValue(true)

                val network = AppNetwork.service.onGetOrderIdAsync(
                    bearer = setBearer(token),
                    orderId = order_id)
                    .await()
                _specificOrder.postValue(network.result.asDomainModel())
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