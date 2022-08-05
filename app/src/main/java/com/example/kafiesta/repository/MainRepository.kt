package com.example.kafiesta.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.kafiesta.constants.UserConst
import com.example.kafiesta.domain.ProfileDomain
import com.example.kafiesta.domain.UserDomain
import com.example.kafiesta.network.AppNetwork
import com.example.kafiesta.utilities.helpers.SharedPrefs
import com.example.kafiesta.utilities.setBearer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainRepository (private val sharedPrefs: SharedPrefs) {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    suspend fun onLogout() {
        withContext(Dispatchers.IO) {
            _isLoading.postValue(true)

            val token = sharedPrefs.getString(UserConst.TOKEN)!!
            val network =  AppNetwork.service.onLogoutAsync(
                bearer = setBearer(token)
            ).await()
//            Timber.d(Gson().toJson(network))

            clearSecurePreference()
            _isLoading.postValue(false)
        }

        suspend fun onLogoutOffline() {
            withContext(Dispatchers.IO) {
                _isLoading.postValue(true)
                clearSecurePreference()
                _isLoading.postValue(true)
            }
        }
    }

    private fun saveToSecurePreference(userDomain: UserDomain, profileDomain: ProfileDomain) {
        sharedPrefs.save(UserConst.ID, profileDomain.id)
        sharedPrefs.save(UserConst.FIRSTNAME, profileDomain.firstName)
        sharedPrefs.save(UserConst.LASTNAME, profileDomain.lastName)
        sharedPrefs.save(UserConst.EMAIL, profileDomain.email)
        sharedPrefs.save(UserConst.ADDRESS, profileDomain.status)
        sharedPrefs.save(UserConst.ROLE, profileDomain.role)
        sharedPrefs.save(UserConst.USERINFORMATION, profileDomain.userInformations ?: "")
        sharedPrefs.save(UserConst.TOKEN, userDomain.token)
    }

    private fun clearSecurePreference() {
        sharedPrefs.clearAll()
    }
}