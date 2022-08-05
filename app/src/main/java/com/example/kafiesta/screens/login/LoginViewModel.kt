package com.example.kafiesta.screens.login

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.kafiesta.R
import com.example.kafiesta.repository.LoginRepository
import com.example.kafiesta.utilities.helpers.SharedPrefs
import com.example.kafiesta.utilities.helpers.getSecurePrefs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    private val loginRepository = LoginRepository(SharedPrefs(getSecurePrefs(application)))

    val userDomain = loginRepository.networkDataResponse
    val networkFormState = loginRepository.networkFormState
    val isLoading = loginRepository.isLoading

    private val _loginFormState = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> get() = _loginFormState

    /**
     * This function will validate the credentials of the user if that inputted characters meet the required structure
     */
    fun validateLoginCredentials(context: Context, email: String, password: String) {
        if (email.isEmpty() && password.isEmpty()) {
            _loginFormState.value = LoginFormState(
                isInvalidEmail = true,
                invalidEmailMessage = context.getString(R.string.login_empty_username_email),
                isInvalidPassword = true,
                invalidPasswordMessage = context.getString(R.string.login_empty_password)
            )
        } else if (email.isEmpty()) {
            _loginFormState.value = LoginFormState(
                isInvalidEmail = true,
                invalidEmailMessage = context.getString(R.string.login_empty_username_email)
            )
        } else if (password.isEmpty()) {
            _loginFormState.value = LoginFormState(
                isInvalidPassword = true,
                invalidPasswordMessage = context.getString(R.string.login_empty_password)
            )
        } else {
            login(email, password)
        }
    }

    private fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                setLoading(true)
                loginRepository.onLogin(email, password)
            } catch (networkError: IOException) {
                Timber.d(networkError)
                setNetworkError(networkError.message.toString())
                setLoading(false)
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

    fun setLoading(set: Boolean) {
        _loginFormState.value = LoginFormState(
            isLoading = set
        )
    }

    private fun setNetworkError(message: String) {
        _loginFormState.value = LoginFormState(
            isNetworkError = true,
            networkErrorMessage = message
        )
    }

    data class LoginFormState(
        var isLoading: Boolean = false,
        var isNetworkError: Boolean = false,
        var networkErrorMessage: String = "",
        var isInvalidEmail: Boolean = false,
        var invalidEmailMessage: String = "",
        var isInvalidPassword: Boolean = false,
        var invalidPasswordMessage: String = "",
    )

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}