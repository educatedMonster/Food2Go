package com.example.food2go.screens.login

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.food2go.R
import com.example.food2go.repository.LoginRepository
import com.example.food2go.utilities.helpers.SharedPrefs
import com.example.food2go.utilities.helpers.getSecurePrefs
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
    val networkFormState = loginRepository.networkFormStateLogin
    val isLoading = loginRepository.isLoading

    private val _loginFormState = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> get() = _loginFormState

    /**
     * This function will validate the credentials of the user if that inputted characters meet the required structure
     */
    fun validateLoginCredentials(context: Context, email: String, password: String, isRemember: Boolean) {
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
            login(email, password, isRemember)
        }
    }

    private fun login(email: String, password: String, isRemember: Boolean) {
        viewModelScope.launch {
            try {
                setLoading(true)
                loginRepository.onLogin(email, password, isRemember)
            } catch (networkError: IOException) {
                Timber.d(networkError)
                setNetworkError(networkError.message.toString())
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