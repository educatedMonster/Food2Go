package com.example.kafiesta.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.kafiesta.constants.ServerConst.IS_SUCCESS
import com.example.kafiesta.constants.UserConst
import com.example.kafiesta.domain.*
import com.example.kafiesta.network.AppNetwork
import com.example.kafiesta.network.asDomainModel
import com.example.kafiesta.network.paramsToRequestBody
import com.example.kafiesta.utilities.helpers.SharedPrefs
import com.example.kafiesta.utilities.setBearer
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import timber.log.Timber
import java.io.File

class ProductRepository(private val sharedPrefs: SharedPrefs) {

    private val _productList = MutableLiveData<ResultDomaintest>()
    val productList: LiveData<ResultDomaintest> get() = _productList

    private val _isProductCreated = MutableLiveData<ProductDomain>()
    val isProductCreated: LiveData<ProductDomain> get() = _isProductCreated

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _isUploaded = MutableLiveData<Boolean>()
    val isUploaded: LiveData<Boolean> get() = _isUploaded

    suspend fun onGetAllProducts(length: Long, start: Long, search: String) {
        withContext(Dispatchers.IO) {
            try {
                _isLoading.postValue(true)
                val token = sharedPrefs.getString(UserConst.TOKEN)!!

                val params = HashMap<String, Any>()
                params["length"] = length
                params["start"] = start
                params["search"] = search
                val network = AppNetwork.service.onAddProductListAsync(
                    bearer = setBearer(token),
                    params = paramsToRequestBody(params))
                    .await()
//                Timber.d(Gson().toJson(network))
                if (network.status.matches(IS_SUCCESS.toRegex())) {
                    _productList.postValue(network.result.asDomainModel())
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

    suspend fun onAddProduct(productModel: ProductDomain, selectedFile: File) {
        withContext(Dispatchers.IO) {
            try {
                _isLoading.postValue(true)
                val token = sharedPrefs.getString(UserConst.TOKEN)!!
                val userid = sharedPrefs.getString(UserConst.USER_ID)!!
                val params = HashMap<String, Any>()

                params["user_id"] = userid
                params["name"] = productModel.name
                params["description"] = productModel.description
                params["image_url"] = productModel.imageURL!!
                params["price"] = productModel.price
                params["status"] = productModel.status
                params["tags"] = productModel.tags

                val network = AppNetwork.service.onAddProductAsync(
                    bearer = setBearer(token),
                    params = paramsToRequestBody(params))
                    .await()

                if (network.message.matches((IS_SUCCESS.toRegex()))) {
                    val productDomain = network.result.asDomainModel()
                    _isProductCreated.postValue(productDomain)

                     // when the Product Form successfully created , get the id and pass to the file image
                    onUploadProductImage(productDomain.id , selectedFile)
                }


            } catch (e: HttpException) {
                Timber.e(e.message())
                _isLoading.postValue(false)
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    suspend fun onUploadProductImage(productId: Long, file: File) {
        withContext(Dispatchers.IO) {
            try {
                _isLoading.postValue(true)
                val token = sharedPrefs.getString(UserConst.TOKEN)!!
                val userid = sharedPrefs.getString(UserConst.USER_ID)!!
                val params = HashMap<String, Any>()

                params["product_id"] = productId
                params["file"] = file

                val network = AppNetwork.service.onAddProductImageAsync(
                    bearer = setBearer(token),
                    productId,
                    params = paramsToRequestBody(params)
                ).await()

                val a = network
                Timber.d(Gson().toJson(network))
                _isUploaded.postValue(false)
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