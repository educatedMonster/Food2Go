package com.example.kafiesta.screens.add_product

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.example.kafiesta.domain.ProductDomain
import com.example.kafiesta.repository.ProductRepository
import com.example.kafiesta.utilities.helpers.SharedPrefs
import com.example.kafiesta.utilities.helpers.getSecurePrefs
import id.zelory.compressor.Compressor
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

    fun addProduct(context: Context, product: ProductDomain, selectedFile: File) {
        viewModelScope.launch {
            try {
                val compressedFile = Compressor.compress(context, selectedFile)
                repository.onAddProduct(product, compressedFile)
            } catch (e: IOException) {
                Timber.d(e)
            }
        }
    }

    fun onUploadProductImage(productId: Long, imageFile: File) {
        viewModelScope.launch {
            try {
                repository.onUploadProductImage(productId, imageFile)
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