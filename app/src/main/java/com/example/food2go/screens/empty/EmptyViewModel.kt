package com.example.food2go.screens.empty

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.food2go.repository.EmptyRepository
import com.example.food2go.utilities.helpers.SharedPrefs
import com.example.food2go.utilities.helpers.getSecurePrefs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException

class EmptyViewModel(application: Application) : AndroidViewModel(application) {
    // TODO: Implement the ViewModel
    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    private val repository = EmptyRepository(SharedPrefs(getSecurePrefs(application)))

    val isLoading = repository.isLoading

    fun triggerPusher(
        data: Any,
        event: String,
        channelName: String,
    ) {
        viewModelScope.launch {
            try {
                repository.onTriggerPusher(
                    data,
                    event,
                    channelName)
            } catch (network: IOException) {
                Timber.d(network)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}