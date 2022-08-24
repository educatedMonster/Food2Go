package com.example.kafiesta.screens.product_and_inventory.product

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.kafiesta.domain.ProductDomain
import com.example.kafiesta.domain.ProductDomaintest
import com.example.kafiesta.repository.ProductRepository
import com.example.kafiesta.utilities.helpers.SharedPrefs
import com.example.kafiesta.utilities.helpers.getSecurePrefs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import java.io.IOException

class ProductViewModel(application: Application) : AndroidViewModel(application) {

    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    private val repository = ProductRepository(SharedPrefs(getSecurePrefs(application)))

    val productList = repository.productList
    val isLoading = repository.isLoading
    val isUpdated = repository.isUpdated
    val isDeleted = repository.isDeleted
    val isAddedInventory = repository.isAddedInventory
    val isProductCreated = repository.isProductCreated
    val isUploaded = repository.isUploaded

    fun getAllProducts(length: Long, start: Long, search: String) {
        viewModelScope.launch {
            try {
                repository.onGetAllProducts(length, start, search)
            } catch (e: IOException) {
                Timber.d(e)
            }
        }
    }

    fun addProduct(product: ProductDomain, selectedFile: File) {
        viewModelScope.launch {
            try {
                repository.onAddProduct(product, selectedFile)
            } catch (e: IOException) {
                Timber.d(e)
            }
        }
    }

    fun editProduct(product: ProductDomaintest, selectedFile: File?) {
        viewModelScope.launch {
            try {
                repository.onEditProduct(product, selectedFile)
            } catch (e: IOException) {
                Timber.d(e)
            }
        }
    }

    fun deleteProduct(productId: Long) {
        viewModelScope.launch {
            try {
                repository.onDeleteProductAsync(productId)
            } catch (e: IOException) {
                Timber.d(e)
            }
        }
    }

    fun addInventory(productId: Long) {
        viewModelScope.launch {
            try {
                repository.onaAddInventory(productId)
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