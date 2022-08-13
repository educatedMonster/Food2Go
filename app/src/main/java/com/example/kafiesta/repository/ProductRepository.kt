package com.example.kafiesta.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.kafiesta.constants.ServerConst.IS_SUCCESS
import com.example.kafiesta.constants.UserConst
import com.example.kafiesta.domain.ProductDomain
import com.example.kafiesta.domain.ProductDomaintest
import com.example.kafiesta.domain.ResultDomaintest
import com.example.kafiesta.domain.asDomainModel
import com.example.kafiesta.network.AppNetwork
import com.example.kafiesta.network.asDomainModel
import com.example.kafiesta.network.paramsToRequestBody
import com.example.kafiesta.utilities.helpers.SharedPrefs
import com.example.kafiesta.utilities.setBearer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import timber.log.Timber
import java.io.File

class ProductRepository(private val sharedPrefs: SharedPrefs) {

    private val _productList = MutableLiveData<ResultDomaintest>()
    val productList: LiveData<ResultDomaintest> get() = _productList

    private val _isProductCreated = MutableLiveData<ProductDomain>()
    val isProductCreated: LiveData<ProductDomain> get() = _isProductCreated

    private val _isUpdated = MutableLiveData<Boolean>()
    val isUpdated: LiveData<Boolean> get() = _isUpdated

    private val _isDeleted = MutableLiveData<Boolean>()
    val isDeleted: LiveData<Boolean> get() = _isDeleted

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

    suspend fun onDeleteProductAsync(productId: Long) {
        withContext(Dispatchers.IO) {
            try {
                _isLoading.postValue(true)
                val token = sharedPrefs.getString(UserConst.TOKEN)!!
                val network = AppNetwork.service.onDeleteProductAsync(
                    bearer = setBearer(token), productId).await()
                if (network.status.matches(IS_SUCCESS.toRegex())) {
                    _isLoading.postValue(false)
                    _isDeleted.postValue(true)
                }
            } catch (e: HttpException) {
                Timber.e(e.message())
                _isLoading.postValue(false)
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    suspend fun onAddProduct(prod: ProductDomain, selectedFile: File) {
        withContext(Dispatchers.IO) {
            try {
                _isLoading.postValue(true)
                val token = sharedPrefs.getString(UserConst.TOKEN)!!
                val userid = sharedPrefs.getString(UserConst.USER_ID)!!
                val params = HashMap<String, Any>()

                params["user_id"] = userid
                params["name"] = prod.name
                params["description"] = prod.description
                params["image_url"] = prod.imageURL!!
                params["price"] = prod.price
                params["status"] = prod.status
                params["tags"] = prod.tags

                val network = AppNetwork.service.onAddProductAsync(
                    bearer = setBearer(token),
                    params = paramsToRequestBody(params))
                    .await()

                if (network.status.matches((IS_SUCCESS.toRegex()))) {
                    val productDomain = network.result.asDomainModel()
                    // when the Product Form successfully created , get the id and pass to the file image
                    onUploadProductImage(productDomain.id, selectedFile)
                }


            } catch (e: HttpException) {
                Timber.e(e.message())
                _isLoading.postValue(false)
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    suspend fun onEditProduct(prod: ProductDomain, selectedFile: File) {
        withContext(Dispatchers.IO) {
            try {
                _isLoading.postValue(true)
                val token = sharedPrefs.getString(UserConst.TOKEN)!!
                val userid = sharedPrefs.getString(UserConst.USER_ID)!!
                val params = HashMap<String, Any>()

                params["user_id"] = userid
                params["name"] = prod.name!!
                params["description"] = prod.description!!
                params["image_url"] = prod.imageURL!!
                params["price"] = prod.price!!
                params["status"] = prod.status!!
                params["tags"] = prod.tags!!

                val network = AppNetwork.service.onEditProductAsync(
                    bearer = setBearer(token),
                    params = paramsToRequestBody(params))
                    .await()

                if (network.status.matches((IS_SUCCESS.toRegex()))) {
                    val productDomain = network.result.asDomainModel()
//                    _isProductCreated.postValue(productDomain)

                    // when the Product Form successfully created , get the id and pass to the file image
                    onUploadProductImage(productDomain.id, selectedFile)
                }
            } catch (e: HttpException) {
                Timber.e(e.message())
                _isLoading.postValue(false)
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    private suspend fun onUploadProductImage(productId: Long, file: File) {
        withContext(Dispatchers.IO) {
            try {
                _isLoading.postValue(true)
                val token = sharedPrefs.getString(UserConst.TOKEN)!!
                val userid = sharedPrefs.getString(UserConst.USER_ID)!!
                val params = HashMap<String, Any>()
                params["file"] = file

                val requestFile = RequestBody.create(MediaType.parse("image/*"), file)
                val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

                val network = AppNetwork.service.onAddProductImageAsync(
                    bearer = setBearer(token),
                    productId,
                    params = body
                ).await()
//                Timber.d(Gson().toJson(network))
                _isUploaded.postValue(true)
                _isLoading.postValue(false)
            } catch (e: HttpException) {
                Timber.e(e.message())
                _isLoading.postValue(false)
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

//    fun fileToMultipartBody(partName: String, file: File, fileContentType: String): MultipartBody.Part {
//        val requestBody = RequestBody.create(MediaType.parse(fileContentType), file)
//        return MultipartBody.Part.createFormData(partName, file.name, requestBody)
//    }


}