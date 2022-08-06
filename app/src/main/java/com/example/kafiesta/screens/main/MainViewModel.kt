package com.example.kafiesta.screens.main

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
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
    val userResult = mainRepository.userResult
    val isLoading = mainRepository.isLoading

    fun onLogout() {
        viewModelScope.launch {
            try {
                mainRepository.onLogout()
            } catch (e: IOException) {
                mainRepository.onLogoutOffline()
                Timber.d(e)
            }
        }
    }

    fun getUserId(userId: Long) {
        viewModelScope.launch {
            try {
                mainRepository.getUserId(userId)
            } catch (networkError: IOException) {
                networkError.message.toString()
            }
        }
    }



    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}