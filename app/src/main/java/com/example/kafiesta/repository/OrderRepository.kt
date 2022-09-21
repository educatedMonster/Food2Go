package com.example.kafiesta.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.kafiesta.constants.OrderConst.ORDER_ALL
import com.example.kafiesta.constants.OrderConst.ORDER_COMPLETED
import com.example.kafiesta.constants.OrderConst.ORDER_DELIVERY
import com.example.kafiesta.constants.OrderConst.ORDER_PENDING
import com.example.kafiesta.constants.OrderConst.ORDER_PREPARING
import com.example.kafiesta.constants.PusherConst.PUSHER_MY_CHANNEL
import com.example.kafiesta.constants.PusherConst.PUSHER_ORDER_PIPELINE_EVENT
import com.example.kafiesta.constants.UserConst
import com.example.kafiesta.domain.OrderBaseDomain
import com.example.kafiesta.network.AppNetwork
import com.example.kafiesta.network.asDomainModel
import com.example.kafiesta.network.paramsToRequestBody
import com.example.kafiesta.screens.main.fragment.order.OrderStatusEnum
import com.example.kafiesta.screens.test.test_order.OrderFormState
import com.example.kafiesta.utilities.helpers.SharedPrefs
import com.example.kafiesta.utilities.setBearer
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import timber.log.Timber

class OrderRepository(
    private val sharedPrefs: SharedPrefs,
) {
    private val token = sharedPrefs.getString(UserConst.TOKEN)!!
    private val userid = sharedPrefs.getString(UserConst.USER_ID)!!

    private val _certainOrderFormState = MutableLiveData<OrderFormState>()
    val certainOrderFormState: LiveData<OrderFormState> get() = _certainOrderFormState

    private val _orderList = MutableLiveData<List<OrderBaseDomain>>()
    val orderList: LiveData<List<OrderBaseDomain>> get() = _orderList

    private val _orderPendingList = MutableLiveData<List<OrderBaseDomain>>()
    val orderListPending: LiveData<List<OrderBaseDomain>> get() = _orderPendingList

    private val _orderPreparingList = MutableLiveData<List<OrderBaseDomain>>()
    val orderListPreparing: LiveData<List<OrderBaseDomain>> get() = _orderPreparingList

    private val _orderDeliveryList = MutableLiveData<List<OrderBaseDomain>>()
    val orderListDelivery: LiveData<List<OrderBaseDomain>> get() = _orderDeliveryList

    private val _orderCompletedList = MutableLiveData<List<OrderBaseDomain>>()
    val orderListCompleted: LiveData<List<OrderBaseDomain>> get() = _orderCompletedList

    private val _specificOrder = MutableLiveData<OrderBaseDomain>()
    val specificOrder: LiveData<OrderBaseDomain> get() = _specificOrder

    private val _orderStatus = MutableLiveData<String>()
    val orderStatus: LiveData<String> get() = _orderStatus

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    suspend fun getAllOrderList(
        orderStatusEnum: OrderStatusEnum,
        search: String,
        merchant_user_id: Long,
        date_from: String,
        date_to: String,
    ) {
        withContext(Dispatchers.IO) {
            try {
                _isLoading.postValue(true)

                val status = when (orderStatusEnum) {
                    OrderStatusEnum.ALL -> ORDER_ALL
                    OrderStatusEnum.PENDING -> ORDER_PENDING
                    OrderStatusEnum.PREPARING -> ORDER_PREPARING
                    OrderStatusEnum.DELIVERY -> ORDER_DELIVERY
                    OrderStatusEnum.COMPLETED -> ORDER_COMPLETED
                }

                val params = HashMap<String, Any>()
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
                    OrderStatusEnum.ALL -> _orderList.postValue(network.result!!.map { it.asDomainModel() })
                    OrderStatusEnum.PENDING -> _orderPendingList.postValue(network.result!!.map { it.asDomainModel() })
                    OrderStatusEnum.PREPARING -> _orderPreparingList.postValue(network.result!!.map { it.asDomainModel() })
                    OrderStatusEnum.DELIVERY -> _orderDeliveryList.postValue(network.result!!.map { it.asDomainModel() })
                    OrderStatusEnum.COMPLETED -> _orderCompletedList.postValue(network.result!!.map { it.asDomainModel() })
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
        customerId: Long,
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
                _orderStatus.postValue(network.status)

                val params2 = HashMap<String, Any>()
                val data: String?
                var message: String? = null
                when {
                    status.matches(ORDER_PREPARING.toRegex()) -> {
                        message = "Order is now preparing"
                    }
                    status.matches(ORDER_DELIVERY.toRegex()) -> {
                        message = "Your rider is on the way"
                    }
                    status.matches(ORDER_COMPLETED.toRegex()) -> {
                        message = "Order completed."
                    }
                }
                data = orderMoveResponse(order_id, customerId, message!!)
                params2["data"] = data
                params2["event_name"] = PUSHER_ORDER_PIPELINE_EVENT
                params2["channel_name"] = PUSHER_MY_CHANNEL
                val network2 = AppNetwork.service.onTriggerPusherAsync(
                    bearer = setBearer(token),
                    params = paramsToRequestBody(params2)
                ).await()
                Timber.d(Gson().toJson(network2))
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

    suspend fun getCertainOrderStatus(
        orderPosition: Int,
        orderTitle: String,
        orderStatusEnum: OrderStatusEnum,
        search: String,
        merchant_user_id: Long,
        date_from: String,
        date_to: String,
    ) {
        withContext(Dispatchers.IO) {
            _certainOrderFormState.postValue(
                OrderFormState(
                    orderPosition = orderPosition,
                    orderTitle = orderTitle,
                    isLoading = true,
                    certainOrderStatus = orderTitle
                )
            )

            try {
                val status = when (orderStatusEnum) {
                    OrderStatusEnum.ALL -> ORDER_ALL
                    OrderStatusEnum.PENDING -> ORDER_PENDING
                    OrderStatusEnum.PREPARING -> ORDER_PREPARING
                    OrderStatusEnum.DELIVERY -> ORDER_DELIVERY
                    OrderStatusEnum.COMPLETED -> ORDER_COMPLETED
                }

                val params = HashMap<String, Any>()
                params["search"] = search
                params["status"] = status
                params["merchant_user_id"] = merchant_user_id
                params["date_from"] = date_from
                params["date_to"] = date_to

                val network = AppNetwork.service.onGetAllOrdersAsync(
                    bearer = setBearer(token),
                    params = paramsToRequestBody(params))
                    .await()

                val list = when (orderStatusEnum) {
                    OrderStatusEnum.ALL -> network.result!!.map { it.asDomainModel() } as ArrayList<OrderBaseDomain>
                    OrderStatusEnum.PENDING -> network.result!!.map { it.asDomainModel() } as ArrayList<OrderBaseDomain>
                    OrderStatusEnum.PREPARING -> network.result!!.map { it.asDomainModel() } as ArrayList<OrderBaseDomain>
                    OrderStatusEnum.DELIVERY -> network.result!!.map { it.asDomainModel() } as ArrayList<OrderBaseDomain>
                    OrderStatusEnum.COMPLETED -> network.result!!.map { it.asDomainModel() } as ArrayList<OrderBaseDomain>
                }

                _certainOrderFormState.postValue(
                    OrderFormState(
                        orderPosition = orderPosition,
                        orderTitle = orderTitle,
                        certainOrderStatus = status,
                        isLoading = false,
                        list = list
                    )
                )
                _isLoading.postValue(false)
            } catch (e: HttpException) {
                Timber.e(e.message())
                _isLoading.postValue(false)
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    private fun orderMoveResponse(
        order_id: Long,
        user_id: Long,
        message: String,
    ): String {
        return "{\"order_id\": \"$order_id\",\"user_id\": \"$user_id\",\"message\":\"$message\"}"
    }

}