package com.example.kafiesta.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.kafiesta.constants.UserConst
import com.example.kafiesta.domain.*
import com.example.kafiesta.network.AppNetwork
import com.example.kafiesta.network.asDomainModel
import com.example.kafiesta.network.paramsToRequestBody
import com.example.kafiesta.utilities.helpers.SharedPrefs
import com.example.kafiesta.utilities.setBearer
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONException
import timber.log.Timber


class MainRepository(private val sharedPrefs: SharedPrefs) {

    private val _mainFormState = MutableLiveData<NetworkFormStateMain>()
    val mainFormState: LiveData<NetworkFormStateMain> get() = _mainFormState

    private val _updateFormState = MutableLiveData<UpdateFormState>()
    val updateFormState: LiveData<UpdateFormState> get() = _updateFormState

    private val _data = MutableLiveData<UserBaseDomain>()
    val data: LiveData<UserBaseDomain> get() = _data

    private val _profile = MutableLiveData<ProfileDomain>()
    val profile: LiveData<ProfileDomain> get() = _profile

    private val _contact = MutableLiveData<UserInformationsDomain>()
    val contact: LiveData<UserInformationsDomain> get() = _contact

    private val _userShop = MutableLiveData<UserShopDomain>()
    val userShop: LiveData<UserShopDomain> get() = _userShop

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

                saveToSecurePreference(
                    network.result.userInformations.asDomainModel(),
                    network.result.userShop.asDomainModel()
                )
                _isLoading.postValue(false)
            } catch (e: Exception) {
                Timber.d(e)
                _isLoading.postValue(false)
            }

        }
    }

    suspend fun onUpdateUserInfo(profileDomain: ProfileDomain) {
        withContext(Dispatchers.IO) {
            try {
                _isLoading.postValue(true)
                val token = sharedPrefs.getString(UserConst.TOKEN)!!
                val params = HashMap<String, Any>()
                params["id"] = sharedPrefs.getString(UserConst.USER_ID)!!.toLong()
                params["status"] = profileDomain.status
                params["role"] = profileDomain.role
                params["first_name"] = profileDomain.firstName
                params["last_name"] = profileDomain.lastName
                params["email"] = profileDomain.email

                // stick to this shitty long updating form for the mean time
                params["user_informations[complete_address]"] = profileDomain.userInformations!!.complete_address
                params["user_informations[primary_contact]"] = profileDomain.userInformations.primary_contact
                params["user_informations[secondary_contact]"] = profileDomain.userInformations.secondary_contact


                val network = AppNetwork.service.onUpdateUserInfoAsync(
                    bearer = setBearer(token),
                    params = paramsToRequestBody(params)
                ).await()

                val a = Timber.d(Gson().toJson(network))
                _data.postValue(network.asDomainModel())
//                _profile.postValue(network.result.asDomainModel())
                _updateFormState.postValue(UpdateFormState(network.status, network.message))
                _isLoading.postValue(false)
            } catch (e: Exception) {
                Timber.d(e)
                _isLoading.postValue(false)
            } finally {
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

    private fun saveToSecurePreference(
        userInformationsDomain: UserInformationsDomain,
        userShopDomain: UserShopDomain,
    ) {
        sharedPrefs.save(UserConst.INFO_ID, userInformationsDomain.id)
        sharedPrefs.save(UserConst.SHOP_ID, userShopDomain.id)
    }
}