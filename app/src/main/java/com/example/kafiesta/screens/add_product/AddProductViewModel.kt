package com.example.kafiesta.screens.add_product

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class AddProductViewModel(application: Application): AndroidViewModel(application) {

    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)
//    private val repository = AddTaskRepository(SharedPrefs(getSecurePrefs(application)))


}