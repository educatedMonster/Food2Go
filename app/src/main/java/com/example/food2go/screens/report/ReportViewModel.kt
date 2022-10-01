package com.example.food2go.screens.report

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.food2go.repository.ReportRepository
import com.example.food2go.utilities.helpers.SharedPrefs
import com.example.food2go.utilities.helpers.getSecurePrefs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException

class ReportViewModel(application: Application) : AndroidViewModel(application) {
    // TODO: Implement the ViewModel
    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    private val repository = ReportRepository(SharedPrefs(getSecurePrefs(application)))


    val isLoading = repository.isLoading
    val salesReport = repository.salesReport
    val eodReport = repository.eodReport

    fun getSalesReport(date_from: String, date_to: String) {
        viewModelScope.launch {
            try {
                repository.getSalesReport(date_from, date_to)
            } catch (e: IOException) {
                Timber.d(e)
            }
        }
    }

    fun getEODReport() {
        viewModelScope.launch {
            try {
                repository.getEODReport()
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