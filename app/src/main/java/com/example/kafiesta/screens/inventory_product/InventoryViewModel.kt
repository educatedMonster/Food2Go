package com.example.kafiesta.screens.inventory_product

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.kafiesta.repository.InventoryRepository
import com.example.kafiesta.utilities.helpers.SharedPrefs
import com.example.kafiesta.utilities.helpers.getSecurePrefs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException

class InventoryViewModel(application: Application) : AndroidViewModel(application) {
    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    private val repository = InventoryRepository(SharedPrefs(getSecurePrefs(application)))

    val productInventoryList = repository.productInventoryList
    val inventoryList = repository.inventoryList
    val isAddedInventory = repository.isAddedInventory
    val isModifyQuantity = repository.isModifyQuantity
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

    fun getAllInventory(length: Long, start: Long, search: String) {
        viewModelScope.launch {
            try {
                repository.getAllInventory(length, start, search)
            } catch (e: IOException) {
                Timber.d(e)
            }
        }
    }

    fun modifyQuantity(quantity: String, productId: Long) {
        viewModelScope.launch {
            try {
                repository.onModifyQuantity(quantity, productId)
            } catch (e: IOException) {
                Timber.d(e)
            }
        }
    }

    fun inventoryAndQuantity(productId: Long, quantity: String) {
        viewModelScope.launch {
            try {
                repository.onInventoryAndQuantity(productId, quantity)
            } catch (e: IOException) {
                Timber.d(e)
            }
        }
    }

    fun removeInventory(productId: Long) {
        viewModelScope.launch {
            try {
                repository.onRemoveInventory(productId)
            } catch (e: IOException) {
                Timber.d(e)
            }
        }
    }
}
