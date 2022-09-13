package com.example.kafiesta.screens.empty

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.kafiesta.repository.EmptyRepository
import com.example.kafiesta.utilities.helpers.SharedPrefs
import com.example.kafiesta.utilities.helpers.getSecurePrefs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class EmptyViewModel (application: Application) : AndroidViewModel(application) {
    // TODO: Implement the ViewModel
    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    private val repository = EmptyRepository(SharedPrefs(getSecurePrefs(application)))



    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}