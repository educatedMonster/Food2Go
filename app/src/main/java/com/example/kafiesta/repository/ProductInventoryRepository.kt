package com.example.kafiesta.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.kafiesta.constants.ServerConst
import com.example.kafiesta.constants.UserConst
import com.example.kafiesta.domain.ProductInventoryBaseNetworkDomain
import com.example.kafiesta.domain.asDomainMode
import com.example.kafiesta.domain.asDomainModel
import com.example.kafiesta.network.AppNetwork
import com.example.kafiesta.network.asDomainModel
import com.example.kafiesta.network.paramsToRequestBody
import com.example.kafiesta.utilities.helpers.SharedPrefs
import com.example.kafiesta.utilities.setBearer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import timber.log.Timber

class ProductInventoryRepository(private val sharedPrefs: SharedPrefs) {
    private val token = sharedPrefs.getString(UserConst.TOKEN)!!
    private val userid = sharedPrefs.getString(UserConst.USER_ID)!!


    private val _productInventoryList = MutableLiveData<ProductInventoryBaseNetworkDomain>()
    val productInventoryList: LiveData<ProductInventoryBaseNetworkDomain> get() = _productInventoryList

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
                    _productInventoryList.postValue(network.asDomainModel())
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

    suspend fun onModifyQuantity(quantity: Long, productId: Long) {
        withContext(Dispatchers.IO) {
            try {
                _isLoading.postValue(true)
                val params = HashMap<String, Any>()
                params["id"] = productId
                params["quantity"] = quantity

                val network = AppNetwork.service.onModifyQuantityAsync(
                    bearer = setBearer(token),
                    params = paramsToRequestBody(params)
                )
                    .await()
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
