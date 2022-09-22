package com.example.food2go.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.food2go.constants.ServerConst
import com.example.food2go.constants.UserConst
import com.example.food2go.domain.InventoryBaseDomain
import com.example.food2go.domain.ProductInventoryBaseDomain
import com.example.food2go.network.AppNetwork
import com.example.food2go.network.asDomainModel
import com.example.food2go.network.paramsToRequestBody
import com.example.food2go.utilities.helpers.SharedPrefs
import com.example.food2go.utilities.setBearer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import timber.log.Timber

class InventoryRepository(private val sharedPrefs: SharedPrefs) {
    private val token = sharedPrefs.getString(UserConst.TOKEN)!!
    private val userid = sharedPrefs.getString(UserConst.USER_ID)!!

    private val _productInventoryList = MutableLiveData<ProductInventoryBaseDomain>()
    val productInventoryList: LiveData<ProductInventoryBaseDomain> get() = _productInventoryList

    private val _inventoryList = MutableLiveData<InventoryBaseDomain>()
    val inventoryList: LiveData<InventoryBaseDomain> get() = _inventoryList

    private val _isAddedInventory = MutableLiveData<Boolean>()
    val isAddedInventory: LiveData<Boolean> get() = _isAddedInventory

    private val _isModifyQuantity = MutableLiveData<Boolean>()
    val isModifyQuantity: LiveData<Boolean> get() = _isModifyQuantity

    private val _isRemoved = MutableLiveData<Boolean>()
    val isRemoved: LiveData<Boolean> get() = _isRemoved

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    suspend fun onGetAllProductInventory(length: Long, start: Long, search: String) {
        withContext(Dispatchers.IO) {
            try {
                _isLoading.postValue(true)
                val params = HashMap<String, Any>()
                params["length"] = length
                params["start"] = start
                params["search"] = search

                val network = AppNetwork.service.onGetAllProductInventoryAsync(
                    bearer = setBearer(token),
                    params = paramsToRequestBody(params))
                    .await()
                if (network.status.matches(ServerConst.IS_SUCCESS.toRegex())) {
                    _productInventoryList.postValue(network.result.asDomainModel())
                    _isLoading.postValue(false)
                }
            } catch (e: HttpException) {
                Timber.e(e.message())
                _isLoading.postValue(false)
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    suspend fun getAllInventory(length: Long, start: Long, search: String) {
        withContext(Dispatchers.IO) {
            try {
                _isLoading.postValue(true)
                val params = HashMap<String, Any>()
                params["length"] = length
                params["start"] = start
                params["search"] = search

                val network = AppNetwork.service.onGetAllInventoryAsync(
                    bearer = setBearer(token),
                    params = paramsToRequestBody(params))
                    .await()
                if (network.status.matches(ServerConst.IS_SUCCESS.toRegex())) {
                    _inventoryList.postValue(network.result.asDomainModel())
                    _isLoading.postValue(false)
                }
            } catch (e: HttpException) {
                Timber.e(e.message())
                _isLoading.postValue(false)
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    suspend fun onModifyQuantity(quantity: String, productId: Long) {
        withContext(Dispatchers.IO) {
            try {
                _isLoading.postValue(true)
                val params = HashMap<String, Any>()
                params["product_id"] = productId
                params["quantity"] = quantity

                val network = AppNetwork.service.onModifyQuantityAsync(
                    bearer = setBearer(token),
                    params = paramsToRequestBody(params)
                )
                    .await()
                _isModifyQuantity.postValue(false)
                _isLoading.postValue(false)
            } catch (e: HttpException) {
                Timber.e(e.message())
                _isLoading.postValue(false)
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    suspend fun onInventoryAndQuantity(productId: Long, quantity: String) {
        withContext(Dispatchers.IO) {
            try {
                _isLoading.postValue(true)
                val params = HashMap<String, Any>()
                params["product_id"] = productId
                params["quantity"] = quantity

                val network = AppNetwork.service.onInventoryAndQuantityAsync(
                    bearer = setBearer(token),
                    params = paramsToRequestBody(params)
                ).await()

                // notify add inventory
                _isAddedInventory.postValue(true)
                _isLoading.postValue(false)
            } catch (e: HttpException) {
                Timber.e(e.message())
                _isLoading.postValue(false)
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    suspend fun onRemoveInventory(productId: Long) {
        withContext(Dispatchers.IO) {
            try {
                _isLoading.postValue(true)
                val network = AppNetwork.service.onRemoveInventoryAsync(
                    bearer = setBearer(token),
                    productId = productId).await()
                if (network.status.matches(ServerConst.IS_SUCCESS.toRegex())) {
                    _isLoading.postValue(false)
                    _isRemoved.postValue(true)
                }
            } catch (e: HttpException) {
                Timber.e(e.message())
                _isLoading.postValue(false)
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
}
