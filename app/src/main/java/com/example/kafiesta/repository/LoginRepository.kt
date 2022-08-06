package com.example.kafiesta.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.kafiesta.constants.UserConst
import com.example.kafiesta.domain.DataDomain
import com.example.kafiesta.domain.ProfileDomain
import com.example.kafiesta.domain.UserDomain
import com.example.kafiesta.network.AppNetwork
import com.example.kafiesta.network.asDomainModel
import com.example.kafiesta.network.paramsToRequestBody
import com.example.kafiesta.utilities.helpers.SharedPrefs
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import timber.log.Timber

class LoginRepository(private val sharedPrefs: SharedPrefs) {

    private val _networkUserResponse = MutableLiveData<UserDomain>()
    val networkDataResponse: LiveData<UserDomain> get() = _networkUserResponse

    private val _networkFormState = MutableLiveData<NetworkFormStateLogin>()
    val networkFormStateLogin: LiveData<NetworkFormStateLogin> get() = _networkFormState

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    suspend fun onLogin(email: String, password: String) {
        withContext(Dispatchers.IO) {
            try {
                _isLoading.postValue(true)

                val params = HashMap<String, Any>()
                params["email"] = email
                params["password"] = password

                val network = AppNetwork.service.loginAsync(
                    paramsToRequestBody(params)
                ).await()
                Timber.d(Gson().toJson(network))
                if (network.status.matches("success".toRegex())) {
                    _networkUserResponse.postValue(network.asDomainModel())
                    saveToSecurePreference(
                        network.data.asDomainModel(),
                        network.data.profile.asDomainModel())
                }
            } catch (e: HttpException) {
                Timber.e(e)
                _networkFormState.postValue(
                    NetworkFormStateLogin(
                        serverError = true,
                        message = e.message
                    )
                )
                _isLoading.postValue(false)
            }
        }
    }

    private fun saveToSecurePreference(
        dataDomain: DataDomain,
        profileDomain: ProfileDomain,
    ) {
        sharedPrefs.save(UserConst.TOKEN, dataDomain.token)
        sharedPrefs.save(UserConst.TOKEN_TYPE, dataDomain.tokenType)
        sharedPrefs.save(UserConst.EXPIRES_IN, dataDomain.expiresIn)
        sharedPrefs.save(UserConst.ID, profileDomain.id)
        sharedPrefs.save(UserConst.FIRSTNAME, profileDomain.firstName)
        sharedPrefs.save(UserConst.LASTNAME, profileDomain.lastName)
        sharedPrefs.save(UserConst.EMAIL, profileDomain.email)
        sharedPrefs.save(UserConst.ADDRESS, profileDomain.status)
        sharedPrefs.save(UserConst.ROLE, profileDomain.role)
        sharedPrefs.save(UserConst.USERINFORMATION, profileDomain.userInformations ?: "")
    }

    data class NetworkFormStateLogin(
        val serverError: Boolean = false,
        val message: String? = "",
    )

}