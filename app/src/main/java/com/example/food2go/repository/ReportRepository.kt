package com.example.food2go.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.food2go.constants.UserConst
import com.example.food2go.domain.ReportBaseNetworkDomain
import com.example.food2go.network.AppNetwork
import com.example.food2go.network.asDomainModel
import com.example.food2go.network.paramsToRequestBody
import com.example.food2go.utilities.getDateNow
import com.example.food2go.utilities.helpers.SharedPrefs
import com.example.food2go.utilities.setBearer
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import timber.log.Timber

class ReportRepository(sharedPrefs: SharedPrefs) {
    private val token = sharedPrefs.getString(UserConst.TOKEN)!!
    private val userid = sharedPrefs.getString(UserConst.USER_ID)!!

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _salesReport = MutableLiveData<ReportBaseNetworkDomain>()
    val salesReport: LiveData<ReportBaseNetworkDomain> get() = _salesReport

    private val _eodReport = MutableLiveData<ReportBaseNetworkDomain>()
    val eodReport: LiveData<ReportBaseNetworkDomain> get() = _eodReport

    suspend fun getSalesReport(date_from: String, date_to: String) {
        withContext(Dispatchers.IO) {
            try {
                _isLoading.postValue(true)
                val params = HashMap<String, Any>()
                params["user_id"] = userid
                params["date_from"] = date_from
                params["date_to"] = date_to

                val network = AppNetwork.service.onSalesReportAsync(
                    bearer = setBearer(token),
                    params = paramsToRequestBody(params))
                    .await()
                Timber.d(Gson().toJson(network))
                _salesReport.postValue(network.asDomainModel())
                _isLoading.postValue(false)
            } catch (e: HttpException) {
                Timber.e(e.message())
                _isLoading.postValue(false)
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    suspend fun getEODReport() {
        withContext(Dispatchers.IO) {
            try {
                _isLoading.postValue(true)
                val params = HashMap<String, Any>()
                params["user_id"] = userid
                params["date"] = getDateNow()

                val network = AppNetwork.service.onEODReportAsync(
                    bearer = setBearer(token),
                    params = paramsToRequestBody(params))
                    .await()
                Timber.d(Gson().toJson(network))
                _eodReport.postValue(network.asDomainModel())
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