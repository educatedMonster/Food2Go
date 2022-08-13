package com.example.kafiesta.screens.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.kafiesta.domain.ProfileDomain
import com.example.kafiesta.repository.MainRepository
import com.example.kafiesta.utilities.helpers.SharedPrefs
import com.example.kafiesta.utilities.helpers.getSecurePrefs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    private val mainRepository = MainRepository(SharedPrefs(getSecurePrefs(application)))

    val mainFormState = mainRepository.mainFormState
    val updateFormState = mainRepository.updateFormState
    val data = mainRepository.data
    val profile = mainRepository.profile
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

    fun updateUserInfo(profileDomain: ProfileDomain) {
        viewModelScope.launch {
            try {
                mainRepository.onUpdateUserInfo(profileDomain)
            } catch (e: Exception) {
                e.message.toString()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}