package com.example.food2go.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.food2go.constants.UserConst
import com.example.food2go.domain.*
import com.example.food2go.network.AppNetwork
import com.example.food2go.network.asDomainModel
import com.example.food2go.network.paramsToRequestBody
import com.example.food2go.utilities.helpers.SharedPrefs
import com.example.food2go.utilities.setBearer
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import timber.log.Timber
import java.io.File


class MainRepository(private val sharedPrefs: SharedPrefs) {
    val token = sharedPrefs.getString(UserConst.TOKEN)!!

    private val _mainFormState = MutableLiveData<NetworkFormStateMain>()
    val mainFormState: LiveData<NetworkFormStateMain> get() = _mainFormState

    private val _updateFormState = MutableLiveData<UpdateFormState>()
    val updateFormState: LiveData<UpdateFormState> get() = _updateFormState

    private val _profile = MutableLiveData<ProfileDomain>()
    val profile: LiveData<ProfileDomain> get() = _profile

    private val _isUploaded = MutableLiveData<Boolean>()
    val isUploaded: LiveData<Boolean> get() = _isUploaded

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    suspend fun onGetMe() {
        withContext(Dispatchers.IO) {
            try {
                _isLoading.postValue(true)
                val network = AppNetwork.service.onGetMedAsync(
                    bearer = setBearer(token)
                ).await()
                Timber.d(Gson().toJson(network))
                _profile.postValue(network.result.asDomainModel())

                saveToSecurePreference(
                    network.result.userInformation.asDomainModel(),
                    network.result.userShop.asDomainModel()
                )
                _isLoading.postValue(false)
            } catch (e: Exception) {
                Timber.d(e)
                _isLoading.postValue(false)
            }

        }
    }

    suspend fun onUpdateUserInfo(profileDomain: ProfileDomain, selectedFile: File?) {
        withContext(Dispatchers.IO) {
            try {
                _isLoading.postValue(true)
                val params = HashMap<String, Any>()
                params["id"] = sharedPrefs.getString(UserConst.USER_ID)!!.toLong()
                params["status"] = profileDomain.status
                params["role"] = profileDomain.role
                params["first_name"] = profileDomain.firstName
                params["last_name"] = profileDomain.lastName
                params["email"] = profileDomain.email
                if (profileDomain.password != null) {
                    if (profileDomain.password.isNotBlank() &&
                        profileDomain.password.isNotEmpty() &&
                        profileDomain.password.toString().trim().isNotEmpty() &&
                        (!profileDomain.password.matches("".toRegex()))) {
                    params["password"] = profileDomain.password
                    }
                }
                // stick to this shitty long updating form for the mean time
                // Contact Info
                params["user_informations[complete_address]"] = profileDomain.userInformation!!.complete_address
                params["user_informations[primary_contact]"] = profileDomain.userInformation.primary_contact
                params["user_informations[secondary_contact]"] = profileDomain.userInformation.secondary_contact

                // stick to this shitty long updating form for the mean time
                // Shop Info
                params["user_shop[name]"] = profileDomain.user_shop!!.name
                params["user_shop[address]"] = profileDomain.user_shop.address
                params["user_shop[contact]"] = profileDomain.user_shop.contact
                params["user_shop[open_hour]"] = profileDomain.user_shop.open_hour!!
                params["user_shop[close_hour]"] = profileDomain.user_shop.close_hour!!
                params["user_shop[status]"] = profileDomain.user_shop.status
                params["user_shop[monday]"] = profileDomain.user_shop.monday
                params["user_shop[tuesday]"] = profileDomain.user_shop.tuesday
                params["user_shop[wednesday]"] = profileDomain.user_shop.wednesday
                params["user_shop[thursday]"] = profileDomain.user_shop.thursday
                params["user_shop[friday]"] = profileDomain.user_shop.friday
                params["user_shop[saturday]"] = profileDomain.user_shop.saturday
                params["user_shop[sunday]"] = profileDomain.user_shop.sunday
                params["user_shop[pm_cod]"] = profileDomain.user_shop.pm_cod
                params["user_shop[pm_gcash]"] = profileDomain.user_shop.pm_gcash
                params["user_shop[is_active]"] = profileDomain.user_shop.is_active
                params["user_shop[delivery_charge]"] = profileDomain.user_shop.delivery_charge!!


                val network = AppNetwork.service.onUpdateUserInfoAsync(
                    bearer = setBearer(token),
                    params = paramsToRequestBody(params)
                ).await()

                if (selectedFile != null) {
                    // when the Profile Form successfully created , get the user_shop_id and pass to the file image
                    onUploadImage(profileDomain.user_shop.id, selectedFile)
                }
//                val a = Timber.d(Gson().toJson(network))
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

    suspend fun onUploadImage(userShopId: Long, file: File) {
        withContext(Dispatchers.IO) {
            try {
                _isLoading.postValue(true)
                val params = HashMap<String, Any>()
                params["file"] = file

                val requestFile = RequestBody.create(MediaType.parse("image/*"), file)
                val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
                val network = AppNetwork.service.onUploadImageAsync(
                    bearer = setBearer(token),
                    userShopId = userShopId,
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

    suspend fun onLogout() {
        withContext(Dispatchers.IO) {
            _mainFormState.postValue(
                NetworkFormStateMain(
                    onLogoutRequest = true,
                    isLoggingOut = true
                )
            )
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
        userInformationDomain: UserInformationDomain,
        userShopDomain: UserShopDomain,
    ) {
        sharedPrefs.save(UserConst.INFO_ID, userInformationDomain.id)
        sharedPrefs.save(UserConst.SHOP_ID, userShopDomain.id)
    }
}