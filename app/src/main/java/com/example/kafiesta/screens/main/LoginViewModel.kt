package com.example.kafiesta.screens.main

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.example.kafiesta.repository.LoginRepository
import com.example.kafiesta.utilities.helpers.SharedPrefs
import com.example.kafiesta.utilities.helpers.getSecurePrefs
import kotlinx.coroutines.*
import java.io.IOException

class LoginViewModel(application: Application): AndroidViewModel(application) {

    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    private val loginRepository = LoginRepository(SharedPrefs(getSecurePrefs(application)))

    fun login(context: Context, username: String, password: String){
        viewModelScope.launch {
            try{
                loginRepository.onLogin(username, password)
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