package com.example.food2go.screens.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.food2go.domain.ProfileDomain
import com.example.food2go.repository.MainRepository
import com.example.food2go.utilities.helpers.SharedPrefs
import com.example.food2go.utilities.helpers.getSecurePrefs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import java.io.IOException

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    private val mainRepository = MainRepository(SharedPrefs(getSecurePrefs(application)))

    val mainFormState = mainRepository.mainFormState
    val updateFormState = mainRepository.updateFormState
    val profile = mainRepository.profile
    val isUploaded = mainRepository.isUploaded
    val isLoading = mainRepository.isLoading

    fun logout() {
        viewModelScope.launch {
            try {
                mainRepository.onLogout()
            } catch (e: IOException) {
                mainRepository.onLogoutOffline()
                Timber.d(e)
            }
        }
    }

    fun getMe() {
        viewModelScope.launch {
            try {
                mainRepository.onGetMe()
            } catch (networkError: IOException) {
                networkError.message.toString()
            }
        }
    }

    fun updateUserInfo(profileDomain: ProfileDomain, file: File?) {
        viewModelScope.launch {
            try {
                mainRepository.onUpdateUserInfo(profileDomain, file)
            } catch (e: Exception) {
                e.message.toString()
            }
        }
    }

    fun uploadImage(userShopId: Long, file: File) {
        viewModelScope.launch {
            try {
                mainRepository.onUploadImage(userShopId, file)
            } catch (e: IOException) {
                Timber.d(e)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}