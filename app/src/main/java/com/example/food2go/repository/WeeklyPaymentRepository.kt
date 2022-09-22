package com.example.food2go.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.food2go.constants.UserConst
import com.example.food2go.domain.WeeklyPaymentBaseNetworkDomain
import com.example.food2go.network.AppNetwork
import com.example.food2go.network.asDomainModel
import com.example.food2go.utilities.helpers.SharedPrefs
import com.example.food2go.utilities.setBearer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import timber.log.Timber
import java.io.File

class WeeklyPaymentRepository(
    private val sharedPrefs: SharedPrefs,
) {
    private val token = sharedPrefs.getString(UserConst.TOKEN)!!
    private val userid = sharedPrefs.getString(UserConst.USER_ID)!!

    private val _weeklyPayment = MutableLiveData<WeeklyPaymentBaseNetworkDomain>()
    val weeklyPayment: LiveData<WeeklyPaymentBaseNetworkDomain> get() = _weeklyPayment

    private val _isUploaded = MutableLiveData<Boolean>()
    val isUploaded: LiveData<Boolean> get() = _isUploaded

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    suspend fun onGetWeeklyPayment(merchant_id: Long) {
        withContext(Dispatchers.IO) {
            try {
                _isLoading.postValue(true)
                val network = AppNetwork.service.onWeeklyPaymentAsync(
                    bearer = setBearer(token),
                    merchant_id = merchant_id)
                    .await()
//                Timber.d(Gson().toJson(network))
                _weeklyPayment.postValue(network.asDomainModel())
                _isLoading.postValue(false)
            } catch (e: HttpException) {
                Timber.e(e.message())
                _isLoading.postValue(false)
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    suspend fun onUploadWeeklyPaymentImage(weeklyPaymentId: Long, file: File) {
        withContext(Dispatchers.IO) {
            try {
                _isLoading.postValue(true)
                val params = HashMap<String, Any>()
                params["file"] = file

                val requestFile = RequestBody.create(MediaType.parse("image/*"), file)
                val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
                val network = AppNetwork.service.onApproveByMerchantAsync(
                    bearer = setBearer(token),
                    weeklypayment_id = weeklyPaymentId,
                    params = body
                ).await()
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

}