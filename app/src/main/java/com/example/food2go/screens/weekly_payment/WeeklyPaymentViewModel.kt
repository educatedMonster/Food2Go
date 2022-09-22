package com.example.food2go.screens.weekly_payment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.food2go.repository.WeeklyPaymentRepository
import com.example.food2go.utilities.helpers.SharedPrefs
import com.example.food2go.utilities.helpers.getSecurePrefs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import java.io.IOException

class WeeklyPaymentViewModel(application: Application) : AndroidViewModel(application) {
    // TODO: Implement the ViewModel
    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    private val repository = WeeklyPaymentRepository(SharedPrefs(getSecurePrefs(application)))

    val weeklyPayment = repository.weeklyPayment
    val isUploaded = repository.isUploaded
    val isLoading = repository.isLoading

    fun getWeeklyPayment(merchant_id: Long) {
        viewModelScope.launch {
            try {
                repository.onGetWeeklyPayment(
                    merchant_id)
            } catch (network: IOException) {
                Timber.d(network)
            }
        }
    }

    fun uploadWeeklyPaymentImage(weeklyPaymentId: Long, file: File) {
        viewModelScope.launch {
            try {
                repository.onUploadWeeklyPaymentImage(weeklyPaymentId, file)
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