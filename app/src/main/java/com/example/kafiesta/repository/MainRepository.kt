package com.example.kafiesta.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.kafiesta.constants.UserConst
import com.example.kafiesta.domain.ResultDomain
import com.example.kafiesta.network.AppNetwork
import com.example.kafiesta.network.asDomainModel
import com.example.kafiesta.utilities.helpers.SharedPrefs
import com.example.kafiesta.utilities.setBearer
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class MainRepository(private val sharedPrefs: SharedPrefs) {

    private val _mainFormState = MutableLiveData<NetworkFormStateMain>()
    val mainFormState: LiveData<NetworkFormStateMain> get() = _mainFormState

    private val _userResult = MutableLiveData<ResultDomain>()
    val userResult: LiveData<ResultDomain> get() = _userResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    suspend fun getUserId(userId: Long) {
        withContext(Dispatchers.IO) {
            _isLoading.postValue(true)
            val token = sharedPrefs.getString(UserConst.TOKEN)!!
            val params = HashMap<String, Any>()
            params["id"] = userId

            val network = AppNetwork.service.getUserIdAsync(
                bearer = setBearer(token),
                userId = userId
            ).await()
            Timber.d(Gson().toJson(network))
            _userResult.postValue(network.result.asDomainModel())
            _isLoading.postValue(false)
        }
    }

    suspend fun onLogout() {
        withContext(Dispatchers.IO) {
            _mainFormState.postValue(
                NetworkFormStateMain(
                    onLogoutRequest = true,
                    isLoggingOut = true
                )
            )
            val token = sharedPrefs.getString(UserConst.TOKEN)!!
            val network = AppNetwork.service.onLogoutAsync(setBearer(token)).await()
            Timber.d(Gson().toJson(network))
            clearSecurePreference()
            _mainFormState.postValue(
                NetworkFormStateMain(
                    onLogoutRequest = true,
                    isLoggingOut = false
                )
            )
        }
    }

    suspend fun onLogoutOffline() {
        withContext(Dispatchers.IO) {
            _mainFormState.postValue(
                NetworkFormStateMain(
                    onLogoutRequest = true,
                    isLoggingOut = true
                )
            )
            clearSecurePreference()

            _mainFormState.postValue(
                NetworkFormStateMain(
                    onLogoutRequest = true,
                    isLoggingOut = false
                )
            )
        }
    }

    private fun clearSecurePreference() {
        sharedPrefs.clearAll()
    }

    data class NetworkFormStateMain(
        val onLogoutRequest: Boolean = false,
        val isLoggingOut: Boolean = false,
    )
}