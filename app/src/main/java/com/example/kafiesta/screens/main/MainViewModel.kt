package com.example.kafiesta.screens.main

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.example.kafiesta.repository.LoginRepository
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
    private val loginRepository = LoginRepository(SharedPrefs(getSecurePrefs(application)))

    val userDomain = loginRepository.networkUserResponse
    val isLoading = loginRepository.isLoading

    fun login(username: String, password: String) {
        viewModelScope.launch {
            try {
                loginRepository.onLogin(username, password)
            } catch (e: IOException) {
                Timber.d(e)
            }
        }
    }

    fun getUserId(context: Context, userId: Int) {
        viewModelScope.launch {
            try {
                loginRepository.getUserId(userId)
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