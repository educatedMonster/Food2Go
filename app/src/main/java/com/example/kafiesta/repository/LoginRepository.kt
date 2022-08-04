package com.example.kafiesta.repository

import com.example.kafiesta.network.AppService
import com.example.kafiesta.network.paramsToRequestBody
import com.example.kafiesta.utilities.helpers.SharedPrefs
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class LoginRepository (private val sharedPrefs: SharedPrefs) {

    suspend fun onLogin(username: String, password: String) {
        withContext(Dispatchers.IO) {
            val params = HashMap<String, Any>()
            params["email"] = username
            params["password"] = password
            val network = AppService.AppNetwork.service.loginAsync(
                paramsToRequestBody(params)
            ).await()
            val a = network
            val b = network
            Timber.d(Gson().toJson(network))
        }
    }

}