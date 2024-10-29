package com.irisay.lab8_room.ui.marketproducts.viewmodel

import android.app.Application
import android.net.Uri
import android.os.Environment
import androidx.lifecycle.*
import com.irisay.lab8_room.database.market.MarketEntity
import com.irisay.lab8_room.ui.marketproducts.repository.MarketRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MarketViewModel(application: Application, private val repository: MarketRepository) : AndroidViewModel(application) {

    private val _marketItems = MutableLiveData<List<MarketEntity>>()
    val marketItems: LiveData<List<MarketEntity>> = _marketItems

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    // variable para almacenar temporalmente la ruta de la imagen
    var currentPhotoPath: String? = null

    // metodo para crear el archivo de imagen
    fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val storageDir: File = getApplication<Application>().getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

    // guardar la ruta de la imagen en _imageUri
    fun saveImagePath(path: String) {
        _imageUri.value = Uri.parse(path)
    }

    fun fetchProducts() {
        _isLoading.value = true
        _errorMessage.value = null
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val products = repository.getProductsFromEntity()
                _marketItems.postValue(products)
            } catch (e: Exception) {
                handleException(e)
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    // Función para insertar un producto
    fun addProduct(itemName: String, quantity: Int, imageUrl: String?) {
        viewModelScope.launch {
            val newProduct = MarketEntity(itemName = itemName, quantity = quantity, imageUrl = imageUrl)
            repository.insertMarketItem(newProduct)
            fetchProducts() // Refrescar la lista después de insertar
            clearImageUri()
        }
    }

    // Función para obtener un producto por ID dentro de una corrutina
    fun loadProductById(productId: Int, callback: (MarketEntity?) -> Unit) {
        viewModelScope.launch {
            val product = repository.getProductById(productId)
            callback(product)
        }
    }

    // Función para actualizar un producto
    fun updateProduct(product: MarketEntity) {
        viewModelScope.launch {
            repository.updateMarketItem(product)
            fetchProducts()
            clearImageUri()
        }
    }

    // Función para eliminar un producto
    fun deleteProduct(item: MarketEntity) {
        viewModelScope.launch {
            repository.deleteMarketItem(item)
            fetchProducts()
        }
    }

    private val _imageUri = MutableStateFlow<Uri?>(null)
    val imageUri: StateFlow<Uri?> = _imageUri.asStateFlow()

    fun setImageUri(uri: Uri?) {
        _imageUri.value = uri
    }

    fun clearImageUri() {
        _imageUri.value = null
        currentPhotoPath = null
    }

    private fun handleException(exception: Exception) {
        when (exception) {
            is java.io.IOException -> _errorMessage.value = "Network error: Check your internet connection."
            else -> _errorMessage.value = "An unexpected error occurred."
        }
        exception.printStackTrace()
    }
}

class MarketViewModelFactory(
    private val application: Application,
    private val repository: MarketRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MarketViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MarketViewModel(application, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
