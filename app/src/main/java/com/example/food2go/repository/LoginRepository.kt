package com.example.food2go.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.food2go.constants.ServerConst.IS_SUCCESS
import com.example.food2go.constants.UserConst
import com.example.food2go.domain.LoginBaseDomain
import com.example.food2go.domain.LoginDataDomain
import com.example.food2go.domain.LoginInformationsDomain
import com.example.food2go.domain.LoginProfileDomain
import com.example.food2go.network.AppNetwork
import com.example.food2go.network.asDomainModel
import com.example.food2go.network.paramsToRequestBody
import com.example.food2go.utilities.helpers.SharedPrefs
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import timber.log.Timber

class LoginRepository(private val sharedPrefs: SharedPrefs) {

    private val _networkUserResponse = MutableLiveData<LoginBaseDomain>()
    val networkDataResponse: LiveData<LoginBaseDomain> get() = _networkUserResponse

    private val _networkFormState = MutableLiveData<NetworkFormStateLogin>()
    val networkFormStateLogin: LiveData<NetworkFormStateLogin> get() = _networkFormState

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    suspend fun onLogin(email: String, password: String, isRemember: Boolean) {
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
                if (network.status.matches(IS_SUCCESS.toRegex())) {
                    _networkUserResponse.postValue(network.asDomainModel())
                    saveToSecurePreference(
                        network.data.asDomainModel(),
                        network.data.profile.asDomainModel(),
                        network.data.profile.userInformations?.asDomainModel(),
                        isRemember
                    )
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
        dataDomain: LoginDataDomain,
        profileDomain: LoginProfileDomain,
        loginInformationsDomain: LoginInformationsDomain?,
        isRemember: Boolean,
    ) {
        sharedPrefs.save(UserConst.SP_USER_REMEMBER_ME, isRemember)
        sharedPrefs.save(UserConst.TOKEN, dataDomain.token)
        sharedPrefs.save(UserConst.TOKEN_TYPE, dataDomain.tokenType)
        sharedPrefs.save(UserConst.EXPIRES_IN, dataDomain.expiresIn)
        sharedPrefs.save(UserConst.USER_ID, profileDomain.userId)
        sharedPrefs.save(UserConst.FIRSTNAME, profileDomain.firstName)
        sharedPrefs.save(UserConst.LASTNAME, profileDomain.lastName)
        sharedPrefs.save(UserConst.EMAIL, profileDomain.email)
        sharedPrefs.save(UserConst.STATUS, profileDomain.status)
        sharedPrefs.save(UserConst.ROLE, profileDomain.role)
        if (loginInformationsDomain != null) {
            sharedPrefs.save(UserConst.INFO_ID, loginInformationsDomain.infoId)
            sharedPrefs.save(UserConst.INFO_ID_USER_ID, loginInformationsDomain.userID)
            sharedPrefs.save(UserConst.PRIMARY_CONTACT, loginInformationsDomain.primaryContact)
            sharedPrefs.save(UserConst.SECONDARY_CONTACT, loginInformationsDomain.secondaryContact)
            sharedPrefs.save(UserConst.COMPLETE_ADDRESS, loginInformationsDomain.completeAddress)
        }
    }

    data class NetworkFormStateLogin(
        val serverError: Boolean = false,
        val message: String? = "",
    )

}