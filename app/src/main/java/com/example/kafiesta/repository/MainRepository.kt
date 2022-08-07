package com.example.kafiesta.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.kafiesta.constants.UserConst
import com.example.kafiesta.domain.ProfileDomain
import com.example.kafiesta.network.AppNetwork
import com.example.kafiesta.network.asDomainModel
import com.example.kafiesta.network.paramsToRequestBody
import com.example.kafiesta.utilities.helpers.SharedPrefs
import com.example.kafiesta.utilities.setBearer
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class MainRepository(private val sharedPrefs: SharedPrefs) {

    private val _mainFormState = MutableLiveData<NetworkFormStateMain>()
    val mainFormState: LiveData<NetworkFormStateMain> get() = _mainFormState

    private val _updateFormState = MutableLiveData<UpdateFormState>()
    val updateFormState: LiveData<UpdateFormState> get() = _updateFormState

    private val _profile = MutableLiveData<ProfileDomain>()
    val profile: LiveData<ProfileDomain> get() = _profile

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    suspend fun onGetMe() {
        withContext(Dispatchers.IO) {
            try {
                _isLoading.postValue(true)
                val token = sharedPrefs.getString(UserConst.TOKEN)!!
                val network = AppNetwork.service.onGetMedAsync(
                    bearer = setBearer(token)
                ).await()
                Timber.d(Gson().toJson(network))
                _profile.postValue(network.result.asDomainModel())
                _isLoading.postValue(false)
            } catch (e: Exception) {
                Timber.d(e)
                _isLoading.postValue(false)
            }

        }
    }

    suspend fun onUpdateUserInfo(userInfo: UserInfo) {
        withContext(Dispatchers.IO) {
            try {
                _isLoading.postValue(true)
                val token = sharedPrefs.getString(UserConst.TOKEN)!!
                val params = HashMap<String, Any>()
                params["id"] = sharedPrefs.getString(UserConst.ID)!!.toLong()
                params["status"] = sharedPrefs.getString(UserConst.STATUS)!!
                params["first_name"] = userInfo.firstName
                params["last_name"] = userInfo.lastName
                params["email"] = userInfo.email

                val network = AppNetwork.service.onUpdateUserInfoAsync(
                    bearer = setBearer(token),
                    params = paramsToRequestBody(params)
                ).await()

                Timber.d(Gson().toJson(network))
                _profile.postValue(network.result.asDomainModel())
                _updateFormState.postValue(UpdateFormState(network.status, network.message))
                _isLoading.postValue(false)
            } catch (e: Exception) {
                Timber.d(e)
                _isLoading.postValue(false)
            }
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

    data class UpdateFormState(
        val isSuccess: String,
        val message: String,
    )

    data class UserInfo(
        val firstName: String,
        val lastName: String,
        val email: String,
    )
}