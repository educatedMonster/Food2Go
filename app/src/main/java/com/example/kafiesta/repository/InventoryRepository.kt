package com.example.kafiesta.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.kafiesta.constants.ServerConst
import com.example.kafiesta.constants.UserConst
import com.example.kafiesta.domain.InventoryBaseDomain
import com.example.kafiesta.domain.ProductInventoryBaseDomain
import com.example.kafiesta.network.AppNetwork
import com.example.kafiesta.network.asDomainModel
import com.example.kafiesta.network.paramsToRequestBody
import com.example.kafiesta.utilities.helpers.SharedPrefs
import com.example.kafiesta.utilities.setBearer
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
                val network = AppNetwork.service.onInventoryAndQuantityAsync(
                    bearer = setBearer(token),
                    productId,
                    quantity).await()

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
}
