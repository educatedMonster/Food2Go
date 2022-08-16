package com.example.kafiesta.screens.inventory_product

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.kafiesta.repository.ProductInventoryRepository
import com.example.kafiesta.utilities.helpers.SharedPrefs
import com.example.kafiesta.utilities.helpers.getSecurePrefs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException

class ProductInventoryViewModel(application: Application) : AndroidViewModel(application) {
    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    private val repository = ProductInventoryRepository(SharedPrefs(getSecurePrefs(application)))

    val productInventoryList = repository.productInventoryList
    val isLoading = repository.isLoading

    fun getAllProductInventory(length: Long, start: Long, search: String) {
        viewModelScope.launch {
            try {
                repository.onGetAllProductInventory(length, start, search)
            } catch (e: IOException) {
                Timber.d(e)
            }
        }
    }

    fun modifyQuantity(quantity: Long, productId: Long) {
        viewModelScope.launch {
            try {
                repository.onModifyQuantity(quantity, productId)
            } catch (e: IOException) {
                Timber.d(e)
            }
        }
    }
}
